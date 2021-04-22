package ca.uwaterloo.cs349;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.widget.ImageView;

@SuppressLint("AppCompatCustomView")
public class Show extends ImageView {

    Path path = null;
    boolean canShow = false;
    Paint paintbrush = new Paint(Color.BLUE);
    Bitmap background;

    public Show(Context context) {
        super(context);
    }

    public Show(Context context, AttributeSet attrst) {
        super(context, attrst);
    }

    public Show(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // draw background
        if (background != null) {
            this.setImageBitmap(background);
        }
        // draw lines over it
        if (canShow && path!=null) {
            paintbrush.setStyle(Paint.Style.STROKE);
            paintbrush.setStrokeWidth(5);
            canvas.drawPath(path, paintbrush);
        }
    }

    public void process(Gesture g) {
        float[] x = g.x;
        float[] y = g.y;
        float up = y[0];
        float down = y[0];
        float left = x[0];
        float right = x[0];
        for(int i = 0; i< x.length-1; i++) {
            up = Math.max(up, y[i]);
            down = Math.min(down, y[i]);
            left = Math.min(left, x[i]);
            right = Math.max(right, x[i]);
        }
        float y_center = (up + down)/2;
        float x_center = (left + right)/2;
        float max_length = Math.max(right - left, up - down);
        float factor = 280/max_length;
        path = new Path();
        for(int i = 0; i< x.length; i++) {
            x[i] = (x[i]-x_center)*factor+150;
            y[i] = (y[i]-y_center)*factor+150;
            if(i == 0) {
                path.moveTo(x[i],y[i]);
            } else if (i != x.length - 1) {
                path.lineTo(x[i],y[i]);
            }
        }
        canShow = true;
        invalidate();
    }
}