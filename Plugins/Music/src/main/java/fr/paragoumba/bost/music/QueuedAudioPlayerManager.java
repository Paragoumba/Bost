package fr.paragoumba.bost.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;

public class QueuedAudioPlayerManager extends DefaultAudioPlayerManager {

    public QueuedAudioPlayerManager(){

        super();

        AudioSourceManagers.registerRemoteSources(this);

    }

    @Override
    protected AudioPlayer constructPlayer(){

        return new QueuedAudioPlayer(this);

    }
}
