package com.example.picas.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.picas.R;

import java.util.ArrayList;

public class FullScreenFilePageViewerAdapter extends RecyclerView.Adapter<FullScreenFilePageViewerAdapter.ViewHolder> {
    private final ArrayList<String> files;
    private final Context context;

    public FullScreenFilePageViewerAdapter(Context context, ArrayList<String> files) {
        this.context = context;
        this.files = files;
    }

    @NonNull
    @Override
    public FullScreenFilePageViewerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext( ));
        View view = inflater.inflate(R.layout.fullscreen_file_view, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FullScreenFilePageViewerAdapter.ViewHolder holder, int position) {
        int i = holder.getAdapterPosition();

        Glide.with(context)
                .load(files.get(i))
//                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .override((int) MainActivity.item_size)
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return files.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;

        public ViewHolder(View view) {
            super(view);
            this.image = view.findViewById(R.id.fullscreen_file_view);
        }
    }
}
