package com.chelosky.msostplayer.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.animation.ArgbEvaluator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Toast;

import com.chelosky.msostplayer.R;
import com.chelosky.msostplayer.helpers.DataHelper;
import com.chelosky.msostplayer.helpers.SoundPlayerHelper;
import com.chelosky.msostplayer.helpers.UserPreferencesHelper;
import com.chelosky.msostplayer.models.ItemMainModel;
import com.chelosky.msostplayer.adapter.itemMainAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends AppCompatActivity {

    ViewPager viewPager;
    itemMainAdapter adapter;
    List<ItemMainModel> modelList;
    Integer[] colors = null;
    ArgbEvaluator argbEvaluator = new ArgbEvaluator();
    SoundPool soundPool;
    private int soundSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        modelList = new ArrayList<>();
        modelList = DataHelper.getGameItems(this);
        adapter = new itemMainAdapter(this, modelList);
        viewPager.setAdapter(adapter);
        LayoutAnimationController layoutAnimationController = null;
        layoutAnimationController = AnimationUtils.loadLayoutAnimation(this,  R.anim.layout_fall_down);
        viewPager.setLayoutAnimation(layoutAnimationController);
        viewPager.setPadding(130,0,130,0);
        colors = new Integer[]{
                getResources().getColor(R.color.color1),
                getResources().getColor(R.color.color2),
                getResources().getColor(R.color.color3),
                getResources().getColor(R.color.color4),
                getResources().getColor(R.color.color3),
                getResources().getColor(R.color.color2),
                getResources().getColor(R.color.color1),
                getResources().getColor(R.color.color2),
                getResources().getColor(R.color.color3)
        };
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position < (adapter.getCount() - 1) && position < (colors.length - 1)){
                    viewPager.setBackgroundColor(
                            (Integer)argbEvaluator.evaluate(positionOffset,colors[position],colors[position+1])
                    );
                }else{
                    viewPager.setBackgroundColor(colors[colors.length - 1]);
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC,1);
        soundSelected = soundPool.load(this, R.raw.selected_gun,1);
        FloatingActionButton btnDev = (FloatingActionButton)findViewById(R.id.btnDev);
        btnDev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, ConfigurationActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
            }
        });
    }
    public void OpenElementOst(int position, ItemMainModel itemMainModel){
        if(position == viewPager.getCurrentItem()){
            if(UserPreferencesHelper.getSoundEffectApp(this)){
                soundPool.play(soundSelected,1,1,1,0,0);
            }
            Intent elementIntent = new Intent(MenuActivity.this, ElementActivity.class);
            elementIntent.putExtra("ID",itemMainModel.getId());
            elementIntent.putExtra("FOLDER", itemMainModel.getFolder());
            startActivity(elementIntent);
            overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
        }else{
            viewPager.setCurrentItem(position);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        SoundPlayerHelper.getInstance().StopPropagationSound();
    }


}