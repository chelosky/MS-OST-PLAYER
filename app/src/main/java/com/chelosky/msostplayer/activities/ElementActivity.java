package com.chelosky.msostplayer.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import com.chelosky.msostplayer.R;
import com.chelosky.msostplayer.adapter.ItemSoundAdapter;
import com.chelosky.msostplayer.helpers.ConfigurationHelper;
import com.chelosky.msostplayer.helpers.DataHelper;
import com.chelosky.msostplayer.models.SoundModel;

import java.util.ArrayList;
import java.util.List;

public class ElementActivity extends AppCompatActivity {
    private List<SoundModel> soundModelList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_element);
        soundModelList = DataHelper.getOSTMSG(this, GetIDExtra());
        /*soundModelList.add(new SoundModel("Title (Opening)", "(00:25)"));
        soundModelList.add(new SoundModel("The Military System", "(00:55)"));
        soundModelList.add(new SoundModel("Main Theme From Metal Slug", "(04:12)"));
        soundModelList.add(new SoundModel("Steel Beast", "(02:07)"));
        soundModelList.add(new SoundModel("Carry Out", "(00:09)"));
        soundModelList.add(new SoundModel("Inner Station", "(04:28)"));
        soundModelList.add(new SoundModel("Assault Theme", "(04:16)"));
        soundModelList.add(new SoundModel("Ridge 256", "(03:08)"));
        soundModelList.add(new SoundModel("Gerhardt City", "(03:12)"));
        soundModelList.add(new SoundModel("Final Attack", "(03:13)"));
        soundModelList.add(new SoundModel("End Title", "(02:27)"));
        soundModelList.add(new SoundModel("Hold You Still!", "(03:49)"));
        soundModelList.add(new SoundModel("Gravestone", "(00:09)"));*/

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        if(soundModelList != null){
            LayoutAnimationController layoutAnimationController = null;
            layoutAnimationController = AnimationUtils.loadLayoutAnimation(this, R.anim.layout_slide_from_right );

            ItemSoundAdapter itemSoundAdapter = new ItemSoundAdapter(this, soundModelList, GetFolderExtra());
            recyclerView.setLayoutAnimation(layoutAnimationController);
            recyclerView.setAdapter(itemSoundAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }
    }

    private int GetIDExtra(){
        Bundle extras = getIntent().getExtras();
        return extras.getInt("ID");
    }

    private String GetFolderExtra(){
        Bundle extras = getIntent().getExtras();
        return extras.getString("FOLDER");
    }
}
