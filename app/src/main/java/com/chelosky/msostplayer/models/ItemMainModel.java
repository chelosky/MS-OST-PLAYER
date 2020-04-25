package com.chelosky.msostplayer.models;

public class ItemMainModel {
    private int id;
    private String image;
    private String title;
    private String description;
    private String folder;

    public ItemMainModel(int id, String image, String title, String description, String folder) {
        this.id = id;
        this.image = image;
        this.title = title;
        this.description = description;
        this.folder = folder;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
