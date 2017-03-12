package com.tensbit.android.clipnotes.view;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.tensbit.android.clipnotes.adapter.NoteDataAdapter;
import com.tensbit.android.clipnotes.R;
import com.tensbit.android.clipnotes.model.ImageCheck;

import java.io.File;

import static com.tensbit.android.clipnotes.R.id.content_text;
import static com.tensbit.android.clipnotes.R.id.noteImage;
import static com.tensbit.android.clipnotes.R.id.noteTitle;

public class NoteDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int REQUEST_CODE = 123;
    EditText titleText,contentText;
    FloatingActionButton fab;
    String isArchived;
    String isClipped;
    String isTrashed;
    ImageView noteImageView;
    boolean saved;
    boolean flag;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    RelativeLayout relativeLayout;
    private static final String EXTRA_ID = "Id";
    private static final String EXTRA_TAG = "Details";
    private static final int TAKE_PICTURES = 11;
    String content;
    File imageFile;
    Uri temp = null;
    private static final String BUNDLE_KEY = "myBundle";
    int id;
    String fragTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);
        relativeLayout = (RelativeLayout) findViewById(R.id.activity_note_details);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("New Note");
        setSupportActionBar(toolbar);

        noteImageView = (ImageView) findViewById(noteImage);
        noteImageView.setVisibility(View.GONE);
        if (savedInstanceState == null) {
            temp = Uri.parse("");
            saved = false;
            isClipped = "false";
            isArchived = "false";
            Intent intent = getIntent();
            id = intent.getIntExtra(EXTRA_ID, 0);
            flag = intent.getBooleanExtra(EXTRA_TAG, false);
            fragTag = intent.getStringExtra("fragTag");
            content = "";
        }
        else {
            Bundle myBundle = savedInstanceState.getBundle(BUNDLE_KEY);
            temp = Uri.parse(myBundle.getString("temp"));
            saved = myBundle.getBoolean("saved");
            isClipped = myBundle.getString("isClipped");
            isArchived = myBundle.getString("isArchived");
            isTrashed = myBundle.getString("isTrashed");
            id = myBundle.getInt("id");
            flag = myBundle.getBoolean("flag");
            fragTag = myBundle.getString("fragTag");
            if (!temp.toString().equals("")) {
                Glide.with(this).load(temp).into(noteImageView);
                noteImageView.setVisibility(View.VISIBLE);
            }
            content = myBundle.getString("content");
        }
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        titleText = (EditText) findViewById(R.id.title_text);
        contentText = (EditText) findViewById(R.id.content_text);
        fab = (FloatingActionButton) findViewById(R.id.doneNote);
        fab.setOnClickListener(this);


        if (flag)
        {
            NoteDataAdapter noteDataAdapter = new NoteDataAdapter(this);
            Cursor c = noteDataAdapter.getSingleNote(id);
            c.moveToNext();
            titleText.setText(c.getString(c.getColumnIndex(NoteDataAdapter.NOTETITLE)));
            contentText.setText(c.getString(c.getColumnIndex(NoteDataAdapter.NOTECONTENT)));
            isClipped = c.getString(c.getColumnIndex(NoteDataAdapter.NOTECLIPPED));
            isArchived = c.getString(c.getColumnIndex(NoteDataAdapter.NOTEARCHIVED));
            isTrashed = c.getString(c.getColumnIndex(NoteDataAdapter.NOTETRASH));
            temp = Uri.parse(c.getString(c.getColumnIndex(NoteDataAdapter.NOTEIMAGE)));
            String checkImage = temp.toString();
            if (!(checkImage.equals(""))) {
                Glide.with(this).load(temp).into(noteImageView);
                noteImageView.setVisibility(View.VISIBLE);
            }
            toolbar.setTitle("");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_note_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Snackbar snackbar;
        switch(id) {
            case R.id.attachImage:
                if (hasPermission()) {
                    dispatchTakePictureIntent();
                }
                else {
                    requestPermission();
                }
                break;
            case R.id.clip_note_menu:
                isClipped = "true";
                snackbar = Snackbar
                        .make(relativeLayout, "Note Clipped!", Snackbar.LENGTH_LONG)
                        .setAction("UNDO", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Snackbar snackbar1 = Snackbar.make(relativeLayout, "Clip removed!", Snackbar.LENGTH_SHORT);
                                isClipped = "false";
                                snackbar1.show();
                            }
                        });

                snackbar.show();
                break;
            case R.id.archive_note_menu:
                isArchived = "true";
                snackbar = Snackbar
                        .make(relativeLayout, "Note Archived!", Snackbar.LENGTH_LONG)
                        .setAction("UNDO", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Snackbar snackbar1 = Snackbar.make(relativeLayout, "Note Restored!", Snackbar.LENGTH_SHORT);
                                isArchived = "false";
                                snackbar1.show();
                            }
                        });

                snackbar.show();
                break;
            case android.R.id.home:
                if (!saved) {
                    if (!flag)
                        saveNote(true);
                    else
                        saveNote(false);
                }
                onBackPressed();
                finish();
                break;
            case R.id.shareNote:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, titleText.getText().toString()+"\n"+contentText.getText().toString());
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this,MainActivity.class);
        if (fragTag==null)
            fragTag="";
        if (fragTag.equals("archive"))
            intent.putExtra("fragTag","archived");
        else if (fragTag.equals("trash"))
            intent.putExtra("fragTag","trashed");
        else
            intent.putExtra("fragTag","note");
        if (!saved) {
            if (!flag)
                saveNote(true);
            else
                saveNote(false);
        }
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        content = contentText.getText().toString();
        if (id == R.id.doneNote)
        {
            if (!flag) {
                saveNote(true);
                saved = true;
            }
            else {
                saveNote(false);
                saved = true;
            }
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            takePictureIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
            SharedPreferences sharedPreferences = getSharedPreferences("ImageId",MODE_PRIVATE);
            imageFile = new File(Environment.getExternalStorageDirectory(),ImageCheck.get()+ sharedPreferences.getInt("imageId",0)+".png");
            temp = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", imageFile);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, temp);
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            SharedPreferences sharedPreferences = getSharedPreferences("ImageId",MODE_PRIVATE);
            int id = sharedPreferences.getInt("imageId",0);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("imageId",id+1);
            editor.apply();
            noteImageView.setVisibility(View.VISIBLE);
            Glide.with(this).load(temp).into(noteImageView);
        }
    }

    private void saveNote(boolean saveOrUpdate)
    {
        String content = contentText.getText().toString();
        if ((content.equals("") || content.trim().equals("")) && temp.toString().equals(""))
        {
            Snackbar
                    .make(relativeLayout, "Ehh! blank note?", Snackbar.LENGTH_LONG).show();
        }
        else {
            NoteDataAdapter noteDataAdapter = new NoteDataAdapter(this);
            if (saveOrUpdate) {
                noteDataAdapter.insertNote(titleText.getText().toString(), content, temp, isClipped + "", isArchived + "","false");
                Snackbar.make(relativeLayout, "Note added", Snackbar.LENGTH_LONG).show();
            }
            else {
                if (isArchived.equals("false"))
                    noteDataAdapter.updateNote(titleText.getText().toString(), content, temp, isClipped + "", isArchived + "",id,isTrashed);
                else
                    noteDataAdapter.updateNote(titleText.getText().toString(), content, temp, isClipped + "", isArchived + "",id,"false");
                Snackbar.make(relativeLayout, "Note updated", Snackbar.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Bundle saveBundle = new Bundle();
        saveBundle.putString("temp",temp.toString());
        saveBundle.putBoolean("saved",saved);
        saveBundle.putBoolean("flag",flag);
        saveBundle.putString("isArchived",isArchived);
        saveBundle.putString("isClipped",isClipped);
        saveBundle.putInt("id",id);
        saveBundle.putString("content",content);
        saveBundle.putString("isTrashed",isTrashed);
        saveBundle.putString("fragTag",fragTag);
        outState.putBundle(BUNDLE_KEY,saveBundle);

    }

    private boolean hasPermission()
    {
        int res = 0;

        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};

        for(String param : permissions)
        {
            res = ContextCompat.checkSelfPermission(this,param);
            if (!(res == PackageManager.PERMISSION_GRANTED))
                return false;
        }
        return true;
    }

    private void requestPermission()
    {
        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            ActivityCompat.requestPermissions(this,permissions,TAKE_PICTURES);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        boolean permissionAllowed = false;

        switch(requestCode)
        {
            case TAKE_PICTURES:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    permissionAllowed = true;
                break;
            default:
                permissionAllowed = false;
        }

        if (permissionAllowed) {
            noteImageView.setVisibility(View.VISIBLE);
            dispatchTakePictureIntent();
        }
        else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE))
                    Snackbar.make(relativeLayout,"Permission needed to perform task",Snackbar.LENGTH_SHORT).show();
                else
                    Snackbar.make(relativeLayout,"Please provide permission from settings",Snackbar.LENGTH_SHORT).show();
            }
        }
    }
}



