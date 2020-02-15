package fr.paragoumba.bost.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;

public class QueuedAudioPlayerManager extends DefaultAudioPlayerManager {

    @Override
    protected AudioPlayer constructPlayer(){

        return new QueuedAudioPlayer(this);

    }
}
