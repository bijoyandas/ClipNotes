package com.tensbit.android.clipnotes.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static android.os.Build.VERSION_CODES.N;
import static android.provider.Telephony.Carriers.PASSWORD;

/**
 * Created by Bijoyan on 2/7/2017.
 */

public class NoteDataAdapter {
    public static final String NOTEID = "_id";
    public static final String NOTETITLE = "Title";
    public static final String NOTECONTENT = "Content";
    public static final String NOTECLIPPED = "Clipped";
    public static final String NOTEIMAGE = "Image";
    public static final String NOTEARCHIVED = "Archive";
    public static final String NOTETRASH = "Trash";
    NoteDatahelper noteDatahelper;
    SQLiteDatabase db;
    public void insertNote(String title, String content, Uri imageUri, String clipped, String archived, String trashed)
    {

        ContentValues contentValues = new ContentValues();
        contentValues.put(NoteDatahelper.NOTETITLE,title);
        contentValues.put(NoteDatahelper.NOTECONTENT,content);
        contentValues.put(NoteDatahelper.NOTEIMAGE,imageUri.toString());
        contentValues.put(NoteDatahelper.NOTECLIPPED,clipped);
        contentValues.put(NoteDatahelper.NOTEARCHIVED,archived);
        contentValues.put(NoteDatahelper.NOTETRASH,trashed);

        db = noteDatahelper.getWritableDatabase();
        db.insert(NoteDatahelper.TABLE_NAME,null,contentValues);
        db.close();
    }

    public Cursor getNotes(boolean flag)
    {
        db = noteDatahelper.getWritableDatabase();
        Cursor c;
        if (flag)
            c = db.query(NoteDatahelper.TABLE_NAME,new String[]{NoteDatahelper.NOTEID,NoteDatahelper.NOTETITLE,NoteDatahelper.NOTECONTENT,
                                NoteDatahelper.NOTEIMAGE,NoteDatahelper.NOTECLIPPED,NoteDatahelper.NOTEARCHIVED,NoteDatahelper.NOTETRASH},NoteDatahelper.NOTECLIPPED+" = ? and "
                                + NoteDatahelper.NOTEARCHIVED +" = ? and "+NoteDatahelper.NOTETRASH+" = ?", new String[]{"true","false","false"},null,null,null);
        else
            c = db.query(NoteDatahelper.TABLE_NAME,new String[]{NoteDatahelper.NOTEID,NoteDatahelper.NOTETITLE,NoteDatahelper.NOTECONTENT,
                    NoteDatahelper.NOTEIMAGE,NoteDatahelper.NOTECLIPPED,NoteDatahelper.NOTEARCHIVED,NoteDatahelper.NOTETRASH},NoteDatahelper.NOTECLIPPED+" = ? and "
                    + NoteDatahelper.NOTEARCHIVED +" = ? and "+NoteDatahelper.NOTETRASH+" = ?", new String[]{"false","false","false"},null,null,null);
        return c;
    }

    public Cursor getSingleNote(int id)
    {
        Cursor c;
        db = noteDatahelper.getWritableDatabase();
        c = db.query(noteDatahelper.TABLE_NAME,new String[]{NoteDatahelper.NOTETITLE,NoteDatahelper.NOTECONTENT,
                NoteDatahelper.NOTEIMAGE,NoteDatahelper.NOTECLIPPED,NoteDatahelper.NOTEARCHIVED,NoteDataAdapter.NOTETRASH},
                NoteDatahelper.NOTEID + " = ?",new String[]{id+""},null,null,null);
        return c;
    }

    public void updateNote(String title, String content, Uri imageUri, String clipped, String archived, int id, String trashed)
    {

        ContentValues contentValues = new ContentValues();
        contentValues.put(NoteDatahelper.NOTETITLE,title);
        contentValues.put(NoteDatahelper.NOTECONTENT,content);
        contentValues.put(NoteDatahelper.NOTEIMAGE,imageUri.toString());
        contentValues.put(NoteDatahelper.NOTECLIPPED,clipped);
        contentValues.put(NoteDatahelper.NOTEARCHIVED,archived);
        contentValues.put(NoteDatahelper.NOTETRASH,trashed);

        db = noteDatahelper.getWritableDatabase();
        db.update(NoteDatahelper.TABLE_NAME,contentValues,NoteDatahelper.NOTEID+" = ?",new String[]{id+""});
        db.close();
    }

    public void updateClip(int id, String clip)
    {
        ContentValues values = new ContentValues();
        values.put(NoteDatahelper.NOTECLIPPED,clip);
        db = noteDatahelper.getWritableDatabase();
        db.update(NoteDatahelper.TABLE_NAME,values,NoteDatahelper.NOTEID+" = ?",new String[]{id+""});
        db.close();
    }

    public void updateArchive(int id)
    {
        ContentValues values = new ContentValues();
        values.put(NoteDatahelper.NOTEARCHIVED,"true");
        values.put(NoteDatahelper.NOTETRASH,"false");
        db = noteDatahelper.getWritableDatabase();
        db.update(NoteDatahelper.TABLE_NAME,values,NoteDatahelper.NOTEID+" = ?",new String[]{id+""});
        db.close();
    }

    public void deleteNote(int id)
    {
        db = noteDatahelper.getWritableDatabase();
        db.delete(NoteDatahelper.TABLE_NAME,NoteDatahelper.NOTEID+" = ?",new String[]{id+""});
        db.close();
    }

    public void trashNote(int id)
    {
        db = noteDatahelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NoteDatahelper.NOTETRASH,"true");
        values.put(NoteDatahelper.NOTEARCHIVED,"false");
        values.put(NoteDatahelper.NOTECLIPPED,"false");
        db.update(NoteDatahelper.TABLE_NAME,values,NoteDatahelper.NOTEID+" = ?",new String[]{id+""});
        db.close();
    }

    public void updateDialogNote(int id)
    {
        db = noteDatahelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NoteDatahelper.NOTEARCHIVED,"false");
        values.put(NoteDatahelper.NOTETRASH,"false");
        db.update(NoteDatahelper.TABLE_NAME,values,NoteDatahelper.NOTEID+" = ?",new String[]{id+""});
        db.close();
    }

    public Cursor getArchivedNotes(boolean flag)
    {
        Cursor c;
        db = noteDatahelper.getWritableDatabase();
        if (flag)
            c = db.query(NoteDatahelper.TABLE_NAME,new String[]{NoteDatahelper.NOTEID,NoteDatahelper.NOTETITLE,NoteDatahelper.NOTECONTENT,
                NoteDatahelper.NOTEIMAGE,NoteDatahelper.NOTECLIPPED,NoteDatahelper.NOTEARCHIVED,NoteDatahelper.NOTETRASH},
                NoteDatahelper.NOTEARCHIVED +" = ? and "+NoteDatahelper.NOTETRASH+" = ? and "+NoteDatahelper.NOTECLIPPED+" = ?", new String[]{"true","false","true"},null,null,null);
        else
            c = db.query(NoteDatahelper.TABLE_NAME,new String[]{NoteDatahelper.NOTEID,NoteDatahelper.NOTETITLE,NoteDatahelper.NOTECONTENT,
                    NoteDatahelper.NOTEIMAGE,NoteDatahelper.NOTECLIPPED,NoteDatahelper.NOTEARCHIVED,NoteDatahelper.NOTETRASH},
                    NoteDatahelper.NOTEARCHIVED +" = ? and "+NoteDatahelper.NOTETRASH+" = ? and "+NoteDatahelper.NOTECLIPPED+" = ?", new String[]{"true","false","false"},null,null,null);
        return c;
    }

    public Cursor getTrashNotes()
    {
        Cursor c;

        db = noteDatahelper.getWritableDatabase();
        c = db.query(NoteDatahelper.TABLE_NAME,new String[]{NoteDatahelper.NOTEID,NoteDatahelper.NOTETITLE,NoteDatahelper.NOTECONTENT,
                        NoteDatahelper.NOTEIMAGE,NoteDatahelper.NOTECLIPPED,NoteDatahelper.NOTEARCHIVED,NoteDatahelper.NOTETRASH},
                NoteDatahelper.NOTETRASH+" = ?", new String[]{"true"},null,null,null);
        return c;
    }


    public NoteDataAdapter(Context context)
    {
        noteDatahelper = new NoteDatahelper(context);
    }
    private class NoteDatahelper extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "notedatabase";
        private static final String TABLE_NAME = "NOTEDATA";
        private static final int DATABASE_VERSION = 1;
        private static final String NOTEID = "_id";
        private static final String NOTETITLE = "Title";
        private static final String NOTECONTENT = "Content";
        private static final String NOTECLIPPED = "Clipped";
        private static final String NOTEIMAGE = "Image";
        private static final String NOTETRASH = "Trash";
        private static final String NOTEARCHIVED = "Archive";
        private static final String CREATE_TABLE = "CREATE TABLE "+TABLE_NAME+" ("+NOTEID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                NOTETITLE+" VARCHAR, "+NOTECONTENT+" VARCHAR, "+NOTECLIPPED+" VARCHAR, "+NOTEIMAGE+" VARCHAR, " +NOTEARCHIVED+" VARCHAR, "
                +NOTETRASH+" VARCHAR);";
        private static final String DROP_TABLE = "DROP TABLE IF EXISTS "+TABLE_NAME;
        Context context;

        NoteDatahelper(Context context)
        {
            super(context,DATABASE_NAME,null,DATABASE_VERSION);
        }
        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            try {
                sqLiteDatabase.execSQL(CREATE_TABLE);
            }
            catch(SQLException e)
            {
                Log.d("sqlErr",e.toString());
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            try {
                sqLiteDatabase.execSQL(DROP_TABLE);
            }
            catch(SQLException e)
            {
                Log.d("sqlErr",e.toString());
            }
        }
    }

}
