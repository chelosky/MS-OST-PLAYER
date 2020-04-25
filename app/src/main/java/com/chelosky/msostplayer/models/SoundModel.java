package com.chelosky.msostplayer.models;

public class SoundModel {
    private String name;
    private String information;
    private String url;
    private String nameFile;

    public SoundModel(String name, String information, String url, String nameFile) {
        this.name = name;
        this.information = information;
        this.url = url;
        this.nameFile = nameFile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getNameFile() {
        return nameFile;
    }

    public void setNameFile(String nameFile) {
        this.nameFile = nameFile;
    }
}
