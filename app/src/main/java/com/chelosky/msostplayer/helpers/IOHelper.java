package com.chelosky.msostplayer.helpers;
import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.chelosky.msostplayer.activities.MenuActivity;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class IOHelper {

    /**
     * Función que se encarga de verificar si ya está creado el archivo de texto
     * @param context contexto asociado a la vista que llama a la función
     * @return Retorna un booleano que avisa si existe el archivo de texto
     */
    public static Boolean checkLocalFile(Context context, String nameFile) throws IOException{
        // Buscamos el archivo deseado
        try (FileInputStream fis = context.openFileInput(nameFile)){
            // Si existe retorna verdadero para que no se cree de nuevo
            if (fis != null) return true;
            // Si no existe retorna falso para crear uno
            return false;
        } catch (IOException e) {
            return false;
        }
    }

    public static Boolean checkExternalFile(String pathname){
        File file = new File(Environment
                .getExternalStorageDirectory(), "/" + pathname);
        if(!file.exists()){
            return false;
        }else{
            return true;
        }
    }

    public static void createExternalFolder(String folderPath){
        File file = new File(Environment
                .getExternalStorageDirectory(),"/"+ folderPath );
        if(!file.exists()){
            file.mkdirs();
        }
    }

    /**
     * Función que se encarga de generar un nuevo archivo de texto
     * @param context contexto asociado a la vista que llama a la función
     * @param objStr String que representa la información que habrá dentro del texto (en esta oportunidad es un array vacío
     */
    public static void writeJson(Context context, String nameFile , String objStr) throws IOException{
        // Usado para transformar de JSON a String que simula un JSON
        Gson gson = new Gson();
        // Se genera el archivo de texto por primera vez
        try (FileOutputStream outputStream = context.openFileOutput(nameFile, Context.MODE_PRIVATE)){
            // Dentro del txt sólo hay un  array vacío
            outputStream.write(gson.toJson(objStr).getBytes());
            Log.d("CHELO", "Archivo creado exitosamente!");
        }catch (Exception e){
            Log.d("CHELO", "No se pudo crear el archivo...");
        }
    }

    /**
     * Función que se encarga de leer el archivo de texto de la aplicación
     * @param context contexto asociado a la vista que llama a la función
     * @return Retorna un string con la información que existe dentro del archivo
     */
    public static String readFileString(Context context, String nameFile) throws IOException{
        // Buscamos y leemos el archivo de texto que funciona como 'base de datos'
        try (FileInputStream fis = context.openFileInput(nameFile);
             InputStreamReader isr = new InputStreamReader(fis);
             BufferedReader bufferedReader = new BufferedReader(isr)){
            StringBuilder sb = new StringBuilder();
            String line;
            // Pasamos el texto con saltos de línea a una sola línea de texto
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            // Limpiamos la línea de texto de caracteres innecesarios que se agregan con GSON
            String json = sb.toString();
            json = json.replace("\\", "");
            json = json.substring(1);
            json = json.substring(0, json.length() - 1);
            return json;
        }catch (FileNotFoundException e){
            return "FileNotFoundException";
        }catch (IOException e){
            return "IOException";
        }
    }


}
