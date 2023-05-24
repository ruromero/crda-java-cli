package com.redhat.ecosystemappeng.client;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.resteasy.reactive.ClientWebApplicationException;

import com.redhat.ecosystemappeng.crda.api.AnalysisReport;
import com.redhat.ecosystemappeng.service.PackageManagerService;
import com.redhat.ecosystemappeng.service.PackageManagerServiceProvider;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import jakarta.enterprise.context.Dependent;

@Dependent
public class BackendClientService {

    @RestClient
    DependencyAnalysisService dependencyAnalysisService;

    public void analyize(File file, BackendOptions options) {
        try {
            PackageManagerService svc = PackageManagerServiceProvider.get(file);
            AnalysisReport report = dependencyAnalysisService.createReport(svc.getName(), options.verbose(), options.snykToken(), svc.generateSbom(file.toPath()));
            processReport(report);
        } catch (IOException e) {
            System.out.println("ERROR: Unable to read file: " + file.toPath() + ". " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("ERROR: " + e.getMessage());
        } catch (ClientWebApplicationException e) {
            System.out.println("ERROR: Unable to process request in the backend. " + e.getMessage());
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
    }

}
