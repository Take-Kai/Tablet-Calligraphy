package com.example.paintapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;


public class MainActivity extends AppCompatActivity {

    private static final String DEBUG_TAG = "Velocity";
    private VelocityTracker mVelocityTracker = null;

    private MyCanvas myCanvas;
    private Paint paint;
    
    Bitmap bitmap;
    String test = "sample";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //描画のIDを取得
        myCanvas = (MyCanvas)findViewById(R.id.myCanvas);
    }

    /*
    //クリアメソッド呼び出し
    public void OnClear(View view) {
       myCanvas.clearCanvas();
    }
     */

    @SuppressLint("WorldReadableFiles")
    protected void saveLocalFile(Bitmap bitmap) {
        FileOutputStream out;
        try {
            out = this.openFileOutput("filename01.png", Context.MODE_PRIVATE);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public Bitmap getViewCapture(View view) {
        view.setDrawingCacheEnabled(true);

        // Viewのキャッシュを取得
        Bitmap cache = view.getDrawingCache();
        Bitmap screenShot = Bitmap.createBitmap(cache);
        view.setDrawingCacheEnabled(false);
        return screenShot;
    }

    public void saveCapture(View view, File file) {
        // キャプチャを撮る
        Bitmap capture = getViewCapture(view);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file, false);
            //画像のフォーマットと画質と出力先を指定して保存
            capture.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException ie) {
                    fos = null;
                }
            }
        }
    }


        @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Bitmap image = null;


        //File file = new File(Environment.getExternalStorageDirectory() + "/capture.jpeg");
        File file = new File(getExternalFilesDir(Environment.DIRECTORY_DCIM), "sample.jpeg");
        //Objects.requireNonNull(file.getParentFile()).mkdir();

        if(id == R.id.action_save) {
            saveCapture(findViewById(android.R.id.content), file);
        }


        if(id == R.id.action_delete) {
            myCanvas.lines.clear();     //履歴をクリア
            myCanvas.paint.reset();
            myCanvas.clearCanvas();
        }

        if(id == R.id.red) {
            myCanvas.paint.setColor(Color.RED);
        }

        if(id == R.id.black) {
            myCanvas.paint.setColor(Color.BLACK);
        }

        if(id == R.id.blue) {
            myCanvas.paint.setColor(Color.BLUE);
        }

        String message = "「" + item.getTitle()  + "」が押されました。";
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        return true;
    }

    /*
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int index = event.getActionIndex();
        int action = event.getActionMasked();
        int pointerId = event.getPointerId(index);

        switch(action) {
            case MotionEvent.ACTION_DOWN:
                if(mVelocityTracker == null) {
                    mVelocityTracker = VelocityTracker.obtain();
                }
                else {
                    mVelocityTracker.clear();
                }
                mVelocityTracker.addMovement(event);
                break;

            case MotionEvent.ACTION_MOVE:
                mVelocityTracker.addMovement(event);
                mVelocityTracker.computeCurrentVelocity(1000);

                Log.d("TouchEvent", "X velocity: " + mVelocityTracker.getXVelocity(pointerId));
                Log.d("TouchEvent", "Y velocity: " + mVelocityTracker.getYVelocity(pointerId));
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mVelocityTracker.recycle();
                break;
        }
        return true;
    }
    */

    //public void OnSave(View view) throws IOException { myCanvas.saveBitmap();}

    /*
    @Override
    protected void onDraw(Canvas canvas) {
        paint.setColor(Color.argb(255, 255, 255, 255));
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        canvas.drawCircle(x, y, x)
    }
    
     */
}