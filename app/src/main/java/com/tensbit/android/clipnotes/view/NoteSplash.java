package com.tensbit.android.clipnotes.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.tensbit.android.clipnotes.R;
import com.tensbit.android.clipnotes.view.MainActivity;

public class NoteSplash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_splash);

        Thread myThread = new Thread(){
            public void run()
            {
                try {
                    sleep(500);
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        myThread.start();
    }
}
