package com.tensbit.android.clipnotes.model;

import com.tensbit.android.clipnotes.R;

import java.util.Random;

/**
 * Created by Bijoyan on 2/9/2017.
 */

public class Colors {

    private static int[] colors ={R.color.noteColor1,R.color.noteColor2,
            R.color.noteColor3,R.color.noteColor4,R.color.noteColor5,R.color.noteColor6};

    public  static int getColor()
    {
        int color = new Random().nextInt(colors.length);
        return colors[color];
    }

}
