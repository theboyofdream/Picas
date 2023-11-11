package com.example.picas.trash;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.picas.databinding.ActivityGesturesBinding;
import com.example.picas.databinding.ActivityMainBinding;

import java.util.Date;

public class Gestures extends AppCompatActivity implements GestureDetector.OnGestureListener {

    TextView text;

    private static float x1, y1, x2, y2;
    private static long t1, t2;
    private static final int SWIPE_THRESHOLD = 100, SWIPE_TIME_THRESHOLD = 400;
    int count = 0;

    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityGesturesBinding binding = ActivityGesturesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        this.text = binding.gestureLogTextView;
        text.setText("heeloeoeo");

        gestureDetector = new GestureDetector(this, this);
    }

    protected void log(String msg) {
        if (count >= 4) {
            text.setText(msg);
            count = 0;
        } else {
            String previousStr = String.valueOf(text.getText());
            String newText = previousStr + "\n" + msg;
            text.setText(newText);
            count++;
        }
        Log.d("DEBUG_LOG", msg);
    }


    void resetGestureVariable() {
        x1 = 0;
        y1 = 0;
        x2 = 0;
        y2 = 0;
        t1 = 0;
        t2 = 0;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        switch (event.getAction()) {
            case (MotionEvent.ACTION_DOWN):
                x1 = event.getX();
                y1 = event.getY();
                t1 = (new Date()).getTime();
//                log("Action was DOWN");
                break;
//                return true;
//            case (MotionEvent.ACTION_MOVE):
//                log("Action was MOVE");
//                return true;
            case (MotionEvent.ACTION_UP):
                x2 = event.getX();
                y2 = event.getY();
                t2 = (new Date()).getTime();

                float diffX = Math.abs(x2 - x1);
                float diffY = Math.abs(y2 - y1);
                long diffT = Math.abs(t2 - t1);

//                log(String.format("t1 %d", t1));
//                log(String.format("t2 %d", t2));

                if (diffT < SWIPE_TIME_THRESHOLD) {
                    if (diffX > SWIPE_THRESHOLD && diffX > diffY) {
                        if (x2 > x1) {
                            log("left");
//                            log(String.format("diffT %d", diffT));
                        } else {
                            log("right");
                        }
                    } else if (diffY > SWIPE_THRESHOLD && diffY > diffX) {
                        if (y2 > y1) {
                            log("bottom");
                        } else {
                            log("top");
                        }
                    }
                }
//                log("Action was UP");
//                return true;
                break;
            case (MotionEvent.ACTION_CANCEL):
            case (MotionEvent.ACTION_OUTSIDE):
                resetGestureVariable();
//                log("Action was CANCEL");
                break;
//                return true;
            //                log("Movement occurred outside bounds of current screen element");
//            default:
//                return super.onTouchEvent(event);
        }

        return super.onTouchEvent(event);
//        return super.onTouchEvent(event);
    }

    @Override
    public boolean onDown(@NonNull MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(@NonNull MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(@NonNull MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(@NonNull MotionEvent e1, @NonNull MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(@NonNull MotionEvent e) {

    }

    @Override
    public boolean onFling(@NonNull MotionEvent e1, @NonNull MotionEvent e2, float velocityX, float velocityY) {
//        log("fling");
//        float diffX = e2.getX() - e1.getX();
//        float diffY = e2.getY() - e1.getY();
//
//        if (Math.abs(diffX) > Math.abs(diffY) &&
//                Math.abs(diffX) > SWIPE_THRESHOLD &&
//                Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
//            if (diffX > 0) {
//                // Right swipe
//                log("right");
////                    onSwipeRight();
//            } else {
//                // Left swipe
//                log("left");
////                    onSwipeLeft();
//            }
//            return true;
//        } else if (Math.abs(diffY) > SWIPE_THRESHOLD &&
//                Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
//            if (diffY > 0) {
//                // Bottom swipe
//                log("bottom");
////                    onSwipeBottom();
//            } else {
//                // Top swipe
//                log("top");
////                    onSwipeTop();
//            }
//            return true;
//        }
        return false;
    }

//    public boolean onTouchEvent(MotionEvent event) {
//        switch (event.getAction( )) {
//            case (MotionEvent.ACTION_DOWN):
//                log("Action was DOWN");
//                return true;
//            case (MotionEvent.ACTION_MOVE):
//                log("Action was MOVE");
//                return true;
//            case (MotionEvent.ACTION_UP):
//                log("Action was UP");
//                return true;
//            case (MotionEvent.ACTION_CANCEL):
//                log("Action was CANCEL");
//                return true;
//            case (MotionEvent.ACTION_OUTSIDE):
//                log("Movement occurred outside bounds of current screen element");
//                return true;
//            default:
//                return super.onTouchEvent(event);
//        }
//    }

//    @Override
//    public boolean onTouch(View v, MotionEvent event) {
//        return  gestureDetector.onTouchEvent(event);
//    }
//
//    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
//
//        private static final int SWIPE_THRESHOLD = 100;
//        private static final int SWIPE_VELOCITY_THRESHOLD = 100;
//
//        @Override
//        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
//            float diffX = e2.getX() - e1.getX();
//            float diffY = e2.getY() - e1.getY();
//
//            if (Math.abs(diffX) > Math.abs(diffY) &&
//                    Math.abs(diffX) > SWIPE_THRESHOLD &&
//                    Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
//                if (diffX > 0) {
//                    // Right swipe
//                    onSwipeRight();
//                } else {
//                    // Left swipe
//                    onSwipeLeft();
//                }
//                return true;
//            } else if (Math.abs(diffY) > SWIPE_THRESHOLD &&
//                    Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
//                if (diffY > 0) {
//                    // Bottom swipe
//                    onSwipeBottom();
//                } else {
//                    // Top swipe
//                    onSwipeTop();
//                }
//                return true;
//            }
//            return false;
//        }
//    }
//
//    public void onSwipeRight() {
//        // Implement your right swipe action here
//        Toast.makeText(this, "right",Toast.LENGTH_SHORT).show();
//        log("right swipe");
//    }
//
//    public void onSwipeLeft() {
//        // Implement your left swipe action here
//        log("left swipe");
//    }
//
//    public void onSwipeTop() {
//        // Implement your top swipe action here
//        log("top swipe");
//    }
//
//    public void onSwipeBottom() {
//        // Implement your bottom swipe action here
//        log("bottom swipe");
//    }
}