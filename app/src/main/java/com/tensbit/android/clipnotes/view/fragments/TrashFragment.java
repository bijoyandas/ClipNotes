package com.tensbit.android.clipnotes.view.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tensbit.android.clipnotes.model.NoteData;
import com.tensbit.android.clipnotes.adapter.NoteDataAdapter;
import com.tensbit.android.clipnotes.model.NoteDataGetter;
import com.tensbit.android.clipnotes.adapter.NoteGridDataAdapter;
import com.tensbit.android.clipnotes.R;
import com.tensbit.android.clipnotes.view.NoteDetailsActivity;

import java.util.ArrayList;

import static android.view.View.GONE;

/**
 * Created by Bijoyan on 2/13/2017.
 */

public class TrashFragment extends Fragment implements View.OnLongClickListener,View.OnClickListener,NoteGridDataAdapter.itemClickCallback {
    View rootView;
    TextView noNotesView;
    RecyclerView noteGrid;
    RecyclerView.LayoutManager manager;
    NoteGridDataAdapter adapter;
    ArrayList<NoteData> list;
    private static final String EXTRA_ID = "Id";
    private static final String EXTRA_TAG = "Details";
    FloatingActionButton fab;
    View dialogView;
    AlertDialog.Builder builder;
    public TrashFragment()
    {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setLayoutManager(RecyclerView.LayoutManager manager)
    {
        this.manager = manager;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.content_main,container,false);

        noNotesView = (TextView) rootView.findViewById(R.id.noNoteText);
        noteGrid = (RecyclerView) rootView.findViewById(R.id.noteGrid);
        noNotesView.setText(R.string.no_notes_trash);

        noteGrid.setHasFixedSize(true);
        noteGrid.setItemViewCacheSize(20);
        noteGrid.setDrawingCacheEnabled(true);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(createHelperCallback());
        itemTouchHelper.attachToRecyclerView(noteGrid);

        noteGrid.setLayoutManager(manager);

        fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setVisibility(View.GONE);

        checkNoteDatabase();

        return rootView;
    }

    private void checkNoteDatabase() {
        NoteDataAdapter noteDataAdapter = new NoteDataAdapter(getActivity());
        list = (ArrayList<NoteData>) NoteDataGetter.getTashNote(getActivity());
        if (list.size()>0) {
            noNotesView.setVisibility(View.GONE);
            adapter = new NoteGridDataAdapter(getActivity(), list);
            adapter.setItemClickCallback(this);
            noteGrid.setAdapter(adapter);
        } else {
            noteGrid.setVisibility(GONE);
        }
    }

    private ItemTouchHelper.Callback createHelperCallback() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback =
                new ItemTouchHelper.SimpleCallback(0,
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

                    @Override
                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                          RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                        Snackbar.make(rootView,"Deleted permanently",Snackbar.LENGTH_SHORT).show();
                        permanentDeleteItem(viewHolder.getAdapterPosition());
                        //permanentDeleteItem(viewHolder.getAdapterPosition());
                    }
                };
        return simpleItemTouchCallback;
    }

    private void permanentDeleteItem(int position) {
        NoteData data = list.get(position);
        int id = data.getId();
        NoteDataAdapter noteDataAdapter = new NoteDataAdapter(getActivity());
        noteDataAdapter.deleteNote(id);
        list.remove(position);
        adapter.setList(list);
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();

        if(id == R.id.fab)
        {
            startActivity(new Intent(getActivity(),NoteDetailsActivity.class));
        }
    }

    @Override
    public boolean onLongClick(View view) {
        int id = view.getId();

        if (id == R.id.fab)
        {
            startActivity(new Intent(getActivity(),NoteDetailsActivity.class));
        }
        return  true;
    }

    @Override
    public void onViewClicked(View view, int position) {
        NoteData data = list.get(position);

        Intent i = new Intent(getActivity(), NoteDetailsActivity.class);

        i.putExtra(EXTRA_ID, data.getId());
        i.putExtra(EXTRA_TAG, true);
        i.putExtra("fragTag","trash");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptionsCompat options = ActivityOptionsCompat.makeScaleUpAnimation(view, 0, 0, view.getWidth(), view.getHeight());
            ActivityCompat.startActivity(getActivity(), i, options.toBundle());
            getActivity().finish();
        } else {
            startActivity(i);
            getActivity().finish();
        }
    }

    @Override
    public void onViewLongClicked(View view, final int position) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Color Mode", Context.MODE_PRIVATE);
        boolean colorToggle = sharedPreferences.getBoolean("minimal",true);
        dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.delete_dialog, (ViewGroup) view,false);
        final NoteData dialogData = list.get(position);
        int colorId = dialogData.getColorId();
        builder = new AlertDialog.Builder(getActivity());
        TextView noteIt = (TextView) dialogView.findViewById(R.id.dialogArchive);
        TextView deleteIt = (TextView) dialogView.findViewById(R.id.dialogTrash);
        ImageView dialogIcon1 = (ImageView) dialogView.findViewById(R.id.dialogIcon1);
        ImageView dialogIcon2 = (ImageView) dialogView.findViewById(R.id.dialogIcon2);
        if (!colorToggle) {
            dialogView.setBackgroundColor(ContextCompat.getColor(getActivity(), colorId));
            noteIt.setTextColor(Color.WHITE);
            deleteIt.setTextColor(Color.WHITE);
            dialogIcon1.setImageResource(R.drawable.ic_delete_forever);
            dialogIcon2.setImageResource(R.drawable.ic_note_white_24dp);
        }
        else {
            dialogView.setBackgroundColor(Color.WHITE);
            noteIt.setTextColor(Color.BLACK);
            deleteIt.setTextColor(Color.BLACK);
            dialogIcon1.setImageResource(R.drawable.ic_delete_forever_black_24dp);
            dialogIcon2.setImageResource(R.drawable.ic_note_black_24dp);

        }
        noteIt.setText(R.string.moved_to_notes);
        deleteIt.setText(R.string.delete_permanently);
        builder.setView(dialogView);
        final AlertDialog dialog = builder.create();
        noteIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                NoteDataAdapter noteDataAdapter = new NoteDataAdapter(getActivity());
                noteDataAdapter.updateDialogNote(dialogData.getId());
                list.remove(position);
                adapter.setList(list);
                adapter.notifyDataSetChanged();
                Snackbar.make(rootView,"Note revived",Snackbar.LENGTH_SHORT).show();
            }
        });

        deleteIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                NoteDataAdapter noteDataAdapter = new NoteDataAdapter(getActivity());
                noteDataAdapter.deleteNote(dialogData.getId());
                list.remove(position);
                adapter.setList(list);
                adapter.notifyDataSetChanged();
                Snackbar.make(rootView,"Deleted permanently",Snackbar.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }

    @Override
    public void onClipClicked(View view, int position) {
        NoteData data = list.get(position);
        ImageView clipView = (ImageView) view.findViewById(R.id.clip_image);
        String clip = data.getClipped();

        Snackbar snackbar;

        if (clip.equals("true")) {
            clip = "false";
            clipView.setImageResource(R.drawable.ic_turned_in_not);
            data.setClipped(clip);
            Snackbar.make(rootView.findViewById(R.id.content_main), "Clip removed!", Snackbar.LENGTH_LONG).show();
        } else {
            clip = "true";
            clipView.setImageResource(R.drawable.ic_turned_in);
            data.setClipped(clip);
            Snackbar.make(rootView.findViewById(R.id.content_main), "Note clipped!", Snackbar.LENGTH_LONG).show();
        }
        NoteDataAdapter noteDataAdapter = new NoteDataAdapter(getActivity());
        noteDataAdapter.updateClip(data.getId(), clip);
    }

    public NoteGridDataAdapter getAdapter()
    {
        return adapter;
    }
}
