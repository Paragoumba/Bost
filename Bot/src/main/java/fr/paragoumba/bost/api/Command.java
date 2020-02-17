package fr.paragoumba.bost.api;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;

public abstract class Command {

    private CommandInfo info;

    public abstract boolean execute(String command, String[] args, Member sender, MessageChannel channel) throws Exception;

    public void setInfo(CommandInfo info){

        this.info = info;

    }

    public CommandInfo getInfo(){

        return info;

    }
}
