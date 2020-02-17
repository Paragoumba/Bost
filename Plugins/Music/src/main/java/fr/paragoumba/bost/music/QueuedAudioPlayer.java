package fr.paragoumba.bost.music;

import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import java.util.LinkedList;

public class QueuedAudioPlayer extends DefaultAudioPlayer {

    public QueuedAudioPlayer(DefaultAudioPlayerManager manager){

        super(manager);

        queuedTracks = new LinkedList<>();

        setPaused(true);

    }

    private LinkedList<AudioTrack> queuedTracks;

    public void queueTrack(AudioTrack track){

        queuedTracks.add(track);

        if (isPaused()){

            setPaused(false);

        }
    }

    @Override
    public void setPaused(boolean value){

        super.setPaused(value);

        if (!(queuedTracks.isEmpty() || value)){

            AudioTrack track = queuedTracks.getFirst();

            if (track.getState() == AudioTrackState.INACTIVE){

                playTrack(track);

            }
        }
    }

    public void skipTrack(){

        if (!queuedTracks.isEmpty()){

            queuedTracks.removeFirst();

            if (!queuedTracks.isEmpty()){

                AudioTrack nextTrack = queuedTracks.getFirst();

                if (nextTrack.getState() != AudioTrackState.PLAYING
                        && nextTrack.getState() != AudioTrackState.LOADING){

                    playTrack(nextTrack);

                }

            } else {

                setPaused(true);

            }
        }
    }

    public LinkedList<AudioTrack> getQueuedTracks(){

        return (LinkedList<AudioTrack>) queuedTracks.clone();

    }
}
