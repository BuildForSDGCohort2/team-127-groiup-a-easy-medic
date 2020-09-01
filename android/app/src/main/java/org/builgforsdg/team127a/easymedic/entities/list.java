package org.builgforsdg.team127a.easymedic.entities;

public class list {


    private String name;

    private String message;

    private int imageId;

    public list(String name, String message , int imageId) {
        this.name = name;
        this.message = message;
        this.imageId = imageId;
    }

    public int getImageId() {
        return imageId;
    }

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }

}
