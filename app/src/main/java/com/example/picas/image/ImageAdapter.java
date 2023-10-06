package com.example.picas.image;

import android.content.Context;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.picas.R;

import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

  private ArrayList<String> imagePaths;
  private Context context;
  private RequestOptions glideOptions = new RequestOptions( ).diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop( );

  public ImageAdapter(Context context, ArrayList<String> imagePaths) {
    this.context = context;
    this.imagePaths = imagePaths;
  }

  @NonNull
  @Override
  public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(parent.getContext( ));
    View view = inflater.inflate(R.layout.image, parent, false);
    ImageViewHolder imageViewHolder = new ImageViewHolder(view);
    return imageViewHolder;
  }

  @Override
  public void onBindViewHolder(@NonNull ImageViewHolder imageViewHolder, int position) {
    Log.d("image adapter", imagePaths.get(position));

    Uri imageUri = Uri.parse(imagePaths.get(position));

    Glide.with(context)
        .asBitmap()
        .load(imageUri)
//        .load(imagePaths.get(position))
//        .load(imageUri.toString())
        .apply(glideOptions)
        .into(imageViewHolder.imageView);
  }

  @Override
  public int getItemCount() {
    return imagePaths.size( );
  }

  class ImageViewHolder extends RecyclerView.ViewHolder {
    ImageView imageView;

    public ImageViewHolder(View viewHolder) {
      super(viewHolder);
      this.imageView = viewHolder.findViewById(R.id.imageView);
    }
  }
}

/**
 * @Override public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
 * //    ImageItem item = imageList.get(position);
 * //    String item = imagesPath.get(position);
 * //    Log.d("image path",item.getImagePath());
 * //    Glide.with(context)
 * //         .load(item.getImagePath())
 * //        .load(item)
 * //        .apply(RequestOptions.centerCropTransform())
 * //        .into(holder.imageView);
 * <p>
 * //    holder.imageView.setOnClickListener(new ImageView.OnClickListener(){
 * //        Toast.makeText(context, item, Toast.LENGTH_LONG).show();
 * //    });
 * //    Bitmap bitmap = BitmapFactory.decodeFile(item.getImagePath( ));
 * //    Bitmap bitmap = BitmapFactory.decodeFile(item);
 * //    holder.imageView.setImageBitmap(bitmap);
 * }
 */