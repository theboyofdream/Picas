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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;


public class MainActivity extends AppCompatActivity {

    private final String[] permissions_string = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private RecyclerView folders_recyclerView, files_recyclerView;
    private GridLayoutManager folders_gridLayoutManager, files_gridLayoutManager;
    private final int maximum_column_count = 4;
    private int folders_column_count, files_column_count, current_column_count = maximum_column_count - 1;
    public Function folder_view_onClick, file_view_onClick, folder_view_onLongPress, file_view_onLongPress;
    private boolean is_view_selectable = false;
    private HashMap<String, Integer> selected_views = new HashMap<>();

    public static RecyclerView foldersView, filesView;

    public int foldersViewColumnCount = 4;
    public GridLayoutManager gridLayoutManagerForFoldersView;
    public GridLayoutManager gridLayoutManagerForFilesView;
    public int filesViewColumnCount = foldersViewColumnCount - 1;

    public HashMap<String, Integer> selectedViews = new HashMap<>();
    public boolean isSelectable = false;

    /**
     * @noinspection rawtypes
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        checkPermissions();

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int width = metrics.widthPixels;
        int itemSize = width / foldersViewColumnCount;

        foldersView = findViewById(R.id.foldersView);
        gridLayoutManagerForFoldersView = new GridLayoutManager(this, foldersViewColumnCount);
        foldersView.setLayoutManager(gridLayoutManagerForFoldersView);

        filesView = findViewById(R.id.filesView);
        gridLayoutManagerForFilesView = new GridLayoutManager(this, filesViewColumnCount);
        filesView.setLayoutManager(gridLayoutManagerForFilesView);

        HashMap<String, Set<String>> data = scanImages();

        ArrayList<String> foldersPath = new ArrayList<>(data.keySet());
        ArrayList<String> filesPath = new ArrayList<>();

        Function onFileViewClicked = index -> {
            int i = (int) index;
            if (filesPath.size() > 0 && !isSelectable) {
                // Toast.makeText(this, filesPath.get((Integer) index), Toast.LENGTH_SHORT).show( );

                Intent fullScreenFileIntent = new Intent(this, FullScreenFileView.class);
                fullScreenFileIntent.putExtra("files", filesPath);
                fullScreenFileIntent.putExtra("index", i);
                startActivity(fullScreenFileIntent);
            } else if (isSelectable) {
                selectedViews.put(filesPath.get(i), i);
//      filesView.notifyAll();
//      foldersView.notifyAll();
            }

            return null;
        };

        Function onLongClick = index -> {
            isSelectable = true;
            filesView.notifyAll();
            foldersView.notifyAll();
            return null;
        };

        Function onFolderViewClicked = index -> {
            foldersViewColumnCount = 1;
            gridLayoutManagerForFoldersView.setSpanCount(1);

            filesPath.clear();
            filesPath.addAll(Objects.requireNonNull(data.get(foldersPath.get((int) index))));

            TestAdapter filesAdapter = new TestAdapter(this, filesPath, filesPath, onFileViewClicked, onLongClick, itemSize, false, false, new ArrayList<>(), isSelectable);
            filesView.setAdapter(filesAdapter);
            filesView.setVisibility(View.VISIBLE);

            return null;
        };

        ArrayList<String> foldersCover = new ArrayList<>();
        ArrayList<Integer> filesCount = new ArrayList<>();
        for (String folderPath : foldersPath) {
            ArrayList<String> files = new ArrayList<>(Objects.requireNonNull(data.get(folderPath)));
            foldersCover.add(files.get(0));
            filesCount.add(files.size());
        }

        TestAdapter foldersAdapter = new TestAdapter(this, foldersPath, foldersCover, onFolderViewClicked, onLongClick, itemSize, true, true, filesCount, isSelectable);
        foldersView.setAdapter(foldersAdapter);
    }

    private void init() {

    }


    private enum Layout_Variant{
        TYPE_1,
        TYPE_2,
        TYPE_3
    }
    private void change_layout(@NonNull Layout_Variant layout_variant) {
        switch (layout_variant) {
            case TYPE_1: { // 4 folders 0 files
                folders_recyclerView.setVisibility((View.VISIBLE));
                files_recyclerView.setVisibility(View.GONE);
                folders_column_count = current_column_count;
                files_column_count = 1;
            }
            case TYPE_2: { // 1 folders 3 files
                folders_recyclerView.setVisibility((View.VISIBLE));
                files_recyclerView.setVisibility(View.VISIBLE);
                folders_column_count = 1;
                files_column_count = current_column_count - 1;
            }
            case TYPE_3: { // 0 folders 4 files
                folders_recyclerView.setVisibility((View.GONE));
                files_recyclerView.setVisibility(View.VISIBLE);
                folders_column_count = 1;
                files_column_count = current_column_count;
            }
        }
    }


    @Override
    public void onBackPressed() {
        int foldersViewColumnCount = gridLayoutManagerForFoldersView.getSpanCount();
        if (foldersViewColumnCount == 4) {
            super.onBackPressed();
            return;
        }
        gridLayoutManagerForFoldersView.setSpanCount(3);
        filesView.setVisibility(View.GONE);
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

        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME};
        String sortOrder = MediaStore.Images.Media.DATE_MODIFIED + " desc";

        Cursor cursor = this.getContentResolver().query(uri, projection, null, null, sortOrder);

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

