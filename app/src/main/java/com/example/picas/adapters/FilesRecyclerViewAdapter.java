package com.example.picas.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.picas.R;
import com.example.picas.databinding.FileViewBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.function.Function;

public class FilesRecyclerViewAdapter extends RecyclerView.Adapter<FilesRecyclerViewAdapter.ViewHolder> {
    Context context;
    ArrayList<String> files_path;
    ArrayList<String> selectedFiles;
    Boolean selectionOn;
    Integer item_size;
    HashMap<String, Function<String, Void>> functions;


    public FilesRecyclerViewAdapter(Context context, ArrayList<String> files_path, HashMap<String, Function<String, Void>> functions, int item_size, Boolean selection_on, ArrayList<String> selected_files) {
        this.context = context;
        this.files_path = files_path;
        this.item_size = item_size;
        this.functions = functions;
        this.selectionOn = selection_on;
        this.selectedFiles = selected_files;
    }

    @NonNull
    @Override
    public FilesRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.file_view, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FilesRecyclerViewAdapter.ViewHolder holder, int position) {
        int i = holder.getAdapterPosition();
        String file_path = files_path.get(i);
        Glide.with(context)
                .load(file_path)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .override((int) MainActivity.item_size)
                .override(item_size)
                .into(holder.cover);

        if (selectionOn) {
            holder.checkBox.setVisibility(View.VISIBLE);
        } else {
            holder.checkBox.setVisibility(View.GONE);
        }

        holder.checkBox.setChecked(selectedFiles.contains(files_path.get(i)));

        holder.container.setOnClickListener(v -> Objects.requireNonNull(functions.get("on_file_click")).apply(String.valueOf(i)));
        holder.container.setOnLongClickListener(v -> {
            Objects.requireNonNull(functions.get("on_long_press")).apply(file_path);
            return false;
        });
    }
    @Override
    public int getItemCount() {
        return files_path.size();
    }

//    @SuppressLint("NotifyDataSetChanged")
    public void setSelectedFiles(ArrayList<String> selectedFiles) {
        this.selectedFiles = selectedFiles;
        notifyDataSetChanged();
    }
    public void setSelectionOn(Boolean on) {
        this.selectionOn = on;
        notifyDataSetChanged();
    }

    public ArrayList<String> getFiles() {
        return files_path;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView container;
        ImageView cover;
        CheckBox checkBox;

        ViewHolder(View view) {
            super(view);
            FileViewBinding binding = FileViewBinding.bind(view);
            container = binding.squareViewCard;
            cover = binding.squareImageView;
            checkBox = binding.squareViewCheckBox;
        }
    }
}

