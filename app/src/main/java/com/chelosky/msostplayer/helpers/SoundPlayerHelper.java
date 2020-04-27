package com.chelosky.msostplayer.helpers;

import android.media.MediaPlayer;
import android.os.Environment;
import android.widget.Toast;

public class SoundPlayerHelper {
    private static SoundPlayerHelper instance;
    MediaPlayer mediaPlayer;

    public static SoundPlayerHelper getInstance(){
        if(instance == null){
            instance = new SoundPlayerHelper();
        }
        return instance;
    }

    private SoundPlayerHelper(){
        this.mediaPlayer = new MediaPlayer();
    }

    public boolean PlayOst(String pathFile){
        try {
            mediaPlayer.setDataSource(Environment.getExternalStorageDirectory()+"/"+pathFile);
            mediaPlayer.prepare();
            mediaPlayer.start();
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public void StopMusic(){
        mediaPlayer.stop();
        mediaPlayer.release();
        this.mediaPlayer = new MediaPlayer();
    }


    public boolean IsPlayingMediaPlayer(){
        return mediaPlayer.isPlaying();
    }

    public void StopPropagationSound(){
        if(IsPlayingMediaPlayer()){
            StopMusic();
        }
    }
}
