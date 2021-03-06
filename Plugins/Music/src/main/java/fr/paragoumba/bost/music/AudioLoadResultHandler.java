package fr.paragoumba.bost.music;

import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import fr.paragoumba.bost.EmbedColor;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.managers.AudioManager;

import java.util.List;

public class AudioLoadResultHandler implements com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler {

    private static final QueuedAudioPlayer player = Music.getInstance().getPlayer();
    private static final String ytThumbnailUrl = "https://img.youtube.com/vi/%i/hqdefault.jpg";

    public AudioLoadResultHandler(GuildVoiceState voiceState, String identifier, MessageChannel channel){

        this.voiceState = voiceState;
        this.identifier = identifier;
        this.channel = channel;

    }

    private final GuildVoiceState voiceState;
    private final String identifier;
    private final MessageChannel channel;

    @Override
    public void trackLoaded(AudioTrack track){

        if (!isConnectedToChannel()){

            joinChannel();

        }

        player.queueTrack(track);
        sendTrackEmbed(track);

    }

    @Override
    public void playlistLoaded(AudioPlaylist playlist){

        List<AudioTrack> tracks = playlist.getTracks();

        if (!isConnectedToChannel()){

            joinChannel();

        }

        if (playlist.isSearchResult()){

            AudioTrack selectedTrack = tracks.get(0);

            player.queueTrack(selectedTrack);
            sendTrackEmbed(selectedTrack);

        } else {

            int i = 1;

            for (AudioTrack track : tracks){

                player.queueTrack(track);

                ++i;

            }

            MessageEmbed message = new EmbedBuilder()
                    .setTitle(":cd: Loaded " + i + " tracks")
                    .build();

            channel.sendMessage(message).queue();

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

        MessageEmbed message;

        if (throwable.severity == FriendlyException.Severity.COMMON){

            message = new EmbedBuilder()
                    .setTitle(":x: Error when playing track")
                    .setDescription(throwable.getLocalizedMessage())
                    .build();

        } else {

            message = new EmbedBuilder()
                    .setTitle(":x: Error when playing track")
                    .setDescription("Sorry :woman_shrugging:")
                    .build();

        }

        channel.sendMessage(message).queue();

    }

    private void sendTrackEmbed(AudioTrack track){

        AudioTrackInfo trackInfo = track.getInfo();

        MessageEmbed message = new EmbedBuilder()
                .setTitle("Added track to queue")
                .setDescription('[' + trackInfo.title + "](" + trackInfo.uri + ')')
                .setThumbnail(ytThumbnailUrl.replaceAll("%i", trackInfo.identifier))
                .addField("Channel", trackInfo.author, true)
                .addField("Duration", QueuedAudioPlayer.getDuration(track), true)
                .setColor(EmbedColor.INFO)
                .build();

        channel.sendMessage(message).queue();

    }

    private void joinChannel(){

        AudioManager audioManager = voiceState.getGuild().getAudioManager();

        audioManager.setSendingHandler(new AudioPlayerSendHandler(player));
        audioManager.openAudioConnection(voiceState.getChannel());

    }

    private void quitChannel(){

        voiceState.getGuild().getAudioManager().closeAudioConnection();

    }

    private boolean isConnectedToChannel(){

        AudioManager audioManager = voiceState.getGuild().getAudioManager();

        return audioManager.isConnected();

    }
}
