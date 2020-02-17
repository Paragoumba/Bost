package fr.paragoumba.bost.api;

public class CommandInfo {

    public CommandInfo(String command, String usage){

        this.command = command;
        this.usage = usage;

    }

    private String command;
    private String usage;

    public String getCommand(){

        return command;

    }

    public String getUsage(){

        return usage;

    }
}
