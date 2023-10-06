package com.example.picas;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Function;


public class MainActivity extends AppCompatActivity {
  public static RecyclerView folders_view, files_view;
  private final String[] permissions_string = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
  private HashMap<String, ArrayList<String>> folders;
  private HashMap<String, ArrayList<String>> files;
  private int foldersViewColumnCount = 3, filesViewColumnCount = 2;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_main);
    checkPermissions( );

    DisplayMetrics metrics = new DisplayMetrics();
    getWindowManager().getDefaultDisplay().getMetrics(metrics);

    int height = metrics.heightPixels;
    int width = metrics.widthPixels;

    folders_view = findViewById(R.id.foldersView);

    GridLayoutManager gridLayoutManagerForFoldersView = new GridLayoutManager(this, foldersViewColumnCount);
    folders_view.setLayoutManager(gridLayoutManagerForFoldersView);
    folders_view.setHasFixedSize(false);

    files_view = findViewById(R.id.filesView);
    GridLayoutManager gridLayoutManagerForFilesView = new GridLayoutManager(this, filesViewColumnCount);
    files_view.setLayoutManager(gridLayoutManagerForFilesView);
    files_view.setHasFixedSize(false);


    ArrayList<String> imagePaths = getAllImagePaths( );

//    for (String path: imagePaths) {
//      Log.d("paths", path);
//    }
//    ImageAdapter imageAdapter = new ImageAdapter(this, imagePaths);
    Function onFolderViewClicked = o -> {
      gridLayoutManagerForFoldersView.setSpanCount(1);

//      DisplayMetrics metrics = new DisplayMetrics();
//      getWindowManager().getDefaultDisplay().getMetrics(metrics);
//
//      int height = metrics.heightPixels;
//      int width = metrics.widthPixels;
//      gridLayoutManagerForFilesView.setSpanCount(2);
      files_view.setVisibility(View.VISIBLE);

      return null;
    };

    TestAdapter adapter = new TestAdapter(imagePaths, this, onFolderViewClicked, width/foldersViewColumnCount);
//    Log.d("count", String.valueOf(adapter.getItemCount( )));
//    Log.d("count", String.valueOf(imagePaths.size()));
    folders_view.setAdapter(adapter);
  }


//  public static void onFolderViewClicked(int position){
//    gridLayoutManager.setSpanCount(1);
//  }

  protected void checkPermissions() {
    for (String permission_str : permissions_string) {
      if (ContextCompat.checkSelfPermission(this, permission_str) != PackageManager.PERMISSION_GRANTED) {
        ActivityCompat.requestPermissions(this, new String[]{permission_str}, 100);
      }
    }
  }

  public ArrayList<String> getAllImagePaths() {

    ArrayList<String> imagePaths = new ArrayList<>( );

    Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    String[] projection = {MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME};
    String sortOrder = MediaStore.Images.Media.DATE_MODIFIED + " desc";

    Cursor cursor = this.getContentResolver( ).query(uri, projection, null, null, sortOrder);

    if (cursor != null) {

      int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
//      int loopCount = 0;
//      while (cursor.moveToNext( ) && loopCount < 100) {
      while (cursor.moveToNext( )) {
        String imagePath = cursor.getString(columnIndex);
        imagePaths.add(imagePath);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
          Path filePath = null;
          filePath = Paths.get(imagePath);
          Path folderPath = filePath.getParent();
          folderPath.getFileName();
//          folders.put(folderPath, imagePaths)
          Log.d("PATH", String.valueOf(folderPath));
          Log.d("PATH", folderPath.getFileName().toString());
        }
//        loopCount++;
      }
      cursor.close( );
    }
    return imagePaths;
  }
}

//  private void getFolderNamesWithImages() {
////    FolderAdapter folderAdapter;
//    RecyclerView recyclerView;
//    List<String> folderNames = new ArrayList<>();
//    Set<String> folderNameSet = new HashSet<>();
//
//    String[] projection = {MediaStore.Images.Media.DATA};
//    Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null, null, null);
//
//    if (cursor != null) {
//      int columnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
//      while (cursor.moveToNext()) {
//        String imagePath = cursor.getString(columnIndex);
//        File imageFile = new File(imagePath);
//        String parentFolder = imageFile.getParent();
//
//        if (parentFolder != null) {
//          folderNameSet.add(parentFolder);
//        }
//      }
//      cursor.close();
//    }
//
//    folderNames.addAll(folderNameSet);
//
//    // Set up the RecyclerView
////    recyclerView.setLayoutManager(new LinearLayoutManager(this));
////    folderAdapter = new FolderAdapter(this, new ArrayList<>(folderNames));
////    recyclerView.setAdapter(folderAdapter);
//  }


/**
 * private List<String> getAllImagesPath(){
 * <p>
 * List<String> imagePathList = new ArrayList<>();
 * <p>
 * Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
 * String[] projection = {MediaStore.MediaColumns.DATA,MediaStore.Images.Media.BUCKET_DISPLAY_NAME};
 * Cursor cursor = this.getContentResolver().query(uri, projection,null,null,MediaStore.Images.Media.DATE_MODIFIED + " desc");
 * <p>
 * if(cursor != null){
 * int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
 * while(cursor.moveToNext()){
 * String imagePath = cursor.getString(columnIndex);
 * imagePathList.add(imagePath);
 * //      imagePathList.add(new ImageItem(images_path));
 * Log.d("path",imagePath);
 * }
 * cursor.close();
 * }
 * <p>
 * return imagePathList;
 * }
 */
