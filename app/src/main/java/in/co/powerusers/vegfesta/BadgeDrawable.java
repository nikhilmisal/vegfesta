package in.co.powerusers.vegfesta;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;

/**
 * Created by Powerusers on 26-11-2017.
 */

public class BadgeDrawable extends Drawable {
    private Paint mBadgePaint;
    //private Paint mBadgePaint1;
    private Paint mTextPaint;
    private Rect mTextRect = new Rect();

    private String mCount = "";
    private boolean mWillDraw;
    private static final String TAG = "BadgeDrawable";
    public BadgeDrawable(Context context)
    {
        float mTextSize = context.getResources().getDimension(R.dimen.badge_dimen_size);
        mBadgePaint = new Paint();
        mBadgePaint.setColor(Color.RED);
        mBadgePaint.setAntiAlias(true);
        mBadgePaint.setStyle(Paint.Style.FILL);
        //mBadgePaint1 = new Paint();
        //mBadgePaint1.setColor(ContextCompat.getColor(context.getApplicationContext(),R.color.colorAccent));
        //mBadgePaint1.setAntiAlias(true);
        //mBadgePaint1.setStyle(Paint.Style.FILL);

        mTextPaint = new Paint();
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTypeface(Typeface.DEFAULT);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        if(!mWillDraw)
        {
            return;
        }

        Rect bounds = getBounds();
        float width = bounds.right - bounds.left;
        float height = bounds.bottom - bounds.top;
        //float width = 1;
        //float height = 1;

        float radius = (Math.max(width,height)/2)/2;
        float centerX = (width-radius-1)+5;
        float centerY = radius-5;
        Log.d(TAG,"width: "+width+"  height: "+height+"    radius: "+radius+"   centerX: "+centerX+"   centerY: "+centerY);
        if(mCount.length() <=2)
        {
            //canvas.drawCircle(centerX,centerY,(int)(radius+12),mBadgePaint1);
            canvas.drawCircle(centerX,centerY,(int)(radius+6),mBadgePaint);
        }else
        {
            //canvas.drawCircle(centerX,centerY,(int)(radius+14),mBadgePaint1);
            canvas.drawCircle(centerX,centerY,(int)(radius+7),mBadgePaint);
        }
        mTextPaint.getTextBounds(mCount,0,mCount.length(),mTextRect);
        float textHeight = mTextRect.bottom - mTextRect.top;
        float textY = centerY + (textHeight/2f);
        if(mCount.length() > 2)
        {
            canvas.drawText("99+",centerX,textY,mTextPaint);
        }else
        {
            canvas.drawText(mCount,centerX,textY,mTextPaint);
        }
    }

    public void setmCount(String count)
    {
        mCount = count;
        mWillDraw = !count.equalsIgnoreCase("0");
        invalidateSelf();
    }

    @Override
    public void setAlpha(int i) {

    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.UNKNOWN;
    }
}
