package com.example.picas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.util.ArrayList;

public class FullScreenFileView extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_full_screen_file_view);

    Intent parameters = getIntent();
    ArrayList<String> files = parameters.getStringArrayListExtra("files");
    int index = parameters.getIntExtra("index",0);

    ImageView fullScreenView = findViewById(R.id.fullScreenItemView);
    Glide.with(this)
        .load(new File(files.get(index)))
        .apply(RequestOptions.centerInsideTransform())
        .into(fullScreenView);

  }
}