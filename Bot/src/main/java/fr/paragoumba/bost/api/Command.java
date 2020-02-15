package fr.paragoumba.bost.api;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;

public abstract class Command {

    public abstract boolean execute(String command, String[] args, Member sender, MessageChannel channel) throws Exception;

}
