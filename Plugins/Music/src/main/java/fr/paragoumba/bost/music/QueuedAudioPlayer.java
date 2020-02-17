package fr.paragoumba.bost.music;

import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackState;
import fr.paragoumba.bost.Bot;

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

        //return (LinkedList<AudioTrack>) queuedTracks.clone();
        return queuedTracks;

    }

    public void removeTrack(int i){

        queuedTracks.remove(i);

    }

    public void clearQueue(){

        queuedTracks.clear();

    }

    public String getDuration(AudioTrack track){

        String format = Bot.getConfig().getString("music.timeformat");

        if (format == null){

            format = "%mm%ss";

        }

        float duration = (float) track.getDuration() / 60000;

        return format
                .replaceAll("%m", String.valueOf((int) duration))
                .replaceAll("%s", String.valueOf((int) (duration % 1 * 60)));

    }
}
