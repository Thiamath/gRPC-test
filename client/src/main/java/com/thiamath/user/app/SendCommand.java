package com.thiamath.user.app;

import picocli.CommandLine;
import picocli.CommandLine.ParentCommand;

@CommandLine.Command(name = "send", mixinStandardHelpOptions = true, version = "1",
        description = "Command to send objects to the user @|magenta server|@",
        subcommands = {CommandLine.HelpCommand.class})
class SendCommand implements Runnable {

    @ParentCommand
    private App.CliCommands parent;

    @Override
    public void run() {
        System.out.println(new CommandLine(this).getUsageMessage());
    }

    @CommandLine.Command(name = "user", subcommands = {CommandLine.HelpCommand.class},
            description = "Send a user from the @|magenta server|@")
    public void sendUser(@CommandLine.Parameters(description = "User name", arity = "1") String username) {
        parent.getContext().getController().sendUser(username);
    }

    @CommandLine.Command(name = "userlist", subcommands = {CommandLine.HelpCommand.class},
            description = "Gets a list of users from the server")
    public void sendUserList(@CommandLine.Parameters(description = "User name list", arity = "1..*") String[] usernameList) {
        parent.getContext().getController().sendUserList(usernameList);
    }
}
