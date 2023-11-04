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
import com.example.picas.MainActivity;
import com.example.picas.R;
import com.example.picas.databinding.FileViewBinding;

import java.util.ArrayList;

public class FilesRecyclerViewAdapter extends RecyclerView.Adapter<FilesRecyclerViewAdapter.ViewHolder> {
    Context context;
    ArrayList<String> files_path;
    int item_size;

    public FilesRecyclerViewAdapter(Context context, ArrayList<String> files_path,int item_size){
        this.context = context;
        this.files_path = files_path;
        this.item_size = item_size;
    }

    @NonNull
    @Override
    public FilesRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext( ));
        View view = inflater.inflate(R.layout.file_view, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FilesRecyclerViewAdapter.ViewHolder holder, int position) {
        int i = holder.getAdapterPosition();

        Glide.with(context)
                .load(files_path.get(i))
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .override((int) MainActivity.item_size)
                .override(item_size)
                .into(holder.cover);
    }

    @Override
    public int getItemCount() {
        return files_path.size();
    }
    public void setSelectedFiles(ArrayList<String> files_path){
        this.files_path = files_path;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        CardView container;
        ImageView cover;
        CheckBox checkBox;

        ViewHolder(View view){
            super(view);
            FileViewBinding binding = FileViewBinding.bind(view);
            container = binding.squareViewCard;
            cover = binding.squareImageView;
            checkBox = binding.squareViewCheckBox;
        }
    }
}

