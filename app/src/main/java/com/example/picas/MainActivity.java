package com.example.picas;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.picas.adapters.FilesRecyclerViewAdapter;
import com.example.picas.adapters.FoldersRecyclerViewAdapter;
import com.example.picas.databinding.ActivityMainBinding;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;


public class MainActivity extends AppCompatActivity implements View.OnDragListener {

  final String[] permissions_string = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
  RecyclerView folders_recyclerView, files_recyclerView;
  GridLayoutManager folders_gridLayoutManager, files_gridLayoutManager;
  final int maximum_column_count = 4;
  int current_column_count = maximum_column_count - 1, folders_column_count = current_column_count, files_column_count = 1, item_size;
  ActivityMainBinding activityMainBinding;
  HashMap<String, Set<String>> data;
  boolean selection_on = false;
  ArrayList<String> selected_list = new ArrayList<>();
  HashMap<String, Function<String, Void>> folder_adapter_functions = new HashMap<>();
  HashMap<String, Function<String, Void>> files_adapter_functions = new HashMap<>();
  String current_layout_variant = "TYPE 1";
  FoldersRecyclerViewAdapter folder_adapter;
  FilesRecyclerViewAdapter files_adapter;

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
    files_recyclerView.setOnDragListener(this);

//        folder_adapter_functions.put("on_folder_click", folder_path -> {
////            Log.d("DEBUG: on long press", String.valueOf(selection_on));
//            if (current_layout_variant.equals("TYPE 1")) {
//                if (selection_on) {
//                    if (selected_list.contains(folder_path)) {
//                        selected_list.remove(folder_path);
//                        folder_adapter.setSelectedFolders(selected_list);
//                    } else {
//                        selected_list.add(folder_path);
//                        folder_adapter.setSelectedFolders(selected_list);
//                    }
//                    return null;
//                }
//
//                ArrayList<String> folders_path = new ArrayList<>(data.keySet());
//                int folder_path_position = folders_path.indexOf(folder_path);
//                if (folder_path_position >= 0) {
//                    folders_recyclerView.scrollToPosition(folder_path_position);
//                }
//                change_layout("TYPE 2");
//            }
//            load_files_in_recyclerView(folder_path);
//
//            return null;
//        });
//        folder_adapter_functions.put("on_long_press", folder_path -> {
////            Log.d("DEBUG: on long press", String.valueOf(selection_on));
////            Log.d("DEBUG: on long press", String.valueOf(selection_on));
//            if (current_layout_variant.equals("TYPE 1")) {
//                if (!selection_on) {
//                    selected_list.clear();
//
//                    selection_on = true;
//                    selected_list.add(folder_path);
////                    folders_recyclerView.notifyAll();
//
//                    folder_adapter.setSelectionOn(true);
//                    folder_adapter.setSelectedFolders(selected_list);
//                }
//            }
//            return null;
//        });

//    files_adapter_functions.put("on_file_click", index -> {
//      Integer i = Integer.parseInt(index);
//      String file_path = files_adapter.getFiles().get(i);
//      if (current_layout_variant.equals("TYPE 2")) {
//        if (selection_on) {
//          if (selected_list.contains(file_path)) {
//            selected_list.remove(file_path);
//          } else {
//            selected_list.add(file_path);
//          }
//
//          HashMap<String, Integer> selected_files_count = new HashMap<>();
//          for (String file : selected_list) {
//            String[] parts = file.split("/");
//            ArrayList<String> partsArr = new ArrayList<>(Arrays.asList(parts));
//            partsArr.remove(partsArr.size() - 1);
//
//            String folder_path = String.join("/", partsArr);
//            if (selected_files_count.containsKey(folder_path)) {
//              selected_files_count.put(folder_path, selected_files_count.get(folder_path) + 1);
//            } else {
//              selected_files_count.put(folder_path, 1);
//            }
//          }
////                    Log.d("DEBUG", String.valueOf(selected_files_count));
//          folder_adapter.setSelectedFileCount(selected_files_count);
//          files_adapter.setSelectedFiles(selected_list);
//
//        } else {
//          // show image in Full Screen View when image is clicked.
//          Intent intent = new Intent(this, FullScreenFileView.class);
//          intent.putExtra("files", files_adapter.getFiles());
//          intent.putExtra("index", i);
//          startActivity(intent);
//        }
//      }
//      return null;
//    });
//    files_adapter_functions.put("on_long_press", file_path -> {
//      if (current_layout_variant.equals("TYPE 2")) {
//        if (!selection_on) {
//          selected_list.clear();
//
//          selection_on = true;
//          selected_list.add(file_path);
//
//          files_adapter.setSelectionOn(true);
//          files_adapter.setSelectedFiles(selected_list);
//
//          HashMap<String, Integer> selected_files_count = new HashMap<>();
//          for (String file : selected_list) {
//            String[] parts = file.split("/");
//            ArrayList<String> partsArr = new ArrayList<>(Arrays.asList(parts));
//            partsArr.remove(partsArr.size() - 1);
//
//            String folder_path = String.join("/", partsArr);
//            if (selected_files_count.containsKey(folder_path)) {
//              selected_files_count.put(folder_path, selected_files_count.get(folder_path) + 1);
//            } else {
//              selected_files_count.put(folder_path, 1);
//            }
//          }
////                    Log.d("DEBUG", String.valueOf(selected_files_count));
//          folder_adapter.setSelectedFileCount(selected_files_count);
//        }
//      }
//      return null;
//    });


    update_item_size();

    this.data = scan_images();
    load_folders_in_recyclerView(data);
  }

  private void update_item_size() {
    DisplayMetrics metrics = new DisplayMetrics();
    getWindowManager().getDefaultDisplay().getMetrics(metrics);

    int device_width = metrics.widthPixels;

    item_size = device_width / folders_column_count;
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
          files_column_count = current_column_count - folders_column_count;
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
    folder_adapter = new FoldersRecyclerViewAdapter(this, data, item_size);
    folder_adapter.setListeners(new FoldersRecyclerViewAdapter.FolderListeners() {
      @Override
      public void onClick(String folder_path) {
//        if (current_layout_variant.equals("TYPE 1")) {
//          if (selection_on) {
//            if (selected_list.contains(folder_path)) {
//              selected_list.remove(folder_path);
//            } else {
//              selected_list.add(folder_path);
//            }
//            folder_adapter.setSelectedFolders(selected_list);
//            folder_adapter.update();
//          }
//
//        } else if (current_layout_variant.equals("TYPE 2")) {
//          ArrayList<String> folders_path = new ArrayList<>(data.keySet());
//          int folder_path_position = folders_path.indexOf(folder_path);
//          if (folder_path_position >= 0) {
//            change_layout("TYPE 2");
//            folders_recyclerView.scrollToPosition(folder_path_position);
//            load_files_in_recyclerView(folder_path);
////              Log.d("DEBUG", String.valueOf(folder_path_position));
//          }
//        }
        switch (current_layout_variant) {
          case "TYPE 1": {
            if (selection_on) {
              if (selected_list.contains(folder_path)) {
                selected_list.remove(folder_path);
              } else {
                selected_list.add(folder_path);
              }
              folder_adapter.setSelectedFolders(selected_list);
              folder_adapter.update();
            } else {
              ArrayList<String> folders_path = new ArrayList<>(data.keySet());
              int folder_path_position = folders_path.indexOf(folder_path);
              if (folder_path_position >= 0) {
                folders_recyclerView.scrollToPosition(folder_path_position);
                load_files_in_recyclerView(folder_path);
                change_layout("TYPE 2");
              }
            }
            break;
          }
          case "TYPE 2": {
            ArrayList<String> folders_path = new ArrayList<>(data.keySet());
            int folder_path_position = folders_path.indexOf(folder_path);
            if (folder_path_position >= 0) {
//              folders_recyclerView.scrollToPosition(folder_path_position);
              load_files_in_recyclerView(folder_path);
            }
            break;
          }
        }
      }

      @Override
      public void onLongClick(String folder_path) {
        if (current_layout_variant.equals("TYPE 1") && !selection_on) {
          selected_list.clear();

          selection_on = true;
          selected_list.add(folder_path);

          folder_adapter.setSelectionOn(true);
          folder_adapter.setSelectedFolders(selected_list);
          folder_adapter.update();

        }
      }
    });
    folders_recyclerView.setAdapter(folder_adapter);
  }

  private void load_files_in_recyclerView(String folder_path) {

    ArrayList<String> files_path = new ArrayList<>(Objects.requireNonNull(data.get(folder_path)));
    files_adapter = new FilesRecyclerViewAdapter(this, files_path, item_size);

    files_adapter.setListener(new FilesRecyclerViewAdapter.FileListener() {
      @Override
      public void onClick(int position, String filePath) {
//        ArrayList<String> files = files_adapter.getFiles();
//        String file_path = files.get(position);
//        if (current_layout_variant.equals("TYPE 2")) {
//          if (selection_on) {
//            if (selected_list.contains(file_path)) {
//              selected_list.remove(file_path);
//            } else {
//              selected_list.add(file_path);
//            }
//
//            HashMap<String, Integer> selected_files_count = new HashMap<>();
//            for (String file : selected_list) {
//              String[] parts = file.split("/");
//              ArrayList<String> partsArr = new ArrayList<>(Arrays.asList(parts));
//              partsArr.remove(partsArr.size() - 1);
//
//              String folder_path = String.join("/", partsArr);
//              if (selected_files_count.containsKey(folder_path)) {
//                selected_files_count.put(folder_path, selected_files_count.get(folder_path) + 1);
//              } else {
//                selected_files_count.put(folder_path, 1);
//              }
//            }
////                    Log.d("DEBUG", String.valueOf(selected_files_count));
//            folder_adapter.setSelectedFileCount(selected_files_count);
//            folder_adapter.update();
//
//            files_adapter.setSelectedFiles(selected_list);
//            files_adapter.update();
//
//          } else {
//            // show image in Full Screen View when image is clicked.
//            Intent intent = new Intent(MainActivity.this, FullScreenFileView.class);
//            intent.putExtra("files", files);
//            intent.putExtra("index", position);
//            startActivity(intent);
//          }
//        }
      }

      @Override
      public void onLongClick(int position, String filePath) {
//        if (current_layout_variant.equals("TYPE 2")) {
//          if (!selection_on) {
//            selected_list.clear();
//
//            selection_on = true;
//            selected_list.add(filePath);
//
//            files_adapter.setSelectionOn(true);
//            files_adapter.setSelectedFiles(selected_list);
//            files_adapter.update();
//
//            HashMap<String, Integer> selected_files_count = new HashMap<>();
//            for (String file : selected_list) {
//              String[] parts = file.split("/");
//              ArrayList<String> partsArr = new ArrayList<>(Arrays.asList(parts));
//              partsArr.remove(partsArr.size() - 1);
//
//              String folder_path = String.join("/", partsArr);
//              if (selected_files_count.containsKey(folder_path)) {
//                selected_files_count.put(folder_path, selected_files_count.get(folder_path) + 1);
//              } else {
//                selected_files_count.put(folder_path, 1);
//              }
//            }
//            folder_adapter.setSelectedFileCount(selected_files_count);
//            folder_adapter.update();
//          }
//        }
      }
    });

    files_recyclerView.setAdapter(files_adapter);
    Log.d("DEBUG", String.valueOf(files_path));

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
      selected_list.clear();
      selection_on = false;

      switch (current_layout_variant) {
        case "TYPE 1": {
          folder_adapter.setSelectionOn(false);
          folder_adapter.setSelectedFolders(selected_list);
          folder_adapter.update();
          return;
        }
        case "TYPE 2": {
          files_adapter.setSelectionOn(false);
          files_adapter.setSelectedFiles(selected_list);
          folder_adapter.setSelectedFileCount(new HashMap<>());
          return;
        }
      }


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

  String fileToMove;
  int newContactPosition = -1;
  int currentPosition = -1;
  boolean isExerciseAdded = false;
  boolean isFromExercise = false;

  @Override
  public boolean onDrag(View view, DragEvent dragEvent) {
    View selectedView = (View) dragEvent.getLocalState();
    RecyclerView rcvSelected = (RecyclerView) view;

    Log.d("DEBUG", dragEvent.toString());


    try {
      currentPosition = activityMainBinding.filesView.getChildAdapterPosition(selectedView);

      // Ensure the position is valid.
      if (currentPosition != -1) {
        fileToMove = files_adapter.getFiles().get(currentPosition);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    switch (dragEvent.getAction()) {
      case DragEvent.ACTION_DRAG_LOCATION:
        Log.d("DEBUG ACTION_DRAG_LOCATION", dragEvent.getX() + " " + dragEvent.getY());
        View onTopOf = rcvSelected.findChildViewUnder(dragEvent.getX(), dragEvent.getY());
        newContactPosition = rcvSelected.getChildAdapterPosition(onTopOf);
        break;
      case DragEvent.ACTION_DRAG_ENTERED:
        break;
      case DragEvent.ACTION_DRAG_EXITED:
        break;
      case DragEvent.ACTION_DROP:
        //when Item is dropped off to recyclerview.
        if (isFromExercise) {
          File file = new File(fileToMove);
          String folderPath = file.getParent();
          Set<String> files = data.get(folderPath);
          files.add(fileToMove);
//                    exerciseSelectedList.add(fileToMove);
//                    exerciseList.remove(fileToMove);
          activityMainBinding.filesView.getAdapter().notifyItemRemoved(currentPosition);
//                    activityMainBinding.();
        }
        //This is to hide/add the container!
                /*ViewGroup owner = (ViewGroup) (view.getParent());
                if (owner != null) {
                    //owner.removeView(selectedView);
                    //owner.addView(selectedView);

                    try {
                        LinearLayout rlContainer = (LinearLayout) view;
                        rlContainer.addView(selectedView);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //selectedView.setVisibility(View.VISIBLE);
                }*/

        break;

      case DragEvent.ACTION_DRAG_ENDED:
        // Reset the visibility for the Contact item's view.
        // This is done to reset the state in instances where the drag action didn't do anything.
        selectedView.setVisibility(View.VISIBLE);
        // Boundary condition, scroll to top is moving list item to position 0.
        if (newContactPosition != -1) {
          rcvSelected.scrollToPosition(newContactPosition);
          newContactPosition = -1;
        } else {
          rcvSelected.scrollToPosition(0);
        }
      default:
        break;
    }
    return false;
  }
}


























