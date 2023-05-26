package com.redhat.ecosystemappeng.client;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.resteasy.reactive.ClientWebApplicationException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.redhat.ecosystemappeng.crda.api.AnalysisReport;
import com.redhat.ecosystemappeng.service.PackageManagerService;
import com.redhat.ecosystemappeng.service.PackageManagerServiceProvider;

import jakarta.enterprise.context.Dependent;
import jakarta.ws.rs.core.Response;

@Dependent
public class BackendClientService {

    @RestClient
    DependencyAnalysisService dependencyAnalysisService;

    private final ObjectMapper mapper = new ObjectMapper();

    public void analyize(File file, BackendOptions options) {
        try {
            PackageManagerService svc = PackageManagerServiceProvider.get(file);
            Response response = dependencyAnalysisService.createReport(svc.getName(), options.verbose(), options.snykToken(), svc.generateSbom(file.toPath()));
            DepAnalysisDTO dto = processResponse(response);
            processReport(dto.report());
            saveHtmlReport(dto.html());
        } catch (IOException e) {
            System.out.println("ERROR: Unable to read file: " + file.toPath() + ". " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("ERROR: " + e.getMessage());
        } catch (ClientWebApplicationException e) {
            System.out.println("ERROR: Unable to process request in the backend. " + e.getMessage());
        }
    }

    private DepAnalysisDTO processResponse(Response response) {
        Map<String, String> params = response.getMediaType().getParameters();
        String boundary = params.get("boundary");
        if(boundary == null) {
            System.out.println("Missing response boundary");
            return null;
        }
        String body = response.readEntity(String.class);
        String[] lines = body.split("\n");
        int cursor = 0;
        while(!lines[cursor].contains(boundary)) {
            cursor++;
        }
        cursor++;
        while(lines[cursor].startsWith("Content-") || lines[cursor].isBlank()) {
            cursor++;
        }
        StringBuffer json = new StringBuffer();
        while(!lines[cursor].contains(boundary)) {
            json.append(lines[cursor++]);
        }
        while(!lines[cursor].contains(boundary)) {
            cursor++;
        }
        cursor++;
        while(lines[cursor].startsWith("Content-") || lines[cursor].isBlank()) {
            cursor++;
        }
        StringBuffer html = new StringBuffer();
        while(!lines[cursor].contains(boundary)) {
            html.append(lines[cursor++]);
        }
        try {
            return new DepAnalysisDTO(mapper.readValue(json.toString(), AnalysisReport.class), html.toString());
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    private void processReport(AnalysisReport report) {
        System.out.println("Summary");
        System.out.println("  Dependencies");
        System.out.println("    Scanned dependencies:    " + report.summary().dependencies().scanned());
        System.out.println("    Transitive dependencies: " + report.summary().dependencies().transitive());
        System.out.println("  Vulnerabilities");
        System.out.println("    Total: " + report.summary().vulnerabilities().total());
        System.out.println("    Direct: " + report.summary().vulnerabilities().direct());
        System.out.println("    Critical: " + report.summary().vulnerabilities().critical());
        System.out.println("    High: " + report.summary().vulnerabilities().high());
        System.out.println("    Medium: " + report.summary().vulnerabilities().medium());
        System.out.println("    Low: " + report.summary().vulnerabilities().low());
        System.out.println("");
    }

    private void saveHtmlReport(String html) {
        try {
            Path temp = Files.createTempFile("dependency-analysis-report", ".html");
            BufferedWriter writer = Files.newBufferedWriter(temp);
            writer.append(html);
            writer.close();
            System.out.println("You can find the detailed HTML report in: " + temp);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
