package com.chelosky.msostplayer.adapter;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chelosky.msostplayer.R;
import com.chelosky.msostplayer.helpers.DownloaderHelper;
import com.chelosky.msostplayer.helpers.IOHelper;
import com.chelosky.msostplayer.helpers.SoundPlayerHelper;
import com.chelosky.msostplayer.helpers.UserPreferencesHelper;
import com.chelosky.msostplayer.models.SoundModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class ItemSoundAdapter extends RecyclerView.Adapter<ItemSoundAdapter.SoundHolder> {

    // Variables
    private Context context;
    private List<SoundModel> soundModelList;
    private String folderOst;
    private SoundHolder currentHolder = null;
    private SoundModel currentModel = null;

    public ItemSoundAdapter(Context context, List<SoundModel> soundModelList, String folderOst) {
        this.context = context;
        this.soundModelList = soundModelList;
        this.folderOst = folderOst;
    }

    @NonNull
    @Override
    public SoundHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sound_ost,parent,false);
        SoundHolder soundHolder = new SoundHolder(view);
        return soundHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final SoundHolder holder, final int position) {
        SoundModel soundModel = soundModelList.get(position);
        holder.name.setText(soundModel.getName());
        if(soundModel.getName().length() >= 35){;
            holder.name.setTextSize(15);
        }
        holder.information.setText(soundModel.getInformation());
        if (checkSound(soundModel.getNameFile())){
            if(SoundPlayerHelper.getInstance().IsPlayingMediaPlayer()){
                if(isTheCurrentSound(soundModel)){
                    holder.isPlaying = true;
                    holder.UpdateIconPlaying(true);
                }else{
                    holder.UpdateIconDownload(true);
                }
            }else{
                holder.UpdateIconDownload(true);
            }
        }else{
            holder.UpdateIconDownload(false);
        }
        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoundModel ost = soundModelList.get(position);
                if(!checkSound(ost.getNameFile())){
                    IOHelper.createExternalFolder(getFolderPath());
                    DownloadFile(holder ,getPathFile(ost.getNameFile()), ost);
                }else{
                    holder.ChangePlaying();
                    // SI ES NULL SIGNIFICA QUE NO HAY NADA REPRODUCIENDO
                    if(currentHolder == null){
                        currentHolder = holder;
                        currentModel = ost;
                        //PLAY MUSIC
                        SoundPlayerHelper.getInstance().PlayOst(getPathFile(ost.getNameFile()));
                    }else{
                        // SIGNIFICA QUE HAY ALGO REPRODUCIENDOSE
                        /* HAY 2 POSIBILIDADES
                                    1- ES EL MISMO SONIDO -> SOLO SE PARA LA MUSICA, SE CAMBIA EL ICONO Y SE LIBERA MEMORIA
                                    2- ES OTRO -> HAY QUE PARAR ESE OTRO Y PONER A REPRODUCIR EL ACTUAL
                        */
                        //Play(holder);
                        SoundPlayerHelper.getInstance().StopMusic();
                        if(isTheCurrentSound(ost)){
                            currentModel = null;
                            currentHolder = null;
                        }else{
                            //CHANGE CURRENTHOLDER
                            currentHolder.ChangePlaying();
                            //SET CURRENT
                            currentModel = ost;
                            currentHolder = holder;
                            //PLAY ACTUAL MUSICA
                            SoundPlayerHelper.getInstance().PlayOst(getPathFile(ost.getNameFile()));
                        }
                    }
                }
            }
        });
    }

    private String getFolderPath(){
        return UserPreferencesHelper.getNameFolderDownloads(context) + "/" + folderOst + "/";
    }

    private String getPathFile(String nameFile){
        return getFolderPath() + nameFile;
    }

    private Boolean checkSound(String nameFile){
        return IOHelper.checkExternalFile(getPathFile(nameFile));
    }

    private Boolean isTheCurrentSound(SoundModel soundModel){
        if(soundModel.getUrl().equals(currentModel.getUrl())){
            return  true;
        }
        return false;
    }

    private void DownloadFile(SoundHolder holder, String pathfile, SoundModel soundModel){
        DownloaderHelper.getInstance(context).DownloadMusic(holder, pathfile, soundModel);
    }

    @Override
    public int getItemCount() {
        return soundModelList.size();
    }

    public class SoundHolder extends RecyclerView.ViewHolder{
        //VARIABLES
        TextView name;
        TextView information;
        FloatingActionButton btn;
        public boolean isPlaying;

        public SoundHolder(@NonNull View itemView) {
            super(itemView);
            this.name = (TextView) itemView.findViewById(R.id.nameSound);
            this.information = (TextView) itemView.findViewById(R.id.informationSound);
            this.btn = (FloatingActionButton) itemView.findViewById(R.id.btnSound);
            this.isPlaying = false;
        }

        public void UpdateIconDownload(boolean value){
            if(value){
                this.btn.setImageResource(R.drawable.baseline_play_arrow_white_48dp);
            }else{
                this.btn.setImageResource(R.drawable.baseline_get_app_white_48dp);
            }
        }

        public void ChangePlaying(){
            this.isPlaying = !this.isPlaying;
            UpdateIconPlaying(this.isPlaying);
        }

        public void UpdateIconPlaying(boolean value){
            if(value){
                this.btn.setImageResource(R.drawable.baseline_stop_white_48dp);
            }else{
                this.btn.setImageResource(R.drawable.baseline_play_arrow_white_48dp);
            }
        }
    }
}
