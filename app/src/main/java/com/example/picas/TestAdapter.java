package com.example.picas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Function;

public class TestAdapter extends RecyclerView.Adapter<TestAdapter.TestViewHolder> {

  ArrayList<String> list;
  ArrayList<String> covers;
  Context context;
  Function onItemClick;
  boolean showName;
  boolean showCount;
  ArrayList<Integer> count;
  float itemSize;

  TestAdapter(Context context, ArrayList<String> list, ArrayList<String> covers, Function onItemClick, float itemSize,boolean showName,boolean showCount, ArrayList<Integer> count) {
    this.list = list;
    this.context = context;
    this.covers = covers;
    this.onItemClick = onItemClick;
    this.itemSize = itemSize;
    this.showName = showName;
    this.showCount = showCount;
    this.count = count;
  }

  @NonNull
  @Override
  public TestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(parent.getContext( ));
    View view = inflater.inflate(R.layout.square_view, parent, false);

    return new TestViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull TestViewHolder holder, int position) {
    int index = holder.getAdapterPosition();

    if (showName) {
      String name = list.get(index);
      String[] parts = name.split("/");
      ArrayList<String> partsArray = new ArrayList<>(Arrays.asList(parts) );
      name = partsArray.remove(partsArray.size() -1 );
      holder.text.setText(name);
    }else{
      holder.text.setVisibility(View.GONE);
    }

    if(showCount){
      holder.count.setText(String.valueOf(count.get(index)));
    }else{
      holder.count.setVisibility(View.GONE);
    }

    Glide.with(context)
        .load(new File(covers.get(index)))
        .apply(RequestOptions.centerCropTransform( ))
        .into(holder.image);
    holder.card.setMinimumWidth((int) itemSize);

    holder.card.setOnClickListener(v -> onItemClick.apply(index));
  }

  @Override
  public int getItemCount() {
    return list.size( );
  }

  static class TestViewHolder extends RecyclerView.ViewHolder {
    CardView card;
    TextView text;
    TextView count;
    ImageView image;

    TestViewHolder(View view) {
      super(view);
      this.card = view.findViewById(R.id.square_view_card);
      this.text = view.findViewById(R.id.square_view_text);
      this.image = view.findViewById(R.id.square_image_view);
      this.count = view.findViewById(R.id.square_view_file_count);
    }
  }

}
