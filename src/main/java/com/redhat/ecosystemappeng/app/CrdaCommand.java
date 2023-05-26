package com.redhat.ecosystemappeng.app;

import io.quarkus.picocli.runtime.annotations.TopCommand;
import picocli.CommandLine;

@TopCommand
@CommandLine.Command(name = "crda", mixinStandardHelpOptions = true, subcommands = {AnaliseCommand.class})
public class CrdaCommand {
    
}
