package com.tensbit.android.clipnotes.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.tensbit.android.clipnotes.R;

public class Settings extends AppCompatActivity {
    Toolbar toolbar;
    TextView colorText;
    Switch colorToggle;
    LinearLayout colorView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.action_settings);
        setSupportActionBar(toolbar);

        colorView = (LinearLayout) findViewById(R.id.colorView);
        SharedPreferences sharedPreferences = getSharedPreferences("Color Mode",MODE_PRIVATE);
        boolean colorMode = sharedPreferences.getBoolean("minimal",true);

        colorText = (TextView) findViewById(R.id.colorSorM);
        colorToggle = (Switch) findViewById(R.id.colorToggle);

        if (colorMode) {
            colorText.setText(getString(R.string.minimalistic));
            colorToggle.setChecked(false);
        }
        else {
            colorText.setText(getString(R.string.rainbow));
            colorToggle.setChecked(true);
        }

        colorToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SharedPreferences sharedPreferences = getSharedPreferences("Color Mode",MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                if (!b)
                {
                    colorText.setText(R.string.minimalistic);
                    editor.putBoolean("minimal",true).apply();
                }
                else {
                    colorText.setText(R.string.rainbow);
                    editor.putBoolean("minimal",false).apply();
                }
            }
        });

        colorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeToggle();
            }
        });

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        }

        return true;

    }

    private void changeToggle()
    {
        boolean b = colorToggle.isChecked();
        colorToggle.setChecked(!b);
        SharedPreferences sharedPreferences = getSharedPreferences("Color Mode",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (b)
        {
            colorText.setText(R.string.minimalistic);
            editor.putBoolean("minimal",true).apply();
        }
        else {
            colorText.setText(R.string.rainbow);
            editor.putBoolean("minimal",false).apply();
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }
}
