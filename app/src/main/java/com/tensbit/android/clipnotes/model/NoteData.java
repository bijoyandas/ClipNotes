package com.tensbit.android.clipnotes.model;

import android.graphics.Bitmap;
import android.net.Uri;

/**
 * Created by Bijoyan on 2/8/2017.
 */

public class NoteData {

    private String title;
    private String content;
    private String clipped;
    private String archived;
    private Uri imageUri;
    private int id;
    private String trashed;
    private int colorId;

    public int getColorId() {
        return colorId;
    }

    public String getTrashed() {
        return trashed;
    }

    public void setTrashed(String trashed) {
        this.trashed = trashed;
    }

    public NoteData(String title, String content, String clipped, String archived, int id, Uri imageUri, String trashed)
    {
        this.title = title;
        this.content = content;

        this.clipped = clipped;
        this.archived = archived;
        this.imageUri = imageUri;
        this.id = id;
        this.trashed = trashed;
        this.colorId = Colors.getColor();
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setClipped(String clipped) {
        this.clipped = clipped;
    }

    public void setArchived(String archived) {
        this.archived = archived;
    }

    public void setImage(Uri imageUri) {
        this.imageUri = imageUri;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getClipped() {
        return clipped;
    }

    public String getArchived() {
        return archived;
    }

    public Uri getImage() {
        return imageUri;
    }

    public int getId() {
        return id;
    }
}
