package fr.paragoumba.bost.music.commands;

import fr.paragoumba.bost.EmbedColor;
import fr.paragoumba.bost.api.Command;
import fr.paragoumba.bost.music.*;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class PlayCommand extends Command {

    @Override
    public boolean execute(String command, String[] args, Member sender, MessageChannel channel){

        Music plugin = Music.getInstance();
        QueuedAudioPlayer player = plugin.getPlayer();

        if (args.length < 1){

            player.setPaused(!player.isPaused());

            return true;

        }

        QueuedAudioPlayerManager playerManager = plugin.getPlayerManager();

        TrackScheduler trackScheduler = new TrackScheduler();
        player.addListener(trackScheduler);

        GuildVoiceState voiceState = sender.getVoiceState();

        if (voiceState == null || !voiceState.inVoiceChannel()){

            MessageEmbed message = new EmbedBuilder()
                    .setTitle(":x: You are not in a voice channel")
                    .setDescription("Connect to a voice channel to play a music")
                    .setColor(EmbedColor.ERROR)
                    .build();

            channel.sendMessage(message).queue();

            return true;

        }

        String identifier = String.join(" ", args);

        //playerManager.enableGcMonitoring();
        playerManager.loadItem(identifier, new AudioLoadResultHandler(voiceState, identifier, channel));

        return true;

    }
}
