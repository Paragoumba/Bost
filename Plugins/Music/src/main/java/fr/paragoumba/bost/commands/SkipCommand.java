package fr.paragoumba.bost.commands;

import fr.paragoumba.bost.Music;
import fr.paragoumba.bost.QueuedAudioPlayer;
import fr.paragoumba.bost.api.Command;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;

public class SkipCommand extends Command {

    @Override
    public boolean execute(String command, String[] args, Member sender, MessageChannel channel){

        Music plugin = Music.getInstance();
        QueuedAudioPlayer player = plugin.getPlayer();

        player.skipTrack();

        System.out.println("Skipping music.");

        return true;

    }
}
