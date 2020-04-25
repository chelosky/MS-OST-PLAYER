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
import com.chelosky.msostplayer.helpers.IOHelper;
import com.chelosky.msostplayer.helpers.UserPreferencesHelper;
import com.chelosky.msostplayer.models.SoundModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class ItemSoundAdapter extends RecyclerView.Adapter<ItemSoundAdapter.SoundHolder> {

    // Variables
    private Context context;
    private List<SoundModel> soundModelList;
    private String folderOst;

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
    public void onBindViewHolder(@NonNull SoundHolder holder, final int position) {
        SoundModel soundModel = soundModelList.get(position);
        holder.name.setText(soundModel.getName());
        if(soundModel.getName().length() >= 35){;
            holder.name.setTextSize(15);
        }
        holder.information.setText(soundModel.getInformation());
        if(checkSound(soundModel.getNameFile())){
            holder.btn.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.baseline_play_arrow_white_48dp));
        }else{
            holder.btn.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.baseline_get_app_white_48dp));
        }
        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!checkSound(soundModelList.get(position).getNameFile())){
                    IOHelper.createExternalFolder(UserPreferencesHelper.getNameFolderDownloads(context)
                            + "/" + folderOst + "/");
                    DownloadFile(UserPreferencesHelper.getNameFolderDownloads(context)
                            + "/" + folderOst + "/" + soundModelList.get(position).getNameFile());
                }else{
                    Play();
                }
            }
        });
    }

    private Boolean checkSound(String nameFile){
        return IOHelper.checkExternalFile(UserPreferencesHelper.getNameFolderDownloads(context)
                + "/" + folderOst + "/" + nameFile);
    }

    private void Play(){
        //todo
    }

    private void DownloadFile(String value){
        Toast.makeText(context, value,Toast.LENGTH_SHORT).show();
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

        public SoundHolder(@NonNull View itemView) {
            super(itemView);
            this.name = (TextView) itemView.findViewById(R.id.nameSound);
            this.information = (TextView) itemView.findViewById(R.id.informationSound);
            this.btn = (FloatingActionButton) itemView.findViewById(R.id.btnSound);
        }
    }
}
