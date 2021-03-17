package com.thiamath.user.app;

import com.diogonunes.jcolor.Ansi;
import com.diogonunes.jcolor.Attribute;
import com.thiamath.user.client.Controller;
import com.thiamath.user.client.UserClient;
import org.jline.reader.LineReader;
import picocli.CommandLine;
import picocli.CommandLine.ParentCommand;

@CommandLine.Command(name = "chat", mixinStandardHelpOptions = true, version = "1",
        description = "Command to start a chat with the @|magenta server|@",
        subcommands = {CommandLine.HelpCommand.class})
class ChatCommand implements Runnable {

    @ParentCommand
    private App.CliCommands parent;

    @Override
    public void run() {
        UserClient.Chat chat = parent.getContext().getController().startChat();
        LineReader reader = parent.getContext().getLineReader();
        boolean endChat = true;
        while (endChat) {
            String line = reader.readLine("Chat with SERVER> ");
            if ("quit".equals(line)) {
                endChat = false;
                chat.finish();
            } else {
                final String response = chat.say(line);
                System.out.println(Ansi.colorize("SERVER SAYS: ", Attribute.YELLOW_TEXT()) +
                        Ansi.colorize(response, Attribute.CYAN_TEXT()));
            }

        }
    }
}
