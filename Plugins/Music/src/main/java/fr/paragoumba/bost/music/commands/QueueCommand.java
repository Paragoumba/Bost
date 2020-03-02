package fr.paragoumba.bost.music.commands;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import fr.paragoumba.bost.EmbedColor;
import fr.paragoumba.bost.music.Music;
import fr.paragoumba.bost.music.QueuedAudioPlayer;
import fr.paragoumba.bost.api.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.util.LinkedList;

public class QueueCommand extends Command {

    @Override
    public boolean execute(String command, String[] args, Member sender, MessageChannel channel){

        Music plugin = Music.getInstance();
        QueuedAudioPlayer player = plugin.getPlayer();

        LinkedList<AudioTrack> queuedTracks = player.getQueuedTracks();

        if (!queuedTracks.isEmpty()){

            StringBuilder builder = new StringBuilder();
            int i = 0;

            for (AudioTrack track : queuedTracks){

                StringBuilder trackBuilder = new StringBuilder();

                if (i == 0 && !player.isPaused()){

                    trackBuilder.append("Track playing:\n").append(formatTrack(track)).append('\n');

                } else {

                    trackBuilder.append(i).append(". ").append(formatTrack(track));

                }

                if (builder.length() + trackBuilder.length() > 2048){

                    sendQueueMessage(channel, builder.toString());
                    builder = new StringBuilder();

                }

                builder.append(trackBuilder);

                ++i;

            }

            sendQueueMessage(channel, builder.toString());

        } else {

            MessageEmbed message = new EmbedBuilder()
                    .setDescription(":mute: No queued songs")
                    .setColor(EmbedColor.INFO)
                    .build();

            channel.sendMessage(message).queue();

        }

        return true;

    }

    private void sendQueueMessage(MessageChannel channel, String description){

        MessageEmbed message = new EmbedBuilder()
                .setTitle(":headphones: Queued songs")
                .setDescription(description)
                .setColor(EmbedColor.INFO)
                .build();

        channel.sendMessage(message).queue();

    }

    private String formatTrack(AudioTrack track){

        Music plugin = Music.getInstance();
        QueuedAudioPlayer player = plugin.getPlayer();

        StringBuilder builder = new StringBuilder();
        AudioTrackInfo trackInfo = track.getInfo();

        builder.append('[').append(trackInfo.title).append("](").append(trackInfo.uri).append(") (")
                .append(player.getDuration(track)).append(")\n");

        return builder.toString();

    }
}
