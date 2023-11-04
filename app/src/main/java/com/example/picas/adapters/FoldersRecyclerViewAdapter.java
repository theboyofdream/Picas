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
import com.example.picas.databinding.FolderViewBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

public class FoldersRecyclerViewAdapter extends RecyclerView.Adapter<FoldersRecyclerViewAdapter.ViewHolder> {
    Context context;
    HashMap<String, Set<String>> data;
    ArrayList<String> folders_path;
    HashMap<String, Function<String, Void>> functions;
    int item_size;

    public FoldersRecyclerViewAdapter(Context context, HashMap<String, Set<String>> data, HashMap<String, Function<String, Void>> functions, int item_size) {
        this.context = context;
        this.data = data;
        this.folders_path = new ArrayList<>(data.keySet());
        this.functions = functions;
        this.item_size = item_size;
    }

    @NonNull
    @Override
    public FoldersRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.folder_view, parent, false);

        return new FoldersRecyclerViewAdapter.ViewHolder(view);
    }

    @NonNull
    @Override
    public void onBindViewHolder(FoldersRecyclerViewAdapter.ViewHolder holder, int position) {
        int i = holder.getAdapterPosition();

        String folder_path = folders_path.get(i);
        ArrayList<String> files = new ArrayList<>(Objects.requireNonNull(data.get(folder_path)));

        String[] parts = folder_path.split("/");
        ArrayList<String> partsArray = new ArrayList<>(Arrays.asList(parts));
        String folder_name = partsArray.remove(partsArray.size() - 1);

        String files_count = String.valueOf(files.size());
        String folder_cover_path = files.get(0);

        holder.name.setText(folder_name);
        holder.counts.setText(files_count);

        Glide.with(context)
                .load(folder_cover_path)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .override((int) MainActivity.item_size)
                .override(item_size)
                .into(holder.cover);

//        Log.d("DEBUG", folder_name);
//        Log.d("DEBUG", files_count);
//        Log.d("DEBUG", folder_path);
//        Log.d("DEBUG", folder_cover_path);
//        holder.container.setOnClickListener(e->onFolderClick(folder_path));



        holder.container.setOnClickListener(v -> {
//            if (MainActivity.selection_on) {
//                holder.checkBox.setVisibility(View.VISIBLE);
//                if (MainActivity.selected_list.contains(folder_path)) {
//                    holder.checkBox.setChecked(true);
//                } else {
//                    holder.checkBox.setChecked(false);
//                }
//            } else {
//                holder.checkBox.setVisibility(View.GONE);
//            }
            Objects.requireNonNull(functions.get("on_folder_click")).apply(folder_path);
        });
//        holder.container.setOnLongClickListener(v -> {
//            Objects.requireNonNull(functions.get("on_long_press")).apply(folder_path);
//            return false;
//        });
    }
    @Override
    public int getItemCount() {
        return data.size();
    }
    public void setSelectedFiles(ArrayList<String> folders_path){
        this.folders_path = folders_path;
        notifyDataSetChanged();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView container;
        TextView name;
        TextView counts;
        ImageView cover;
        CheckBox checkBox;

        ViewHolder(View view) {
            super(view);
            FolderViewBinding binding = FolderViewBinding.bind(view);
            container = binding.squareViewCard;
            name = binding.squareViewText;
            counts = binding.squareViewFileCount;
            cover = binding.squareImageView;
            checkBox = binding.squareViewCheckBox;
        }
    }
}
