package ca.uwaterloo.cs349;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

import java.util.ArrayList;

@SuppressLint("AppCompatCustomView")
public class Draw extends ImageView {

    int N = 128;
    // drawing
    boolean canSave = false;
    boolean moved = false;
    Path path = null;
    ArrayList<Float> x_list = new ArrayList<>();
    ArrayList<Float> y_list = new ArrayList<>();
    ArrayList<Float> x_process = new ArrayList<>();
    ArrayList<Float> y_process = new ArrayList<>();
    Paint paintbrush = new Paint(Color.BLUE);
    Bitmap background;

    public Draw(Context context) {
        super(context);
    }

    public Draw(Context context, AttributeSet attrst) {
        super(context, attrst);
    }

    public Draw(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    
    //referenced some sample code of cs349
    float x1, y1;
    int p1_id, p1_index;
    Matrix matrix = new Matrix();
    Matrix inverse = new Matrix();

    // capture touch events (down/move/up) to create a path/stroke that we draw later
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getPointerCount()) {
            case 1:
                p1_id = event.getPointerId(0);
                p1_index = event.findPointerIndex(p1_id);

                // invert using the current matrix to account for pan/scale
                // inverts in-place and returns boolean
                inverse = new Matrix();
                matrix.invert(inverse);

                // mapPoints returns values in-place
                float[] inverted = new float[]{event.getX(p1_index), event.getY(p1_index)};
                inverse.mapPoints(inverted);
                x1 = inverted[0];
                y1 = inverted[1];

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x_list.clear();
                        y_list.clear();
                        x_process.clear();
                        y_process.clear();
                        path = new Path();
                        path.moveTo(x1, y1);
                        invalidate();
                        canSave = false;
                        x_list.add(x1);
                        y_list.add(y1);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        path.lineTo(x1, y1);
                        x_list.add(x1);
                        y_list.add(y1);
                        invalidate();
                        paintbrush.setStyle(Paint.Style.STROKE);
                        paintbrush.setStrokeWidth(5);
                        moved = true;
                        break;
                    case MotionEvent.ACTION_UP:
                        process();
                        if(moved) {
                            canSave = true;
                            moved = false;
                        }
                        break;
                }
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // apply transformations from the event handler above
        canvas.concat(matrix);

        // draw background
        if (background != null) {
            this.setImageBitmap(background);
        }
        // draw lines over it
        if (path != null) {
            canvas.drawPath(path, paintbrush);
        }
    }

    void process() {
        int i = 0;
        float d = 0;
        float x_ = 0;
        float y_ = 0;
        float x1 = 0;
        float y1 = 0;
        float x2 = 0;
        float y2 = 0;
        while (i < x_list.size()) {
            if (i == 0) {
                y_ = y_list.get(i);
                x_ = x_list.get(i);
            } else {
                d += distance(x_list.get(i), y_list.get(i), x_, y_);
                x_ = x_list.get(i);
                y_ = y_list.get(i);
            }
            i++;
        }
        float interval = (float) d / (N - 1);
        i = 0;
        float remain = 0;
        while (i < x_list.size()) {
            if (i == 0) {
                x_process.add(x_list.get(i));
                y_process.add(y_list.get(i));
                y_ = y_list.get(i);
                x_ = x_list.get(i);
                x1 = x_;
                y1 = y_;
            } else {
                x2 = x_list.get(i);
                y2 = y_list.get(i);
                d = distance(x1, y1, x2, y2) + remain;
                while (d >= interval) {
                    x_ = x2 - d * (x2 - x1) / distance(x1, y1, x2, y2);
                    y_ = y2 - d * (y2 - y1) / distance(x1, y1, x2, y2);
                    x_process.add(x_);
                    y_process.add(y_);
                    d = d - interval;
                }
                remain = d;
                x1 = x2;
                y1 = y2;
            }
            i++;
        }
        i = 0;
        float x_sum = 0;
        float y_sum = 0;
        int num = x_process.size();
        while (i < num) {
            x_sum += x_process.get(i);
            y_sum += y_process.get(i);
            i++;
        }
        x_process.add(x_sum / num);
        y_process.add(y_sum / num);
    }

    float distance(float x1, float y1, float x2, float y2) {
        return (float) Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));
    }
}