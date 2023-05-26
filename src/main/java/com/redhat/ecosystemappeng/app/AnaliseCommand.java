package com.redhat.ecosystemappeng.app;

import java.io.File;

import com.redhat.ecosystemappeng.client.BackendClientService;
import com.redhat.ecosystemappeng.client.BackendOptions;

import picocli.CommandLine;

@CommandLine.Command(name = "analise", description = "Analises the provided package manager file.")
public class AnaliseCommand implements Runnable {
    
    @CommandLine.Option(names = {"-v", "--verbose"}, description = "Verbose mode retrieves a more detailed report", defaultValue = "true")
    boolean verbose;

    @CommandLine.Option(names = {"--snyk-token"}, description = "Snyk Token to use during the analysis.")
    String snykToken;

    @CommandLine.Parameters(index = "0", description = "The package manager file to analise.")
    File file;

    private final BackendClientService clientService;

    public AnaliseCommand(BackendClientService clientService) {
        this.clientService = clientService;
    }

    @Override
    public void run() {
        clientService.analyize(file, new BackendOptions(verbose, snykToken));
    }
}
