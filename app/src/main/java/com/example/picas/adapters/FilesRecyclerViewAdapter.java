package com.example.picas.adapters;

import android.content.Context;
import android.util.Log;
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

public class FilesRecyclerViewAdapter extends RecyclerView.Adapter<FilesRecyclerViewAdapter.ViewHolder> {
    Context context;
    ArrayList<String> filesPath;
    ArrayList<String> selectedFiles;
    Boolean selectionOn;
    Integer itemSize;
//    HashMap<String, Function<String, Void>> functions;
    FileListener listener;

    public FilesRecyclerViewAdapter(Context context, ArrayList<String> files_path,  int item_size) {
        this.context = context;
        this.filesPath = files_path;
        this.itemSize = item_size;
//        this.functions = functions;
//        this.selectionOn = selection_on;
//        this.selectedFiles = selected_files;
        Log.d("DEBUG", "1");
    }

    @NonNull
    @Override
    public FilesRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.file_view, parent, true);
//        Log.d("DEBUG", "2");
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FilesRecyclerViewAdapter.ViewHolder holder, int position) {
        Log.d("DEBUG", "3");
        int i = holder.getAdapterPosition();
        String file_path = filesPath.get(i);
        Glide.with(context)
                .load(file_path)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .override((int) MainActivity.item_size)
                .override(itemSize)
                .into(holder.cover);

//        if (selectionOn) {
//            holder.checkBox.setVisibility(View.VISIBLE);
//        } else {
//            holder.checkBox.setVisibility(View.GONE);
//        }
        holder.checkBox.setVisibility(selectionOn?View.VISIBLE:View.INVISIBLE);
        holder.checkBox.setChecked(selectedFiles.contains(filesPath.get(i)));

        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null){
                    listener.onClick(i, file_path);
                }
            }
        });
        holder.container.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(listener!=null){
                    listener.onLongClick(i, file_path);
                }
                return false;
            }
        });
//        holder.container.setOnClickListener(v -> Objects.requireNonNull(functions.get("on_file_click")).apply(String.valueOf(i)));
//        holder.container.setOnLongClickListener(v -> {
//            Objects.requireNonNull(functions.get("on_long_press")).apply(file_path);
//            return false;
//        });
        Log.d("DEBUG", "5");
    }
    @Override
    public int getItemCount() {
        return filesPath.size();
    }

//    @SuppressLint("NotifyDataSetChanged")
    public void setSelectedFiles(ArrayList<String> selectedFiles) {
        this.selectedFiles = selectedFiles;
    }
    public void setSelectionOn(Boolean on) {
        this.selectionOn = on;
    }
    public  void setListener(FileListener listener){
        this.listener = listener;
    }
    public void update(){
        notifyDataSetChanged();
    }


    public ArrayList<String> getFiles() {
        return filesPath;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView container;
        ImageView cover;
        CheckBox checkBox;

        ViewHolder(View view) {
            super(view);

//            FileViewBinding binding = FileViewBinding.bind(view);
//            container = binding.squareViewCard;
//            cover = binding.squareImageView;
//            checkBox = binding.squareViewCheckBox;
            container = view.findViewById(R.id.square_view_card);
            cover = view.findViewById(R.id.square_image_view);
            checkBox = view.findViewById(R.id.square_view_checkBox);
//            Log.d("DEBUG","555");
        }
    }

    public interface FileListener{
        default void onClick(int position, String filePath){}
        default void onLongClick(int position, String filePath){}
    }
}

