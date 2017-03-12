package com.tensbit.android.clipnotes.model;

import android.content.Context;
import android.database.Cursor;

import com.tensbit.android.clipnotes.adapter.NoteDataAdapter;

import java.util.Random;

/**
 * Created by Bijoyan on 2/13/2017.
 */

public class ImageCheck {

    public static String get()
    {
        StringBuilder buffer = new StringBuilder();
        int i=1;
        while(i<=9)
        {
            buffer.append(randomSeriesForThreeCharacter());
            i++;
        }
        return  buffer.toString();
    }

    private static char randomSeriesForThreeCharacter() {
        Random r = new Random();
        return ((char) (48 + r.nextInt(47)));
    }

}
