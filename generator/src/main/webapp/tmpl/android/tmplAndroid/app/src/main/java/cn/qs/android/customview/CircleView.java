package cn.qs.android.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

/**
 * Created by pangpingfei on 2017/1/13.
 */


public class CircleView extends TextView {

    private Paint mBgPaint = new Paint();

    private int badgeNumber = 0;

    PaintFlagsDrawFilter pfd = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);

    public CircleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
    }

    public CircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        mBgPaint.setColor(Color.RED);
        mBgPaint.setAntiAlias(true);
    }

    public CircleView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        mBgPaint.setColor(Color.RED);
        mBgPaint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();
        int max = Math.max(measuredWidth, measuredHeight);
        setMeasuredDimension(max, max);
    }

    @Override
    public void setBackgroundColor(int color) {
        // TODO Auto-generated method stub
        mBgPaint.setColor(color);
    }

    /**
     * 设置标记数量
     *
     * @param value <=0时不显示，>99时显示99+。
     */
    public void setBadgeNumber(Integer value) {
        if (value == null || value <= 0) {
            setText("");
            this.setVisibility(View.GONE);
            badgeNumber = 0;
        } else {
            setText((value > 99 ? 99 : value) + "");
            this.setVisibility(View.VISIBLE);
            badgeNumber = value;
        }
    }

    /**
     * 获取标记数量
     */
    public int getBadgeNumber() {
        return this.badgeNumber;
    }

    /**
     * 减少标记数量（-1）
     */
    public void reduceBadgeNumber() {
        setBadgeNumber(getBadgeNumber() - 1);
    }

    /**
     * 减少标记数量
     *
     * @param reduceValue 减少的数量。
     */
    public void reduceBadgeNumber(int reduceValue) {
        setBadgeNumber(getBadgeNumber() - reduceValue);
    }

    /**
     * 增加标记数量（+1）
     */
    public void increaseBadgeNumber() {
        setBadgeNumber(getBadgeNumber() + 1);
    }

    /**
     * 增加标记数量
     *
     * @param increaseValue 增加的数量。
     */
    public void increaseBadgeNumber(int increaseValue) {
        setBadgeNumber(getBadgeNumber() + increaseValue);
    }

    @Override
    public void draw(Canvas canvas) {
        // TODO Auto-generated method stub
        canvas.setDrawFilter(pfd);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, Math.max(getWidth(), getHeight()) / 2, mBgPaint);
        super.draw(canvas);
    }
}