package fr.paragoumba.bost.music.commands;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import fr.paragoumba.bost.music.Music;
import fr.paragoumba.bost.music.QueuedAudioPlayer;
import fr.paragoumba.bost.api.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.*;

public class QueueCommand extends Command {

    @Override
    public boolean execute(String command, String[] args, Member sender, MessageChannel channel){

        Music plugin = Music.getInstance();
        QueuedAudioPlayer player = plugin.getPlayer();

        StringBuilder builder = new StringBuilder();
        int i = 1;

        for (AudioTrack track : player.getQueuedTracks()){

            AudioTrackInfo trackInfo = track.getInfo();

            if (i == 1 && player.isPaused()){

                builder
                        .append("Track playing:\n")
                        .append('[').append(trackInfo.title).append("](").append(trackInfo.uri).append(")\n\n");

            } else {

                builder.append(i).append(". [").append(trackInfo.title).append("](").append(trackInfo.uri).append(")\n");

            }

            ++i;

        }

        MessageEmbed message = new EmbedBuilder()
                .setTitle(":headphones: Queued songs")
                .setDescription(builder)
                .setColor(Color.RED)
                .build();

        channel.sendMessage(message).queue();

        return true;

    }
}
