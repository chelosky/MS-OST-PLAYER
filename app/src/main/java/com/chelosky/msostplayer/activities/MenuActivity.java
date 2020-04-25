package com.chelosky.msostplayer.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.animation.ArgbEvaluator;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.Toast;

import com.chelosky.msostplayer.R;
import com.chelosky.msostplayer.helpers.ConfigurationHelper;
import com.chelosky.msostplayer.helpers.DataHelper;
import com.chelosky.msostplayer.helpers.UserPreferencesHelper;
import com.chelosky.msostplayer.models.ItemMainModel;
import com.chelosky.msostplayer.adapter.itemMainAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
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

    MediaPlayer mediaPlayer;
    Button bntPlay;

    Button txtSiteName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        isStoragePermissionGranted();
        modelList = new ArrayList<>();
        modelList = DataHelper.getGameItems(this);
        /*modelList.add(new ItemMainModel(1,R.drawable.cover, "Metal Slug: Super Vehicle-001", "Composed by: HIYA!, JIM","MS1"));
        modelList.add(new ItemMainModel(2,R.drawable.cover2, "Metal Slug 2", "Composed by: HIYA!","MS2"));
        modelList.add(new ItemMainModel(3,R.drawable.coverx, "Metal Slug X", "Composed by: HIYA!, Captain Beroou","MSX"));
        modelList.add(new ItemMainModel(4,R.drawable.cover3 , "Metal Slug 3", "Composed by: HIYA!, Captain Beroou, Hori_Hori, Q_JIROU, maitaro","MS3"));
        modelList.add(new ItemMainModel(5,R.drawable.cover4 , "Metal Slug 4", "Composed by: Toshikazu Tanaka","MS4"));
        modelList.add(new ItemMainModel(6,R.drawable.cover5 , "Metal Slug 5", "Composed by: Toshikazu Tanaka","MS5"));
        modelList.add(new ItemMainModel(7,R.drawable.cover6 , "Metal Slug 6", "Composed by: Manabu Namiki, Mitsuhiro Kaneda","MS6"));
        modelList.add(new ItemMainModel(8,R.drawable.cover7 , "Metal Slug 7", "Composed by: Toshikazu Tanaka","MS7"));
        modelList.add(new ItemMainModel(9,R.drawable.cover3d , "Metal Slug 3D", "Composed by: Mitsuhiro Kaneda","MS3D"));*/

        txtSiteName = (Button) findViewById(R.id.txtSiteName);
        UpdateSiteName();
        adapter = new itemMainAdapter(this, modelList);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        LayoutAnimationController layoutAnimationController = null;
        layoutAnimationController = AnimationUtils.loadLayoutAnimation(this,  R.anim.layout_fall_down);
        viewPager.setLayoutAnimation(layoutAnimationController);
        viewPager.setAdapter(adapter);
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
        Button btnDownload = (Button) findViewById(R.id.btnDownload);
        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadTest();
            }
        });
        mediaPlayer = new MediaPlayer();
        prepareMediaPlayer();
        bntPlay = (Button) findViewById(R.id.bntPlay);
        bntPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = new File(Environment
                        .getExternalStorageDirectory(),"/PRUEBA2/" + ConfigurationHelper.urlType[ConfigurationHelper.indexSite].nameFile);
                if(!file.exists()){
                    Toast.makeText(MenuActivity.this, "FILE DOESN'T EXIST", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                    bntPlay.setText("PLAY");
                }else{
                    Toast.makeText(MenuActivity.this, ConfigurationHelper.urlType[ConfigurationHelper.indexSite].nameFile, Toast.LENGTH_SHORT).show();
                    mediaPlayer.start();
                    bntPlay.setText("STOP");
                }
            }
        });

        Button bntSite = (Button) findViewById(R.id.bntSite);
        bntSite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfigurationHelper.UpdateIndex();
                UpdateSiteName();
            }
        });

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

        DataHelper.getOstInformation(this);
    }

    private void prepareMediaPlayer(){
        try {
            mediaPlayer.setDataSource(Environment.getExternalStorageDirectory()+"/PRUEBA2/" + ConfigurationHelper.urlType[ConfigurationHelper.indexSite].nameFile);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void UpdateSiteName(){
        txtSiteName.setText(ConfigurationHelper.urlType[ConfigurationHelper.indexSite].nameSite);
    }

    /**
     * COPY-PASTE > just request permission for writ in the external storage of the phone
     * @return
     */
    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                ///Log.v(TAG,"Permission is granted");
            } else {
                //Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                ///Log.v(TAG,"Permission is granted");
                return true;
            } else {
                //Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            //Log.v(TAG,"Permission is granted");
            return true;
        }
    }

    private void DownloadTest(){
        new DownloadFileFromURL().execute();
    }

    public void OpenElementOst(int position, ItemMainModel itemMainModel){
        if(position == viewPager.getCurrentItem()){
            soundPool.play(soundSelected,1,1,1,0,0);
            Toast.makeText(MenuActivity.this, modelList.get(position).getTitle(), Toast.LENGTH_SHORT).show();
            Intent elementIntent = new Intent(MenuActivity.this, ElementActivity.class);
            elementIntent.putExtra("ID",itemMainModel.getId());
            elementIntent.putExtra("FOLDER", itemMainModel.getFolder());
            startActivity(elementIntent);
            overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
        }else{
            viewPager.setCurrentItem(position);
        }
    }


    public class DownloadFileFromURL extends AsyncTask<String, String, String> {
        ProgressDialog mypDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mypDialog = new ProgressDialog(MenuActivity.this);
            mypDialog.setTitle(ConfigurationHelper.urlType[ConfigurationHelper.indexSite].urlSite);
            mypDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mypDialog.setMessage("0");
            mypDialog.setCanceledOnTouchOutside(false);
            mypDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            int count;
            Log.d("CHELO","FUNCA0");
            try{
                URL url = new URL(ConfigurationHelper.urlType[ConfigurationHelper.indexSite].urlSite);
                URLConnection connection = url.openConnection();
                connection.connect();
                int lenghtOfFile = connection.getContentLength();
                mypDialog.setMessage(String.valueOf(lenghtOfFile));
                // download the file
                InputStream input = new BufferedInputStream(url.openStream(),
                        8192);
                // Output stream
                File file = new File(Environment
                                .getExternalStorageDirectory(),"/"+ UserPreferencesHelper.getNameFolderDownloads(MenuActivity.this) + "/" );
                if(!file.exists()){
                    file.mkdirs();
                }
                OutputStream output = new FileOutputStream(Environment
                        .getExternalStorageDirectory()
                        + "/"+ UserPreferencesHelper.getNameFolderDownloads(MenuActivity.this) +"/" + ConfigurationHelper.urlType[ConfigurationHelper.indexSite].nameFile);
                byte data[] = new byte[1024];
                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    mypDialog.setMessage(String.valueOf(((int) ((total * 100) / lenghtOfFile))));
                    // writing data to file
                    output.write(data, 0, count);
                }
                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();
                prepareMediaPlayer();

            }catch (Exception e){
                Log.e("Error: ", e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            // CIERRA EL DIALOG
            mypDialog.setMessage("IT'S OVER!");
            mypDialog.cancel();
        }
    }
}
