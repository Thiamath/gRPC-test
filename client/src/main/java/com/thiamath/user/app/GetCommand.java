package com.thiamath.user.app;

import picocli.CommandLine;
import picocli.CommandLine.ParentCommand;

@CommandLine.Command(name = "get", mixinStandardHelpOptions = true, version = "1",
        description = "Command to get objects to the user @|magenta server|@",
        subcommands = {CommandLine.HelpCommand.class})
class GetCommand implements Runnable {

    @ParentCommand
    private App.CliCommands parent;

    @Override
    public void run() {
        System.out.println(new CommandLine(this).getUsageMessage());
    }

    @CommandLine.Command(name = "user", subcommands = {CommandLine.HelpCommand.class},
            description = "Gets a user from the @|magenta server|@")
    public void getUser(@CommandLine.Parameters(description = "User name", arity = "1") String username) {
        parent.getContext().getController().getUser(username);
    }

    @CommandLine.Command(name = "userlist", subcommands = {CommandLine.HelpCommand.class},
            description = "Gets a list of users from the server")
    public void getUserList() {
        parent.getContext().getController().getUserList();
    }
}
