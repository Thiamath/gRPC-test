package com.thiamath.user.app;

import com.thiamath.user.client.Controller;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.fusesource.jansi.AnsiConsole;
import org.jline.console.SystemRegistry;
import org.jline.console.impl.SystemRegistryImpl;
import org.jline.reader.*;
import org.jline.reader.impl.DefaultParser;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import picocli.CommandLine;
import picocli.shell.jline3.PicocliCommands;

import java.util.concurrent.TimeUnit;

public class App {
    public static void main(String[] args) throws InterruptedException {
        AnsiConsole.systemInstall();

        PicocliCommands.PicocliCommandsFactory factory = new PicocliCommands.PicocliCommandsFactory();

        final String target = "localhost:8980";
        final ManagedChannel channel = ManagedChannelBuilder.forTarget(target).usePlaintext().build();

        try {
            final AppContext context = new AppContext(new Controller(channel));
            final CommandLine cmd = new CommandLine(new CliCommands(context), factory);
            final PicocliCommands commands = new PicocliCommands(cmd);

            final Parser parser = new DefaultParser();
            try (final Terminal terminal = TerminalBuilder.builder().build()) {
                final SystemRegistry systemRegistry = new SystemRegistryImpl(parser, terminal, null, null);
                systemRegistry.setCommandRegistries(commands);
                systemRegistry.register("help", commands);

                final LineReader reader = LineReaderBuilder.builder()
                        .terminal(terminal)
                        .completer(systemRegistry.completer())
                        .parser(parser)
                        .variable(LineReader.LIST_MAX, 50)
                        .build();

                context.setLineReader(reader);

                final String prompt = "$> ";
                final String rightPrompt = " <final>";
                String line;
                while (true) {
                    try {
                        systemRegistry.cleanUp();
                        line = reader.readLine(prompt, rightPrompt, (MaskingCallback) null, null);
                        systemRegistry.execute(line);
                    } catch (UserInterruptException e) {
                        // Ignore
                    } catch (EndOfFileException e) {
                        return;
                    } catch (Exception e) {
                        systemRegistry.trace(e);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } finally {
            System.out.println("TERMINATING...");
            channel.shutdownNow().awaitTermination(60, TimeUnit.SECONDS);
            AnsiConsole.systemUninstall();
        }
    }

    /**
     * Top-level command that just prints help.
     */
    @CommandLine.Command(name = "",
            description = {
                    "Example interactive shell with completion and autosuggestions. " +
                            "Hit @|magenta <TAB>|@ to see available commands.",
                    "Hit @|magenta ALT-S|@ to toggle tailtips.",
                    ""},
            footer = {"", "Press Ctrl-D to exit."},
            subcommands = {
                    GetCommand.class,
                    SendCommand.class,
                    ChatCommand.class,
                    PicocliCommands.ClearScreen.class,
                    CommandLine.HelpCommand.class
            })
    static class CliCommands implements Runnable {

        private final AppContext context;

        CliCommands(final AppContext context) {
            this.context = context;
        }

        public void run() {
        }

        public AppContext getContext() {
            return context;
        }
    }

}
