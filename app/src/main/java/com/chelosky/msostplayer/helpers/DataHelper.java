package com.chelosky.msostplayer.helpers;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.chelosky.msostplayer.activities.MenuActivity;
import com.chelosky.msostplayer.activities.SplashActivity;
import com.chelosky.msostplayer.models.ItemMainModel;
import com.chelosky.msostplayer.models.SoundModel;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class DataHelper extends Application {
    public static final String RAW_FILE_NAME = "DBINFO.json";
    public static final String RAW_FILE_PATH = Environment.getExternalStorageDirectory() + "/METAL_SLUG_CONF/";
    public static final String FILE_DB_NAME = "DBINFO.txt";
    public static final String JSON_DATABASE = "https://raw.githubusercontent.com/soyingenieroencaminos/OSTMUSIC/master/DB-INFO.json";
    private static final String MSG_GET_INFO = "Preparing Data...";
    public static Context mContext;

    public static void getOstInformation(Context context){
        mContext = context;
        new DownloadFileFromURL().execute();
    }

    public static class DownloadFileFromURL extends AsyncTask<String, String, String> {
        ProgressDialog mypDialog;

        /**
         * Método que se encarga de mostrar el dialog de progreso de la inicialización del lector uhf
         */
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            mypDialog = new ProgressDialog(mContext);
            mypDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mypDialog.setMessage(MSG_GET_INFO);
            mypDialog.setCanceledOnTouchOutside(false);
            mypDialog.show();
            Log.d("CHELO","iniciando la wea");
        }

        @Override
        protected String doInBackground(String... strings) {
            try{
                Log.d("CHELO","descargando la wea");
                downloadJsonFile();
            }catch (Exception e){
                Log.e("Error: ", e.getMessage());
            }
            return null;
        }

        private void setInfodb() throws Exception {
            Log.d("CHELO","trasnformando a txt");
            String jsonString = transformIntoTxt();
            Log.d("CHELO","escribiendo txt");
            IOHelper.writeJson(mContext,FILE_DB_NAME, jsonString);
        }

        private String transformIntoTxt() throws Exception{
            File file = new File(RAW_FILE_PATH + RAW_FILE_NAME);
            InputStream is = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("");
            }
            reader.close();
            return sb.toString();
        }

        private void downloadJsonFile() throws Exception{
            int count;
            URL url = new URL(JSON_DATABASE);
            URLConnection connection = url.openConnection();
            connection.connect();
            // download the file
            InputStream input = new BufferedInputStream(url.openStream(), 8192);
            // Output stream
            File file = new File(RAW_FILE_PATH);
            if(!file.exists()){
                file.mkdirs();
            }
            OutputStream output = new FileOutputStream(RAW_FILE_PATH + RAW_FILE_NAME);
            byte data[] = new byte[1024];
            while ((count = input.read(data)) != -1) {
                output.write(data, 0, count);
            }
            // flushing output
            output.flush();
            // closing streams
            output.close();
            input.close();
            setInfodb();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            // CIERRA EL DIALOG
            mypDialog.cancel();
            Log.d("CHELO","cerrar la wea");
            ((SplashActivity)mContext).goToMain();
        }
    }

    public static List<ItemMainModel> getGameItems(Context context){
        try{
            List<ItemMainModel> listGames = new ArrayList<>();
            String sxml = IOHelper.readFileString(context, FILE_DB_NAME);
            JSONArray json = new JSONArray(sxml);
            for(int i=0; i< json.length(); i++){
                JSONObject game = json.getJSONObject(i);
                ItemMainModel itemMainModel = new ItemMainModel(game.getInt("id"),
                                                                game.getString("image"),
                                                                game.getString("title"),
                                                                game.getString("description"),
                                                                game.getString("folder"));
                listGames.add(itemMainModel);
            }
            return listGames;
        }catch (Exception e){
            Log.d("CHELO",e.getMessage());
            Log.d("CHELO","SE CAYO LA WEA");
            Toast.makeText(context, "ERROR", Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    public static List<SoundModel> getOSTMSG(Context context, int idMSG){
        try {
            List<SoundModel> listSounds = new ArrayList<>();
            String sxml = IOHelper.readFileString(context, FILE_DB_NAME);
            JSONArray json = new JSONArray(sxml);
            JSONObject object = new JSONObject();
            object.put("id",idMSG);
            Integer indexOST = checkIdOST(json,object);

            if(indexOST != -1){
                JSONObject jsonObject = json.getJSONObject(indexOST);
                JSONArray jsonArraySounds = jsonObject.getJSONArray("data");
                for(int i=0;i<jsonArraySounds.length();i++){
                    JSONObject soundJSON = jsonArraySounds.getJSONObject(i);
                    SoundModel soundModel = new SoundModel(soundJSON.getString("name"),
                                                           soundJSON.getString("information"),
                                                           soundJSON.getString("url"),
                                                           soundJSON.getString("nameFile"));
                    listSounds.add(soundModel);
                }
            }else{
                Toast.makeText(context, "NO HAY REGISTRO PARA ESE IDENTIFICADOR", Toast.LENGTH_SHORT).show();
            }
            return listSounds;
        }catch (Exception e){
            Toast.makeText(context, "ERROR", Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    public static Integer checkIdOST(JSONArray jsonArray,  JSONObject object){
        try {
            for (int i=0; i< jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if(jsonObject.getInt("id") == object.getInt("id")){
                    return  i;
                }
            }
            return -1;
        }catch (Exception e){
            return -1;
        }
    }
}
