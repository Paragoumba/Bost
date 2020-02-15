package fr.paragoumba.bost.commands;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import fr.paragoumba.bost.*;
import fr.paragoumba.bost.api.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.managers.AudioManager;

import java.awt.*;
import java.util.logging.Logger;

public class PlayCommand extends Command {

    private static final Logger logger = Bot.getLogger();

    @Override
    public boolean execute(String command, String[] args, Member sender, MessageChannel channel){

        Music plugin = Music.getInstance();
        QueuedAudioPlayer player = plugin.getPlayer();
        QueuedAudioPlayerManager playerManager = plugin.getPlayerManager();

        TrackScheduler trackScheduler = new TrackScheduler();
        player.addListener(trackScheduler);

        GuildVoiceState voiceState = sender.getVoiceState();

        if (voiceState == null || !voiceState.inVoiceChannel()){

            MessageEmbed message = new EmbedBuilder()
                    .setTitle(":x: You are not in a voice channel")
                    .setDescription("Connect to a voice channel to play a music")
                    .setColor(Color.RED)
                    .build();

            channel.sendMessage(message).queue();

            return true;

        }

        if (args.length < 1){

            return false;

        }

        String identifier = String.join(" ", args);

        System.out.println("identifier: " + identifier);

        //playerManager.enableGcMonitoring();
        playerManager.loadItem(identifier, new AudioLoadResultHandler(){

            @Override
            public void trackLoaded(AudioTrack track){

                logger.info(track.getIdentifier() + " (" + track.getDuration() + ")");

                AudioManager audioManager = voiceState.getGuild().getAudioManager();

                audioManager.setSendingHandler(new AudioPlayerSendHandler(player));

                // Connect to the voice channel
                audioManager.openAudioConnection(voiceState.getChannel());

                player.queueTrack(track);

            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist){

                logger.info("playlist");

                System.out.println("Playlist size: " + playlist.getTracks().size());

                int i = 1;

                for (AudioTrack track : playlist.getTracks()){

                    float duration = (float) track.getDuration() / 1000 / 60;

                    logger.info('\t' + "- " + i + ": " + track.getInfo().title + " (" + (int) Math.floor(duration) + "'" + (int) (duration % 1 * 60) + "\")");

                    AudioManager audioManager = voiceState.getGuild().getAudioManager();

                    audioManager.setSendingHandler(new AudioPlayerSendHandler(player));

                    // Connect to the voice channel
                    audioManager.openAudioConnection(voiceState.getChannel());

                    player.queueTrack(track);

                    System.out.println("next track");

                    ++i;

                }
            }

            @Override
            public void noMatches(){

                logger.info("nothing");
                // Notify the user that we've got nothing

            }

            @Override
            public void loadFailed(FriendlyException throwable){

                // Notify the user that everything exploded
                if (throwable.severity == FriendlyException.Severity.COMMON){

                    // Forward message to user
                    throwable.printStackTrace();

                } else {

                    // Craft a message
                    System.out.println("nope");

                }
            }
        });

        return true;

    }
}
