package com.example.picas;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;

public class Gestures extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestures);

        SwipeGestureDetector swipeGestureDetector =new SwipeGestureDetector();
//        swipeGestureDetector.leftSwipe();
//        swipeGestureDetector.onSwipeListener.onSwipeLeft();

    }


    public static class SwipeGestureDetector extends GestureDetector.SimpleOnGestureListener {
        OnSwipeListener onSwipeListener;

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (e1.getX() - e2.getX() > 100) {
                onSwipeListener.onSwipeLeft();
                return true;
            }
            return false;
        }

        public void setOnSwipeListener(OnSwipeListener onSwipeListener) {
            this.onSwipeListener = onSwipeListener;
        }

        public interface OnSwipeListener {
            void onSwipeLeft();
        }

        public void leftSwipe(){

        }
    }
}