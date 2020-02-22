package fr.paragoumba.bost.music;

import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import fr.paragoumba.bost.Bot;
import fr.paragoumba.bost.EmbedColor;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.managers.AudioManager;

import java.util.List;
import java.util.logging.Logger;

public class AudioLoadResultHandler implements com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler {

    private static final Logger logger = Bot.getLogger();
    private static final QueuedAudioPlayer player = Music.getInstance().getPlayer();

    public AudioLoadResultHandler(GuildVoiceState voiceState, String identifier, MessageChannel channel){

        this.voiceState = voiceState;
        this.identifier = identifier;
        this.channel = channel;

    }

    private GuildVoiceState voiceState;
    private String identifier;
    private MessageChannel channel;

    @Override
    public void trackLoaded(AudioTrack track){

        logger.info(track.getIdentifier() + " (" + track.getDuration() + ")");

        if (!isConnectedToChannel()){

            joinChannel();

        }

        player.queueTrack(track);

    }

    @Override
    public void playlistLoaded(AudioPlaylist playlist){

        List<AudioTrack> tracks = playlist.getTracks();

        if (!isConnectedToChannel()){

            joinChannel();

        }

        if (playlist.isSearchResult()){

            player.queueTrack(tracks.get(0));

        } else {

            int i = 1;

            for (AudioTrack track : tracks){

                logger.info('\t' + "- " + i + ": " + track.getInfo().title + " (" + player.getDuration(track) + ")");

                player.queueTrack(track);

                ++i;

            }
        }
    }

    @Override
    public void noMatches(){

        MessageEmbed message = new EmbedBuilder()
                .setTitle(":mag_right: Couldn't find a song")
                .setDescription("Identifier: '" + identifier + '\'')
                .setColor(EmbedColor.ERROR)
                .build();

        channel.sendMessage(message).queue();

    }

    @Override
    public void loadFailed(FriendlyException throwable){

        if (throwable.severity == FriendlyException.Severity.COMMON){

            MessageEmbed message = new EmbedBuilder()
                    .setTitle(":x: Error when playing track")
                    .setDescription(throwable.getLocalizedMessage())
                    .build();

            channel.sendMessage(message).queue();

        } else {

            MessageEmbed message = new EmbedBuilder()
                    .setTitle(":x: Error when playing track")
                    .setDescription("Sorry :woman_shrugging:")
                    .build();

            channel.sendMessage(message).queue();

        }
    }

    private void joinChannel(){

        AudioManager audioManager = voiceState.getGuild().getAudioManager();

        audioManager.setSendingHandler(new AudioPlayerSendHandler(player));

        // Connect to the voice channel
        audioManager.openAudioConnection(voiceState.getChannel());

    }

    private void quitChannel(){

        AudioManager audioManager = voiceState.getGuild().getAudioManager();

        audioManager.closeAudioConnection();

    }

    private boolean isConnectedToChannel(){

        AudioManager audioManager = voiceState.getGuild().getAudioManager();

        return audioManager.isConnected();

    }
}
