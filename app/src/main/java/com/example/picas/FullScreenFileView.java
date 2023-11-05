package com.example.picas;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.picas.adapters.FullScreenFilePageViewerAdapter;

import java.util.ArrayList;

public class FullScreenFileView extends AppCompatActivity {

    FullScreenFilePageViewerAdapter pagerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_file_view);

        Intent parameters = getIntent();
        ArrayList<String> files = parameters.getStringArrayListExtra("files");
        int index = parameters.getIntExtra("index", 0);

//        Log.d("DEBUG 6", String.valueOf(index));
        ViewPager2 viewPager2 = findViewById(R.id.pager_view);
        pagerViewAdapter = new FullScreenFilePageViewerAdapter(this, files, index);
        viewPager2.setAdapter(pagerViewAdapter);
        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setCurrentItem(index,false);


    }
}