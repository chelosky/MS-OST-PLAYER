package com.chelosky.msostplayer.helpers;

import com.chelosky.msostplayer.R;

public class ConfigurationHelper {
    // OWN WORK
    // public static final String URL_TESTING = "https://fillikenesucn.com/lacarpeta/03%20Stage%201.mp3";
    //1 DRIVE DOESNT WORK
    //public static final String URL_TESTING = "https://drive.google.com/file/d/1Cq3Q2nmog0HZbpsRY39-yuKtmv5LmHY2/view";
    //2 DRIVE work
    public static final String URL_TESTING = "https://drive.google.com/u/0/uc?id=1Cq3Q2nmog0HZbpsRY39-yuKtmv5LmHY2&export=download";
    // FIRE BASE WORK
    //public static final String URL_TESTING = "https://firebasestorage.googleapis.com/v0/b/jojodb-6d417.appspot.com/o/03%20Stage%201.mp3?alt=media&token=d9f9be5d-11be-4af0-ac6c-6dd1071dedfb";
    // 1 GITHUB WORK
    //public static final String URL_TESTING = "https://raw.githubusercontent.com/soyingenieroencaminos/OSTMUSIC/master/03%20Stage%201.mp3";
    // 2 GITHUB DONTWORK
    //public static final String URL_TESTING = "https://github.com/soyingenieroencaminos/OSTMUSIC/blob/master/03%20Stage%201.mp3";


    public enum SiteUrl {
        CHELOSKY("CHELOSKY", "https://fillikenesucn.com/lacarpeta/03%20Stage%201.mp3","CHELOSKY.mp3"),
        DRIVE("DRIVE", "https://drive.google.com/u/0/uc?id=1Cq3Q2nmog0HZbpsRY39-yuKtmv5LmHY2&export=download","DRIVE.mp3"),
        FIREBASE("FIREBASE", "https://firebasestorage.googleapis.com/v0/b/jojodb-6d417.appspot.com/o/03%20Stage%201.mp3?alt=media&token=d9f9be5d-11be-4af0-ac6c-6dd1071dedfb","FIREBASE.mp3"),
        GITHUB("GITHUB", "https://raw.githubusercontent.com/soyingenieroencaminos/OSTMUSIC/master/03%20Stage%201.mp3","GITHUB.mp3");

        public String nameSite, urlSite, nameFile;

        SiteUrl(String nameSite, String urlSite, String nameFile){
            this.nameSite = nameSite;
            this.urlSite = urlSite;
            this.nameFile = nameFile;
        }
    }

    public static final SiteUrl[] urlType = new SiteUrl[]{
            SiteUrl.CHELOSKY,
            SiteUrl.DRIVE,
            SiteUrl.FIREBASE,
            SiteUrl.GITHUB
    };

    public static int indexSite = 0;

    public static void UpdateIndex(){
        indexSite++;
        if (indexSite >= urlType.length){
            indexSite = 0;
        }
    }
}
