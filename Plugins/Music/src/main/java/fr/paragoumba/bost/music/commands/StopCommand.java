package fr.paragoumba.bost.music.commands;

import fr.paragoumba.bost.api.Command;
import fr.paragoumba.bost.music.Music;
import fr.paragoumba.bost.music.QueuedAudioPlayer;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.managers.AudioManager;

public class StopCommand extends Command {

    @Override
    public boolean execute(String command, String[] args, Member sender, MessageChannel channel){

        Music plugin = Music.getInstance();
        QueuedAudioPlayer player = plugin.getPlayer();

        player.setPaused(true);
        player.clearQueue();

        AudioManager audioManager = sender.getGuild().getAudioManager();

        if (audioManager.isConnected()){

            audioManager.closeAudioConnection();

        }

        return true;

    }
}
