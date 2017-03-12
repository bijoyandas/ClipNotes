package com.tensbit.android.clipnotes.view;

import android.app.ActivityOptions;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.tensbit.android.clipnotes.view.fragments.ArchiveFragment;
import com.tensbit.android.clipnotes.view.fragments.NoteFragment;
import com.tensbit.android.clipnotes.R;
import com.tensbit.android.clipnotes.view.fragments.TrashFragment;

import static com.tensbit.android.clipnotes.R.id.fab;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final int REQUEST_CODE = 123;
    SearchView searchView;
    Toolbar toolbar;
    NoteFragment noteFragment;
    ArchiveFragment archiveFragment;
    TrashFragment trashFragment;
    boolean isNote;
    boolean isTrash;
    boolean isArchive;
    String fragTag,loadTrash,loadArchive;
    FragmentTransaction transaction;
    boolean isLinear;
    private static final String saveBundle = "saveBundle";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Notes");
        setSupportActionBar(toolbar);


        if (savedInstanceState == null) {
            isNote = true;
            isArchive = false;
            isTrash = false;
            isLinear = false;
        }
        else {
            Bundle local = savedInstanceState.getBundle(saveBundle);
            isNote = local.getBoolean("isNote");
            isTrash = local.getBoolean("isTrash");
            isArchive = local.getBoolean("isArchive");
            isLinear = local.getBoolean("isLinear");
        }
        noteFragment = new NoteFragment();
        archiveFragment = new ArchiveFragment();
        trashFragment = new TrashFragment();
        if (!isLinear)
            setManagersGrid();
        else
            setManagersLinear();
        if (isNote) {
            getSupportFragmentManager().beginTransaction().replace(R.id.rec_holder, noteFragment).commit();
        }
        else if(isArchive) {
            getSupportFragmentManager().beginTransaction().replace(R.id.rec_holder, archiveFragment).commit();
        }
        else if (isTrash) {
            getSupportFragmentManager().beginTransaction().replace(R.id.rec_holder, trashFragment).commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                if (!searchView.isIconified()) {
                    searchView.setIconified(true);
                }
                super.onDrawerOpened(drawerView);
            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setVisibility(View.VISIBLE);
        Intent intent = getIntent();
        fragTag = intent.getStringExtra("fragTag");
        if (fragTag!=null) {
            if (fragTag.equals("archived")) {
                toolbar.setTitle("Archive");
                getSupportFragmentManager().beginTransaction().replace(R.id.rec_holder, archiveFragment).commit();
            } else if (fragTag.equals("trashed")) {
                toolbar.setTitle("Trash");
                getSupportFragmentManager().beginTransaction().replace(R.id.rec_holder, trashFragment).commit();
            } else if (fragTag.equals("note")) {
                toolbar.setTitle("Note");
                getSupportFragmentManager().beginTransaction().replace(R.id.rec_holder, noteFragment).commit();
            }
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
        } else if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView = (SearchView) item.getActionView();
        searchView.setQueryHint("Search notes...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                if(isNote) {
                    noteFragment.getAdapter().filter(query);
                }
                else if (isTrash) {
                    trashFragment.getAdapter().filter(query);
                }
                else if (isArchive)
                {
                    trashFragment.getAdapter().filter(query);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (isNote) {
                    noteFragment.getAdapter().filter(newText);
                }
                else if (isArchive) {
                    archiveFragment.getAdapter().filter(newText);
                }
                else if (isTrash) {
                    trashFragment.getAdapter().filter(newText);
                }

                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_gridView) {
            beginFragmentTransaction();
            isLinear = false;
            setManagersGrid();
            if (isNote) {
                transaction.remove(noteFragment);
                transaction.replace(R.id.rec_holder, noteFragment).commit();
            }
            else if (isArchive) {
                transaction.remove(archiveFragment);
                transaction.add(R.id.rec_holder, archiveFragment).commit();
            }
            else if (isTrash) {
                transaction.remove(trashFragment);
                transaction.add(R.id.rec_holder, trashFragment).commit();
            }
        }
        else if (id == R.id.action_listView) {
            isLinear = true;
            beginFragmentTransaction();
            setManagersLinear();
            if (isNote) {
                transaction.remove(noteFragment);
                transaction.add(R.id.rec_holder, noteFragment).commit();
            }
            else if (isArchive) {
                transaction.remove(archiveFragment);
                transaction.add(R.id.rec_holder, archiveFragment).commit();
            }
            else if (isTrash) {
                transaction.remove(trashFragment);
                transaction.add(R.id.rec_holder, trashFragment).commit();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (!isLinear)
            setManagersGrid();
        else
            setManagersLinear();
        if (id == R.id.nav_note)
        {
            beginFragmentTransaction();
            isNote = true;
            isTrash = false;
            isArchive = false;
            getSupportActionBar().setTitle("Notes");
            transaction.replace(R.id.rec_holder,noteFragment).commit();
        }
        else if (id == R.id.nav_archived)
        {
            beginFragmentTransaction();
            isArchive = true;
            isNote = false;
            isTrash = false;
            getSupportActionBar().setTitle("Archive");
            transaction.replace(R.id.rec_holder,archiveFragment).commit();
        }
        else if (id == R.id.nav_trash)
        {
            beginFragmentTransaction();
            isTrash = true;
            isNote = false;
            isArchive = false;
            getSupportActionBar().setTitle("Trash");
            transaction.replace(R.id.rec_holder,trashFragment).commit();
        }
        else if (id == R.id.nav_settings)
        {
            startActivity(new Intent(this,Settings.class));
            finish();
        }
        else if (id == R.id.nav_send)
        {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:bthesource@gmail.com")); // only email apps should handle this
            intent.putExtra(Intent.EXTRA_SUBJECT, "ClipNotes Query");
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        }
        else if (id == R.id.nav_share)
        {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.app_details));
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void beginFragmentTransaction()
    {
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out);
    }

    private void setManagersLinear()
    {
        archiveFragment.setLayoutManager(new LinearLayoutManager(this));
        noteFragment.setLayoutManager(new LinearLayoutManager(this));
        trashFragment.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setManagersGrid()
    {
        noteFragment.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        archiveFragment.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        trashFragment.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Bundle local = new Bundle();
        local.putBoolean("isNote",isNote);
        local.putBoolean("isTrash",isTrash);
        local.putBoolean("isArchive",isArchive);
        local.putBoolean("isLinear",isLinear);
        outState.putBundle(saveBundle,local);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}