package com.chelosky.msostplayer.helpers;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.chelosky.msostplayer.adapter.ItemSoundAdapter;
import com.chelosky.msostplayer.models.SoundModel;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class DownloaderHelper {


    private static DownloaderHelper instance;

    private Context mcontext;

    public static DownloaderHelper getInstance(Context context){
        if(instance == null){
            instance = new DownloaderHelper(context);
        }else{
            instance.SetContext(context);
        }
        return instance;
    }

    public void SetContext(Context context){
        mcontext = context;
    }

    public DownloaderHelper(Context mcontext) {
        this.mcontext = mcontext;
    }

    public void DownloadMusic(ItemSoundAdapter.SoundHolder soundHolder, String path, SoundModel sndModel){
        DownloadTest(soundHolder,path,sndModel);
    }

    private void DownloadTest(ItemSoundAdapter.SoundHolder soundHolder, String path, SoundModel sndModel){
        DownloadFileFromURL downloaderHelper = new DownloadFileFromURL();
        downloaderHelper.pathFileExt = path;
        downloaderHelper.soundHolder = soundHolder;
        downloaderHelper.soundModel = sndModel;
        downloaderHelper.execute();
    }

    public class DownloadFileFromURL extends AsyncTask<String, String, String> {
        ProgressDialog mypDialog;
        public String pathFileExt;
        public SoundModel soundModel;
        public ItemSoundAdapter.SoundHolder soundHolder;
        private boolean downloadSuccess;
        private String errorString = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mypDialog = new ProgressDialog(mcontext);
            mypDialog.setTitle(soundModel.getName());
            mypDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mypDialog.setMessage("Starting Download");
            mypDialog.setCanceledOnTouchOutside(false);
            mypDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            int count;
            downloadSuccess = true;
            try{
                URL url = new URL(soundModel.getUrl());
                URLConnection connection = url.openConnection();
                connection.connect();
                int lenghtOfFile = connection.getContentLength();
                mypDialog.setMessage("0%");
                // download the file
                InputStream input = new BufferedInputStream(url.openStream(),
                        8192);
                OutputStream output = new FileOutputStream(Environment
                        .getExternalStorageDirectory()
                        + "/"+ pathFileExt);
                byte data[] = new byte[1024];
                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    mypDialog.setMessage(String.valueOf(((int) ((total * 100) / lenghtOfFile))) + "%");
                    // writing data to file
                    output.write(data, 0, count);
                }
                // flushing output
                output.flush();
                // closing streams
                output.close();
                input.close();
            }catch (Exception e){
                e.printStackTrace();
                Log.e("Error: ", e.getMessage());
                errorString = e.getMessage();
                downloadSuccess = false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(downloadSuccess){
                soundHolder.UpdateIconDownload(true);
            }else{
                Toast.makeText(mcontext, errorString, Toast.LENGTH_SHORT).show();
            }
            mypDialog.cancel();
        }
    }
}
