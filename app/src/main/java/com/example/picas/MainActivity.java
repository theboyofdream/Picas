package com.example.picas;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.MutableLiveData;
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
import java.util.Observable;
import java.util.Set;
import java.util.function.Function;


public class MainActivity extends AppCompatActivity {

    private final String[] permissions_string = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    public RecyclerView folders_recyclerView, files_recyclerView;
    public GridLayoutManager folders_gridLayoutManager, files_gridLayoutManager;
    public final int maximum_column_count = 4;
    public int current_column_count = maximum_column_count - 1, folders_column_count = current_column_count, files_column_count = 1;
    private ActivityMainBinding activityMainBinding;
    private HashMap<String, Set<String>> data;
//    public static int item_size;
    public static MutableLiveData item_size = new MutableLiveData<Integer>(100);
    public static boolean selection_on = false;
    public static Set<String> selected_list = new HashSet<>();
    public HashMap<String, Function<String, Void>> folder_adapter_functions = new HashMap<>();
    //    public Set<Function> folder_adapter_functions = new HashSet<>();
    public HashMap<String, Function<String, Void>> files_adapter_functions = new HashMap<>();
    private String current_layout_variant = "TYPE 1";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());

        checkPermissions();
        // recycler view
        folders_recyclerView = activityMainBinding.foldersView;
        folders_gridLayoutManager = new GridLayoutManager(MainActivity.this, folders_column_count);
        folders_recyclerView.setLayoutManager(folders_gridLayoutManager);

        files_recyclerView = activityMainBinding.filesView;
        files_gridLayoutManager = new GridLayoutManager(this, files_column_count);
        files_recyclerView.setLayoutManager(files_gridLayoutManager);

        folder_adapter_functions.put("on_folder_click", folder_path -> {
//            Log.d("DEBUG: on long press", String.valueOf(selection_on));
            if (current_layout_variant.equals("TYPE 1")) {
                if (selection_on) {
                    if (selected_list.contains(folder_path)) {
                        selected_list.remove(folder_path);
                    } else {
                        selected_list.add(folder_path);
                    }
                    return null;
                }

                ArrayList<String> folders_path = new ArrayList<>(data.keySet());
                int folder_path_position = folders_path.indexOf(folder_path);
                if (folder_path_position >= 0) {
                    folders_recyclerView.scrollToPosition(folder_path_position);
                }
                change_layout("TYPE 2");
            }
            load_files_in_recyclerView(folder_path);

            return null;
        });
        folder_adapter_functions.put("on_long_press", folder_path -> {
//            Log.d("DEBUG: on long press", String.valueOf(selection_on));
//            Log.d("DEBUG: on long press", String.valueOf(selection_on));
            if (current_layout_variant.equals("TYPE 1")) {
                if (!selection_on) {
                    MainActivity.selection_on = true;
                    MainActivity.selected_list.add(folder_path);
//                    folders_recyclerView.notifyAll();
                }
            }
            return null;
        });

        files_adapter_functions.put("on_file_click", files_path->{
            Intent intent = new Intent();
//            startActivity();
            return null;
        });



        update_item_size();

        data = scan_images();
        load_folders_in_recyclerView(data);
    }

    private void update_item_size() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int width = metrics.widthPixels;

//        MainActivity.item_size = width / folders_column_count;
        MainActivity.item_size.setValue(width / folders_column_count);
        MainActivity.item_size.notifyAll();

    }

    private void change_layout(String layout_variant) {
        switch (layout_variant) {
            case "TYPE 1": {
                folders_column_count = current_column_count;
                files_column_count = 1;
                folders_gridLayoutManager.setSpanCount(folders_column_count);
                folders_recyclerView.setVisibility(View.VISIBLE);
                files_gridLayoutManager.setSpanCount(files_column_count);
                files_recyclerView.setVisibility(View.GONE);
                current_layout_variant = "TYPE 1";
                break;
            }
            case "TYPE 2": {
                if (folders_column_count != 1) {
                    folders_column_count = 1;
                    files_column_count = current_column_count - 1;
                    folders_gridLayoutManager.setSpanCount(folders_column_count);
                    folders_recyclerView.setVisibility(View.VISIBLE);
                    files_gridLayoutManager.setSpanCount(files_column_count);
                    files_recyclerView.setVisibility(View.VISIBLE);
                }
                current_layout_variant = "TYPE 2";
                break;
            }
        }
    }

    private void load_folders_in_recyclerView(HashMap<String, Set<String>> data) {
        FoldersRecyclerViewAdapter folder_adapter = new FoldersRecyclerViewAdapter(this, data, folder_adapter_functions);
        folders_recyclerView.setAdapter(folder_adapter);
    }

    private void load_files_in_recyclerView(String folder_path) {
        ArrayList<String> files_path = new ArrayList<>(Objects.requireNonNull(data.get(folder_path)));

        FilesRecyclerViewAdapter files_adapter = new FilesRecyclerViewAdapter(this, files_path);
        files_recyclerView.setAdapter(files_adapter);
    }

//    private void load_image() {
//        ArrayList<String> folders_path = new ArrayList<>();
//        ArrayList<String> files_path = new ArrayList<>();
//    }

    protected void checkPermissions() {
        for (String permission_str : permissions_string) {
            if (ContextCompat.checkSelfPermission(this, permission_str) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{permission_str}, 100);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (selection_on) {
            MainActivity.selection_on = false;
            MainActivity.selected_list.clear();
            return;
        }
        switch (current_layout_variant) {
            case "TYPE 1": {
                super.onBackPressed();
                break;
            }
            case "TYPE 2": {
                change_layout("TYPE 1");
                break;
            }
        }
    }

    public HashMap<String, Set<String>> scan_images() {

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