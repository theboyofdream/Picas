package com.example.picas.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.picas.MainActivity;
import com.example.picas.R;
import com.example.picas.databinding.SquareViewBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
import java.util.Set;

import kotlin.Function;

public class FoldersRecyclerViewAdapter extends RecyclerView.Adapter<FoldersRecyclerViewAdapter.ViewHolder> {
    Context context;
    HashMap<String, Set<String>> data;
    ArrayList<String> folders_path;
//    HashMap<String, Function> functions;
    Function<String> onFolderClick;

    public FoldersRecyclerViewAdapter(Context context, HashMap<String, Set<String>> data, Function<String> onFolderClick){
        this.context = context;
        this.data = data;
        this.folders_path = new ArrayList<>(data.keySet());
        this.onFolderClick=onFolderClick;
    }
    @NonNull
    @Override
    public FoldersRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext( ));
        View view = inflater.inflate(R.layout.square_view, parent, false);

        return new FoldersRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoldersRecyclerViewAdapter.ViewHolder holder, int position) {
        int i = holder.getAdapterPosition();

        String folder_path = folders_path.get(i);
        ArrayList<String> files = new ArrayList<>(Objects.requireNonNull(data.get(folder_path)));

        String[] parts = folder_path.split("/");
        ArrayList<String> partsArray = new ArrayList<>(Arrays.asList(parts));
        String folder_name = partsArray.remove(partsArray.size( ) - 1);

        String files_count = String.valueOf(files.size());
        String folder_cover_path = files.get(0);

        holder.name.setText(folder_name);
        holder.counts.setText(files_count);

        Glide.with(context)
                .load(folder_cover_path)
                .centerCrop( )
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .override((int) MainActivity.item_size)
                .into(holder.cover);

//        Log.d("DEBUG", folder_name);
//        Log.d("DEBUG", files_count);
//        Log.d("DEBUG", folder_path);
//        Log.d("DEBUG", folder_cover_path);
        holder.container.setOnClickListener(e->onFolderClick(folder_path));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        CardView container;
        TextView name;
        TextView counts;
        ImageView cover;
        CheckBox checkBox;

        ViewHolder(View view){
            super(view);
            SquareViewBinding binding = SquareViewBinding.bind(view);
            container = binding.squareViewCard;
            name = binding.squareViewText;
            counts = binding.squareViewFileCount;
            cover = binding.squareImageView;
            checkBox = binding.squareViewCheckBox;
        }
    }
}
