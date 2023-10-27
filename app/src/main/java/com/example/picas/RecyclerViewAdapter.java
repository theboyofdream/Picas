package com.example.picas;


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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Function;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    Context context;
    ArrayList<String> paths;
    ArrayList<Integer> counts;
    ArrayList<String> covers;
    Function onItemClick;
    Function onLongClick;
    boolean showName;
    boolean showCount;
//    boolean isSelectable;
    float itemSize;

    RecyclerViewAdapter(Context context,
                ArrayList<String> paths,
                ArrayList<String> covers,
                Function onItemClick,
                Function onLongClick,
                float itemSize,
                boolean showName,
                boolean showCount,
                ArrayList<Integer> counts,
                boolean isSelectable) {
        this.paths = paths;
        this.context = context;
        this.covers = covers;
        this.onItemClick = onItemClick;
        this.onLongClick = onLongClick;
        this.itemSize = itemSize;
        this.showName = showName;
        this.showCount = showCount;
        this.counts = counts;
//        this.isSelectable = isSelectable;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext( ));
        View view = inflater.inflate(R.layout.square_view, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//    int index = position;
        int index = holder.getAdapterPosition( );
//    holder.card.setMinimumWidth((int) itemSize);

        if (showName) {
            String name = paths.get(index);
            String[] parts = name.split("/");
            ArrayList<String> partsArray = new ArrayList<>(Arrays.asList(parts));
            name = partsArray.remove(partsArray.size( ) - 1);
            holder.text.setText(name);
        } else {
            holder.text.setVisibility(View.GONE);
        }

        if (showCount) {
            holder.count.setText(String.valueOf(counts.get(index)));
        } else {
            holder.count.setVisibility(View.GONE);
        }

//        if(isSelectable){
////      holder.checkBox.setChecked(true);
//            holder.checkBox.setVisibility(View.VISIBLE);
//        }else{
////      holder.checkBox.setChecked(false);
//            holder.checkBox.setVisibility(View.GONE);
//        }

        //    File image = new File(covers.get(index));
        Glide.with(context)
                .load(covers.get(index))
                .centerCrop( )
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .override((int) itemSize)
                .error(R.drawable.ic_launcher_background)
                .into(holder.image);

        holder.card.setOnClickListener(e -> onItemClick.apply(index));
//    holder.card.setOnLongClickListener(e -> {
//      onLongClick.apply(index);
//      return true;
//    });
    }

    @Override
    public int getItemCount() {
        return paths.size( );
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView card;
        TextView text;
        TextView count;
        ImageView image;
        CheckBox checkBox;

        ViewHolder(View view) {
            super(view);
            this.card = view.findViewById(R.id.square_view_card);
            this.text = view.findViewById(R.id.square_view_text);
            this.image = view.findViewById(R.id.square_image_view);
            this.count = view.findViewById(R.id.square_view_file_count);
            this.checkBox = view.findViewById(R.id.square_view_checkBox);
        }
    }

}

