package com.example.picas;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.picas.adapters.FilesRecyclerViewAdapter;
import com.example.picas.adapters.FoldersRecyclerViewAdapter;
import com.example.picas.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;


public class MainActivity extends AppCompatActivity {

    private final String[] permissions_string = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    public RecyclerView folders_recyclerView, files_recyclerView;
    public GridLayoutManager folders_gridLayoutManager, files_gridLayoutManager;
    public final int maximum_column_count = 4;
    public int current_column_count = maximum_column_count - 1, folders_column_count, files_column_count;
    //    public Function folder_view_onClick, file_view_onClick, folder_view_onLongPress, file_view_onLongPress;
//    public static HashMap<String, Integer> selected_views = new HashMap<>();
//    public static boolean selection_on = false;
    private ActivityMainBinding activityMainBinding;
    private HashMap<String, Set<String>> data;
    public static int item_size;
    public static boolean selection_on = false;
    public static Set<String> selected_list = new HashSet<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());

        checkPermissions();
        // init layout
        init_layout();
        MainActivity.item_size = getItemSize();


        data = scanImages();
        load_folders_in_recyclerView(data);

//        change_layout(Layout_Variant.TYPE_1);
    }

    private int getItemSize() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int width = metrics.widthPixels;

        return width / folders_column_count;
    }

    public void init_layout() {
        folders_column_count = current_column_count;
        files_column_count = 1;

        folders_recyclerView = activityMainBinding.foldersView;
        folders_gridLayoutManager = new GridLayoutManager(MainActivity.this, folders_column_count);
        folders_recyclerView.setLayoutManager(folders_gridLayoutManager);

        files_recyclerView = activityMainBinding.filesView;
        files_gridLayoutManager = new GridLayoutManager(this, files_column_count);
        files_recyclerView.setLayoutManager(files_gridLayoutManager);
//        change_layout(Layout_Variant.TYPE_1);
//        load_folders_in_recyclerView(data);
    }

    private enum Layout_Variant {
        TYPE_1,
        TYPE_2,
        TYPE_3
    }

    private void change_layout(@NonNull Layout_Variant layout_variant) {
        switch (layout_variant) {
            case TYPE_1: { // 4 folders 0 files
                folders_column_count = current_column_count;
                files_column_count = 1;
                folders_recyclerView.setVisibility(View.VISIBLE);
                files_recyclerView.setVisibility(View.GONE);
            }
            case TYPE_2: { // 1 folders 3 files
                folders_column_count = 1;
                files_column_count = current_column_count - 1;
                folders_recyclerView.setVisibility(View.VISIBLE);
                files_recyclerView.setVisibility(View.VISIBLE);
            }
            case TYPE_3: { // 0 folders 4 files
                folders_column_count = 1;
                files_column_count = current_column_count;
                folders_recyclerView.setVisibility(View.GONE);
                files_recyclerView.setVisibility(View.VISIBLE);
            }
        }
    }

    public void load_folders_in_recyclerView(HashMap<String, Set<String>> data) {
        FoldersRecyclerViewAdapter folder_adapter = new FoldersRecyclerViewAdapter(this, data, load_files_in_recyclerView);
        folders_recyclerView.setAdapter(folder_adapter);
    }

    public Function load_files_in_recyclerView = (folder_path -> {
        ArrayList<String> files_path = new ArrayList<>(Objects.requireNonNull(data.get(folder_path)));

        FilesRecyclerViewAdapter files_adapter = new FilesRecyclerViewAdapter(this, files_path);
        files_recyclerView.setAdapter(files_adapter);
    });

    public interface FunctionsInterface {
        void load_files_in_recyclerView(String folder_path);
    }

    //    public void load_files_in_recyclerView(String folder_path){
//        ArrayList<String> files_path = new ArrayList<>(Objects.requireNonNull(data.get(folder_path)));
//
//        FilesRecyclerViewAdapter files_adapter = new FilesRecyclerViewAdapter(this,files_path);
//        files_recyclerView.setAdapter(files_adapter);
//    }
    private void load_image() {
        ArrayList<String> folders_path = new ArrayList<>();
        ArrayList<String> files_path = new ArrayList<>();
    }

    protected void checkPermissions() {
        for (String permission_str : permissions_string) {
            if (ContextCompat.checkSelfPermission(this, permission_str) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{permission_str}, 100);
            }
        }
    }

    public HashMap<String, Set<String>> scanImages() {

        HashMap<String, Set<String>> _data_ = new HashMap<>();

        Uri images_uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
//        Uri videos_uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME};
        String sortOrder = MediaStore.Images.Media.DATE_MODIFIED + " desc";

        Cursor cursor = this.getContentResolver().query(images_uri, projection, null, null, sortOrder);

        if (cursor != null) {

            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);

            while (cursor.moveToNext()) {
                String filePath = cursor.getString(columnIndex);
                String[] parts = filePath.split("/");
                ArrayList<String> partsArray = new ArrayList<>(Arrays.asList(parts));
                partsArray.remove(partsArray.size() - 1);
                parts = partsArray.toArray(new String[0]);
                String folderPath = String.join("/", parts);

                Set<String> filesPath;
                if (_data_.containsKey(folderPath)) {
                    filesPath = _data_.get(folderPath);
                } else {
                    filesPath = new HashSet<>();
                }
                assert filesPath != null;
                filesPath.add(filePath);
                _data_.put(folderPath, filesPath);

            }
            cursor.close();
        }
        return _data_;
    }
}

