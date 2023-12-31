package com.example.picas.adapters;

import android.content.Context;
import android.util.Log;
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
import com.example.picas.Listeners;
import com.example.picas.R;
import com.example.picas.databinding.FolderViewBinding;
import com.example.picas.models.FolderModal;

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
    ArrayList<FolderModal> folders = new ArrayList<>();
    ArrayList<String> selectedFolders = new ArrayList<>();
    private Boolean selectionOn = false;
    HashMap<String, Function<String, Void>> functions;
    int item_size;
    HashMap<String, Integer> selected_files_count = new HashMap<>();
    FolderListeners listeners;

    public FoldersRecyclerViewAdapter(Context context, HashMap<String, Set<String>> data, int item_size) {
        this.context = context;
        this.data = data;
        this.folders_path = new ArrayList<>(data.keySet());
//        this.functions = functions;
        this.item_size = item_size;

        for (String folder_path : folders_path) {
            folders.add(new FolderModal(folder_path));
        }
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
                .override(item_size)
                .into(holder.cover);

//        if (selectionOn) {
//            holder.checkBox.setVisibility(View.VISIBLE);
//        } else {
//            holder.checkBox.setVisibility(View.GONE);
//        }
        holder.checkBox.setVisibility(selectionOn?View.VISIBLE:View.INVISIBLE);
        holder.checkBox.setChecked(selectedFolders.contains(folder_path));

        if (selected_files_count.containsKey(folder_path)) {
            int count = selected_files_count.get(folder_path);
            if (count > 0) {
                holder.selected_files_count.setVisibility(View.VISIBLE);
                holder.selected_files_count.setText(String.valueOf(count));
            }
        } else {
            holder.selected_files_count.setVisibility(View.INVISIBLE);
            holder.selected_files_count.setText("");
        }

        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listeners!=null){
                    listeners.onClick(folder_path);
                }
            }
        });

        holder.container.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(listeners!=null){
                    listeners.onLongClick(folder_path);
                }
                return true;
            }
        });


//        holder.container.setOnClickListener(v -> Objects.requireNonNull(functions.get("on_folder_click")).apply(folder_path));
//        holder.container.setOnLongClickListener(v -> {
//            Objects.requireNonNull(functions.get("on_long_press")).apply(folder_path);
//            return false;
//        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setSelectedFolders(ArrayList<String> paths) {
        selectedFolders = paths;
    }
    public void setSelectedFileCount(HashMap<String, Integer> selected_files_count) {
        this.selected_files_count = selected_files_count;
    }
    public void setSelectionOn(Boolean on) {
        this.selectionOn = on;
    }
    public void setListeners(FolderListeners listeners){
        this.listeners = listeners;
    }
    public void update(){
        notifyDataSetChanged();
    }

    public interface FolderListeners{
        default  void onClick(String folderPath){}
        default  void onLongClick(String folderPath){}
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView container;
        TextView name;
        TextView counts;
        ImageView cover;
        CheckBox checkBox;
        TextView selected_files_count;

        ViewHolder(View view) {
            super(view);
            FolderViewBinding binding = FolderViewBinding.bind(view);
            container = binding.squareViewCard;
            name = binding.squareViewText;
            counts = binding.squareViewFileCount;
            cover = binding.squareImageView;
            checkBox = binding.squareViewCheckBox;
            selected_files_count = binding.selectedFileCountsTextView;
        }
    }
}
