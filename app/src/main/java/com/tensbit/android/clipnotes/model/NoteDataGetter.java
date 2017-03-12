package com.tensbit.android.clipnotes.model;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import com.tensbit.android.clipnotes.adapter.NoteDataAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bijoyan on 2/8/2017.
 */

public class NoteDataGetter {
    public static List<NoteData> getNoteData(boolean flag, Context context)
    {
        List<NoteData> list = new ArrayList<>();

        NoteDataAdapter noteDataAdapter = new NoteDataAdapter(context);
        Cursor c = noteDataAdapter.getNotes(flag);
        while(c.moveToNext()) {
            Uri imageUri = Uri.parse(c.getString(c.getColumnIndex(NoteDataAdapter.NOTEIMAGE)));
            String title = c.getString(c.getColumnIndex(NoteDataAdapter.NOTETITLE));
            String content = c.getString(c.getColumnIndex(NoteDataAdapter.NOTECONTENT));
            int id = c.getInt(c.getColumnIndex(NoteDataAdapter.NOTEID));
            String clipped = c.getString(c.getColumnIndex(NoteDataAdapter.NOTECLIPPED));
            String archived = c.getString(c.getColumnIndex(NoteDataAdapter.NOTEARCHIVED));
            String trashed = c.getString(c.getColumnIndex(NoteDataAdapter.NOTETRASH));
            NoteData noteData = new NoteData(title,content,clipped,archived,id,imageUri,trashed);
            list.add(noteData);
        }

        return list;
    }

    public static List<NoteData> getArchivedNote(Context context,boolean flag)
    {
        List<NoteData> list = new ArrayList<>();

        NoteDataAdapter noteDataAdapter = new NoteDataAdapter(context);
        Cursor c;
        if (flag)
            c = noteDataAdapter.getArchivedNotes(true);
        else
            c = noteDataAdapter.getArchivedNotes(false);
        while(c.moveToNext()) {
            Uri imageUri = Uri.parse(c.getString(c.getColumnIndex(NoteDataAdapter.NOTEIMAGE)));
            String title = c.getString(c.getColumnIndex(NoteDataAdapter.NOTETITLE));
            String content = c.getString(c.getColumnIndex(NoteDataAdapter.NOTECONTENT));
            int id = c.getInt(c.getColumnIndex(NoteDataAdapter.NOTEID));
            String clipped = c.getString(c.getColumnIndex(NoteDataAdapter.NOTECLIPPED));
            String archived = c.getString(c.getColumnIndex(NoteDataAdapter.NOTEARCHIVED));
            String trashed = c.getString(c.getColumnIndex(NoteDataAdapter.NOTETRASH));
            NoteData noteData = new NoteData(title,content,clipped,archived,id,imageUri,trashed);
            list.add(noteData);
        }

        return list;
    }

    public static List<NoteData> getTashNote(Context context)
    {
        List<NoteData> list = new ArrayList<>();
        NoteDataAdapter noteDataAdapter = new NoteDataAdapter(context);
        Cursor c = noteDataAdapter.getTrashNotes();
        while(c.moveToNext()) {
            Uri imageUri = Uri.parse(c.getString(c.getColumnIndex(NoteDataAdapter.NOTEIMAGE)));
            String title = c.getString(c.getColumnIndex(NoteDataAdapter.NOTETITLE));
            String content = c.getString(c.getColumnIndex(NoteDataAdapter.NOTECONTENT));
            int id = c.getInt(c.getColumnIndex(NoteDataAdapter.NOTEID));
            String clipped = c.getString(c.getColumnIndex(NoteDataAdapter.NOTECLIPPED));
            String archived = c.getString(c.getColumnIndex(NoteDataAdapter.NOTEARCHIVED));
            String trashed = c.getString(c.getColumnIndex(NoteDataAdapter.NOTETRASH));
            NoteData noteData = new NoteData(title,content,clipped,archived,id,imageUri,trashed);
            list.add(noteData);
        }

        return list;
    }

}
