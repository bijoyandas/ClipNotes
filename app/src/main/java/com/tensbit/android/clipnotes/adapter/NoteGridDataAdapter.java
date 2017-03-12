package com.tensbit.android.clipnotes.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tensbit.android.clipnotes.R;
import com.tensbit.android.clipnotes.model.NoteData;

import java.util.ArrayList;

import static android.R.attr.data;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;

/**
 * Created by Bijoyan on 2/8/2017.
 */

public class NoteGridDataAdapter extends RecyclerView.Adapter<NoteGridDataAdapter.NoteHolder> {
    private ArrayList<NoteData> list;
    private ArrayList<NoteData> listCopy;
    private LayoutInflater inflater;
    private Context context;
    private itemClickCallback itemClickCallback;

    public NoteGridDataAdapter(Activity context, ArrayList<NoteData> notes) {
        inflater = LayoutInflater.from(context);
        this.list = notes;
        listCopy = new ArrayList<>();
        listCopy.addAll(list);
        this.context = context;
    }

    public void filter(String text) {
        list.clear();
        text = text.toLowerCase();
        for (NoteData currentData : listCopy) {
            if (currentData.getContent().toLowerCase().contains(text) || currentData.getTitle().toLowerCase().contains(text)) {
                list.add(currentData);
            }
        }
        this.notifyDataSetChanged();
    }

    @Override
    public NoteHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.griditem_layout, parent, false);
        return new NoteHolder(view);
    }

    @Override
    public void onBindViewHolder(NoteHolder holder, int position) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("Color Mode", Context.MODE_PRIVATE);
        boolean colorToggle = sharedPreferences.getBoolean("minimal", true);
        NoteData data = list.get(position);
        Uri imageUri = data.getImage();
        String title = data.getTitle();
        String content = data.getContent();
        String clipped = data.getClipped();
        String myImageCheck = imageUri.toString();

        if (!myImageCheck.equals("") && !(content == null || content.trim().equals(""))) {
            holder.thumbnail.setVisibility(View.VISIBLE);
            Glide.with(context).load(imageUri).into(holder.thumbnail);
            holder.thumbnail.setMinimumHeight(200);
            holder.content.setMaxLines(1);
            holder.content.setText(content);
            if (!colorToggle) {
                holder.content.setTextColor(Color.WHITE);
                holder.content.setBackgroundColor(ContextCompat.getColor(context, data.getColorId()));
            }
            else {
                holder.content.setTextColor(Color.BLACK);
                holder.content.setBackgroundColor(Color.WHITE);
            }
        } else if (!myImageCheck.equals("")) {
            Glide.with(context).load(imageUri).into(holder.thumbnail);
            holder.thumbnail.setVisibility(View.VISIBLE);
            holder.content.setVisibility(View.GONE);
        } else if (!(content == null || content.trim().equals(""))) {
            holder.content.setVisibility(View.VISIBLE);
            holder.thumbnail.setVisibility(View.GONE);
            holder.content.setMaxLines(6);
            holder.content.setText(content);
            if (!colorToggle) {
                holder.content.setTextColor(Color.WHITE);
                holder.content.setBackgroundColor(ContextCompat.getColor(context, data.getColorId()));
            } else {
                holder.content.setTextColor(Color.BLACK);
                holder.content.setBackgroundColor(Color.WHITE);
            }
        }

        if (title == null || title.trim().equals("")) {
            holder.title.setVisibility(View.GONE);
        } else {
            holder.title.setVisibility(View.VISIBLE);
            holder.title.setText(title);
            if (!colorToggle) {
                holder.title.setTextColor(Color.WHITE);
                holder.title.setBackgroundColor(ContextCompat.getColor(context, data.getColorId()));
            } else {
                holder.title.setTextColor(Color.BLACK);
                holder.title.setBackgroundColor(Color.WHITE);
            }
        }
        if (clipped.equals("true")) {
            holder.clip.setImageResource(R.drawable.ic_turned_in);
        } else {
            holder.clip.setImageResource(R.drawable.ic_turned_in_not);
        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class NoteHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private TextView title;
        private ImageView thumbnail;
        private TextView content;
        private ImageView clip;
        private View view;

        public NoteHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.noteTitle);
            thumbnail = (ImageView) itemView.findViewById(R.id.noteImage);
            content = (TextView) itemView.findViewById(R.id.noteContent);
            clip = (ImageView) itemView.findViewById(R.id.clip_image);
            view = itemView.findViewById(R.id.cardNote);

            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
            clip.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int id = view.getId();
            if (id == R.id.cardNote) {
                itemClickCallback.onViewClicked(view, getAdapterPosition());
            } else {
                itemClickCallback.onClipClicked(view, getAdapterPosition());
            }
        }

        @Override
        public boolean onLongClick(View view) {
            itemClickCallback.onViewLongClicked(view, getAdapterPosition());
            return true;
        }
    }

    public interface itemClickCallback {
        void onViewClicked(View view, int position);

        void onViewLongClicked(View view, int position);

        void onClipClicked(View view, int position);
    }

    public void setItemClickCallback(itemClickCallback itemClickCallback) {
        this.itemClickCallback = itemClickCallback;
    }

    public void setList(ArrayList<NoteData> arrayList) {
        this.list = arrayList;
    }
}
