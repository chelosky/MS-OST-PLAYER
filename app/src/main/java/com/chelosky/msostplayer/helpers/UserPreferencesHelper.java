package com.chelosky.msostplayer.helpers;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;

public class UserPreferencesHelper {
    public static final String USER_PREFS = "userPreferences.txt";
    public static final String SOUND_EFFECT_APP = "sound_effect";
    public static final String NAME_FOLDER_DOWNLOADS = "download_folder";
    private static final String FOLDER_DOWNLOADS = "METALSLUGOST";

    public static void initializeUserPreferences(Context context){
        try {
            if (!IOHelper.checkLocalFile(context, USER_PREFS)){
                JSONArray jsonArray = new JSONArray("[]");
                JSONObject jsonObject = new JSONObject();
                jsonObject.put(SOUND_EFFECT_APP, true);
                jsonObject.put(NAME_FOLDER_DOWNLOADS, FOLDER_DOWNLOADS);
                jsonArray.put(jsonObject);
                IOHelper.writeJson(context, USER_PREFS, jsonArray.toString());
            }
        }catch (Exception e){
            Log.d("CHELO", e.getMessage());
        }
    }

    public static void updateUserPreferences(Context context, String nameFolder, boolean soundEffect){
        try{
            String sxml = IOHelper.readFileString(context, USER_PREFS);
            JSONArray jsonArray = new JSONArray(sxml);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            jsonObject.put(SOUND_EFFECT_APP, soundEffect);
            jsonObject.put(NAME_FOLDER_DOWNLOADS, nameFolder);
            IOHelper.writeJson(context, USER_PREFS, jsonArray.toString());
            Toast.makeText(context, "PREFERENCES SAVED!", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Log.d("CHELO", e.getMessage());
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public static String getNameFolderDownloads(Context context){
        try {
            String sxml = IOHelper.readFileString(context, USER_PREFS);
            JSONArray jsonArray = new JSONArray(sxml);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            return jsonObject.getString(NAME_FOLDER_DOWNLOADS);
        }catch (Exception e){
            Log.d("CHELO", e.getMessage());
            return "";
        }
    }

    public static Boolean getSoundEffectApp(Context context){
        try {
            String sxml = IOHelper.readFileString(context, USER_PREFS);
            JSONArray jsonArray = new JSONArray(sxml);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            return jsonObject.getBoolean(SOUND_EFFECT_APP);
        }catch (Exception e){
            Log.d("CHELO", e.getMessage());
            return false;
        }
    }

    private static void deleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                deleteRecursive(child);

        fileOrDirectory.delete();
    }

    public static void deleteAllSounds(Context context){
        ProgressDialog mypDialog = new ProgressDialog(context);
        try {
            mypDialog.setTitle("Delete all Sounds");
            mypDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mypDialog.setMessage("Just wait...");
            mypDialog.setCanceledOnTouchOutside(false);
            mypDialog.show();
            String path = getNameFolderDownloads(context);
            File dir = new File(Environment.getExternalStorageDirectory() + "/"  + path);
            deleteRecursive(dir);
            Toast.makeText(context, "Sounds Removed", Toast.LENGTH_SHORT).show();
            mypDialog.dismiss();
        }catch (Exception e){
            Log.d("CHELO", e.getMessage());
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            mypDialog.dismiss();
        }
    }

}
