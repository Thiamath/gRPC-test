package com.thiamath.user.app;

import org.fusesource.jansi.AnsiConsole;
import org.jline.console.SystemRegistry;
import org.jline.console.impl.SystemRegistryImpl;
import org.jline.reader.*;
import org.jline.reader.impl.DefaultParser;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import picocli.CommandLine;
import picocli.shell.jline3.PicocliCommands;

import java.io.PrintWriter;

public class App {
    public static void main(String[] args) {
        AnsiConsole.systemInstall();

        PicocliCommands.PicocliCommandsFactory factory = new PicocliCommands.PicocliCommandsFactory();

        final CommandLine cmd = new CommandLine(new CliCommands(), factory);
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

        AnsiConsole.systemUninstall();
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
                    SendCommand.class,
                    PicocliCommands.ClearScreen.class,
                    CommandLine.HelpCommand.class
            })
    static class CliCommands implements Runnable {
        PrintWriter out;

        CliCommands() {
        }

        public void run() {
            out.println(new CommandLine(this).getUsageMessage());
        }
    }

    @CommandLine.Command(name = "send", mixinStandardHelpOptions = true, version = "1",
            description = "Command to send objects to the user @|magenta server|@",
            subcommands = {CommandLine.HelpCommand.class})
    static class SendCommand implements Runnable {

        @Override
        public void run() {
            System.out.println(new CommandLine(this).getUsageMessage());
        }

        @CommandLine.Command(name = "user", subcommands = {CommandLine.HelpCommand.class},
                description = "Sends a user to the @|magenta server|@")
        public void sendUser(@CommandLine.Parameters(description = "User name", arity = "1") String username) {
            System.out.println("Sending user " + username);
        }

        @CommandLine.Command(name = "userlist", subcommands = {CommandLine.HelpCommand.class},
                description = "Sends a user to the server")
        public void sendUserList(@CommandLine.Parameters(description = "User name list", arity = "1..*") String[] username) {
            System.out.println("Sending user " + String.join(", ", username));
        }
    }
}
