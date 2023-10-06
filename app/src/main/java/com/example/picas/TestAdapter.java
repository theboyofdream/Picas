package com.example.picas;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.function.Function;

public class TestAdapter extends RecyclerView.Adapter<TestAdapter.TestViewHolder> {

  ArrayList<String> list;
  Context context;
//  View.OnClickListener itemClickListener;
  Function onItemClick;
  float itemSize;

//  TestAdapter(ArrayList<String> list, Context context, View.OnClickListener itemClickListener){
  TestAdapter(ArrayList<String> list, Context context, Function onItemClick, float itemSize){
    this.list = list;
    this.context = context;
    this.onItemClick = onItemClick;
    this.itemSize = itemSize;
//    this.itemClickListener = itemClickListener;
  }

  @NonNull
  @Override
  public TestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    View view = inflater.inflate(R.layout.square_view,parent,false);
    TestViewHolder viewHolder = new TestViewHolder(view);

//    int width = parent.getMeasuredWidth() / 3;
//    viewHolder.card.setMinimumWidth(width);

    return viewHolder;
  }

  @Override
  public void onBindViewHolder(@NonNull TestViewHolder holder, int position) {
    holder.text.setText(list.get(position));
    holder.card.setMinimumWidth((int)itemSize);
//    holder.card.setOnClickListener(new View.OnClickListener( ) {
//      @Override
//      public void onClick(View v) {
//        Toast.makeText(context,  list.get(position),Toast.LENGTH_SHORT).show();
//      }
//    });

    holder.card.setOnClickListener(new View.OnClickListener( ) {
      @Override
      public void onClick(View v) {
//        TextView t = v.findViewById(R.id.square_view_text);
        Log.d("DEBUG", v.toString());
        onItemClick.apply(position);
//        Log.d("DEBUG",v.toString() );
      }
    });
  }

  @Override
  public int getItemCount() {
    return list.size();
  }

  class TestViewHolder extends RecyclerView.ViewHolder{
    CardView card;
    TextView text;
    TestViewHolder(View view){
      super(view);
      this.card = view.findViewById(R.id.square_view_card);
      this.text = view.findViewById(R.id.square_view_text);
    }
  }

}
