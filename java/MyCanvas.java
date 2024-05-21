package com.example.paintapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MyCanvas extends View {
    
    private static final String DEBUG_TAG = "Velocity";
    private VelocityTracker mVelocityTracker = null;
    private final Path path;      //現在書いている線の情報
    Paint paint;        //現在書いている線の情報

    final List<DrawLine> lines;   //描画線の履歴

    private TextView tv_x;
    private TextView tv_y;

    private long startTime;
    private TextView timerText;
    private Context context;


    int alpha = 255;
    float cx = 0;
    float cy = 0;

    float beforeX = 0;
    float beforeY = 0;
    float afterX = 0;
    float afterY = 0;

    float beforeHx = 0;       //DOWNの時のx座標の履歴
    float beforeHy = 0;       //DOWNの時のy座標の履歴
    float afterHx = 0;      //UPの時のx座標の履歴
    float afterHy = 0;      //UPの時のy座標の履歴
    float movingHx = 0;     //動いている時のx座標の履歴
    float movingHy = 0;     //動いている時のy座標の履歴
    float preD = 0;     //タッチ圧力
    float preM = 0;     //動いてる時の圧力
    float preU = 0;       //離した時の圧力
    float touchSize = 0;    //タッチサイズ（HUAWEIは測定不可、NECも不可）
    ArrayList<Float> arrayListX = new ArrayList<>();    //x座標保存用の配列
    ArrayList<Float> arrayListY = new ArrayList<>();    //y座標保存用の配列
    ArrayList<Float> arrayListPre = new ArrayList<>();  //preM保存用の配列

    
    /* DrawLineはペンの色を変えるために設けたクラス */
    /* 過去に書いた線を履歴として保存していく */
    static class DrawLine {
        private final Paint paint;
        private final Path path;
        DrawLine(Path path, Paint paint) {
            this.paint = new Paint(paint);
            this.path = new Path(path);
        }

        void draw(Canvas canvas) {
            canvas.drawPath(this.path, this.paint);
            //canvas.drawCircle(cx, cy, 10, this.paint);
        }
    }


    public MyCanvas(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        path = new Path();      //線を引いたり、図形を描いたりする。グラフィック。

        paint = new Paint();    //筆の種類
        paint.setColor(Color.BLACK);     //色の指定
        paint.setStyle(Paint.Style.STROKE);     //線をひく
        paint.setStrokeWidth(80);       //線の太さ
        paint.setAntiAlias(true);       //アンチエイリアス、曲線描画
        paint.setMaskFilter(new BlurMaskFilter(5, BlurMaskFilter.Blur.NORMAL));

        this.lines = new ArrayList<DrawLine>();
        //this.lines = new ArrayList<DrawCircle>();
    }

    @Override
     protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(path, paint);

        for(DrawLine line : this.lines) {
            line.draw(canvas);
        }
        canvas.drawCircle(cx, cy , 20, paint);


        /*
        //圧力によって半径を大きくしたり小さくしたりする
        //タップした時
        if(0.13 <= preD) {
            canvas.drawCircle(beforeX, beforeY, 20, paint);
        }
        if(0.11 <= preD && preD < 0.13) {
            canvas.drawCircle(beforeX, beforeY, 14, paint);
        }
        if(0.09 < preD && preD < 0.11) {
            canvas.drawCircle(beforeX, beforeY, 8, paint);
        }
        if(0.08 > preD) {
            canvas.drawCircle(beforeX, beforeY, 2, paint);
        }

        //動かしている時
        for(int i = 0; i < arrayListPre.size(); i++) {
            if (0.13 <= arrayListPre.get(i)) {
                for (int j = 0; j < arrayListX.size(); j += 10) {
                    canvas.drawCircle(arrayListX.get(j), arrayListY.get(j), 20, this.paint);
                    //paint.setAlpha(alpha--);
                }
            }
        }

        for(int i = 0; i < arrayListPre.size(); i++) {
            if (0.11 <= arrayListPre.get(i) && arrayListPre.get(i) < 0.13) {
                for (int j = 0; j < arrayListX.size(); j += 10) {
                    canvas.drawCircle(arrayListX.get(j), arrayListY.get(j), 14, this.paint);
                    //paint.setAlpha(alpha--);
                }
            }
        }

        for(int i = 0;  i < arrayListPre.size(); i++) {
            if (0.09 < arrayListPre.get(i) && arrayListPre.get(i) < 0.11) {
                for (int j = 0; j < arrayListX.size(); j += 10) {
                    canvas.drawCircle(arrayListX.get(j), arrayListY.get(j), 8, this.paint);
                    //paint.setAlpha(alpha--);
                }
            }
        }

        for(int i = 0; i< arrayListPre.size(); i++) {
            if (0.08 > arrayListPre.get(i)) {
                for (int j = 0; j < arrayListX.size(); j += 10) {
                    canvas.drawCircle(arrayListX.get(j), arrayListY.get(j), 2, this.paint);
                    //paint.setAlpha(alpha--);
                }
            }
        }
         */


        if(arrayListX.size() > 0) {
            paint.setAlpha(alpha--);

            if(alpha == 0) {
                alpha = 255;
            }
        }


        /*
        for(int i = 0; i < arrayList1.size(); i++) {
            canvas.drawCircle(arrayList1.get(i), arrayList2.get(i), 30, this.paint);
        }
         */


        canvas.drawCircle(afterX, afterY, 30, paint);
        }


    //onTouchEventでクリックやドラックを判定
    @SuppressLint({"Range", "ClickableViewAccessibility"})
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int index = event.getActionIndex();
        int action = event.getActionMasked();
        int pointerId = event.getPointerId(index);
        int count = event.getPointerCount();

        //座標を取得（x座標、y座標）
        float x = event.getX();     //x座標取得
        float y = event.getY();     //y座標取得
        int hSize = 0;

        hSize = event.getHistorySize();

        int alpha = 255;

        //速度用の変数
        float vx = 0;
        float vy = 0;


        switch (action) {
            //タップした時
            case MotionEvent.ACTION_DOWN:
                beforeX = event.getX();
                beforeY = event.getY();

                //座標データの蓄積
                for(int j = 0; j < hSize; j++) {
                    beforeHx = event.getHistoricalX(pointerId, j);
                    beforeHy = event.getHistoricalY(pointerId, j);
                }
                //Log.i("", "Down X: " + beforeHx);
                //Log.i("", "Down Y: " + beforeHy);

                //トラッキング速度用
                if(mVelocityTracker == null)  {
                    mVelocityTracker = VelocityTracker.obtain();
                }
                else {
                    mVelocityTracker.clear();
                }
                mVelocityTracker.addMovement(event);

                path.moveTo(x, y);      //パターン描画ではなく、線で書くとき用

                preD = event.getPressure(pointerId);        //押下圧力測定（NECのタブレットだと測定不可）
                //touchSize = event.getSize(pointerId);           //タッチピクセルサイズ測定（HUAWEI,NECのタブレットだと測定不可）
                Log.i("" , "Pressure: " + preD);    //圧力のログ表示
                //Log.i("" , "TouchSize: " + touchSize);  //タッチビクセルサイズのログ表示
                invalidate();
                break;

            //ドラックした時
            case MotionEvent.ACTION_MOVE:
                mVelocityTracker.addMovement(event);
                mVelocityTracker.computeCurrentVelocity(5000);

                //preM = event.getPressure(pointerId);
                touchSize = event.getSize(pointerId);

                vx = mVelocityTracker.getXVelocity(pointerId);
                vy = mVelocityTracker.getYVelocity(pointerId);

                //座標を配列に保存
                for(int l = 0; l < hSize; l++) {
                    movingHx = event.getHistoricalX(pointerId, l);
                    movingHy = event.getHistoricalY(pointerId, l);

                    arrayListX.add(movingHx);
                    arrayListY.add(movingHy);
                }

                //圧力を配列に保存
                for(int l = 0; l < hSize; l+=5) {
                    preM = event.getPressure(pointerId);
                    arrayListPre.add(preM);
                }

                //Log.i("", "movingHx: " + movingHx);
                //Log.i("", "movingHy: " + movingHy);
                //Log.i("", "X velocity: " + vx);
                //Log.i("", "Y velocity: " + vy);

                cx = event.getX();
                cy = event.getY();
                //path.lineTo(x, y);
                invalidate();
                //i++;
                break;


            //離した時
            case MotionEvent.ACTION_UP:
                afterX = event.getX();
                afterY = event.getY();

                preU = event.getPressure(pointerId);

                for(int k = 0; k < hSize; k++) {
                    afterHx = event.getHistoricalX(pointerId, k);
                    afterHy = event.getHistoricalY(pointerId, k);
                }

                //path.lineTo(x, y);

                //指を離したので、履歴に追加する
                this.lines.add(new DrawLine(this.path, this.paint));
                path.reset();
                //paint.reset();
                break;

            case MotionEvent.ACTION_CANCEL:
                mVelocityTracker.recycle();
                break;
        }
        return true;
    }

    public void clearCanvas() {
        path.reset();       //現在の線をクリア
        paint.reset();
        invalidate();
    }
}
