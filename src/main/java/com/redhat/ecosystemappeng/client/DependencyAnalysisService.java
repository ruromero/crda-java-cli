package com.redhat.ecosystemappeng.client;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import com.redhat.ecosystemappeng.crda.api.AnalysisReport;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

@Path("/")
@RegisterRestClient
public interface DependencyAnalysisService {

    public static final String SNYK_TOKEN_HEADER = "crda-snyk-token";
    public static final String VERBOSE_MODE_HEADER = "verbose";
    public static final String TEXT_VND_GRAPHVIZ = "text/vnd.graphviz";

    @POST
    @Path("/{pkgManager}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    AnalysisReport createReport(
        @PathParam("pkgManager") String pkgManager,
        @QueryParam(VERBOSE_MODE_HEADER) boolean verbose,
        @HeaderParam(SNYK_TOKEN_HEADER)
        String snykToken,
        String fileContent
    );
    
}
