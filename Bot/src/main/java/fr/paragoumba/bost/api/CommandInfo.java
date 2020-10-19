package fr.paragoumba.bost.api;

public class CommandInfo {

    public CommandInfo(String command, String... usage){

        this.command = command;
        this.usage = usage;

    }

    private final String command;
    private final String[] usage;

    public String getCommand(){

        return command;

    }

    public String[] getUsage(){

        return usage;

    }
}
