package cn.qs.android.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import java.math.BigDecimal;

import cn.qs.android.utils.ScreenUtils;

/**
 * Created by chenxin on 17/01/2017.
 */

public class PieChartView extends View {
    private int screenW, screenH;
    /**
     * The paint to draw text, pie and line.
     */
    private Paint textPaint, piePaint, linePaint;

    /**
     * The center and the radius of the pie.
     */
    private int pieCenterX, pieCenterY, pieRadius;
    /**
     * The oval to draw the oval in.
     */
    private RectF pieOval;

    private float smallMargin;

    private int[] mPieColors = new int[]{Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.MAGENTA, Color.CYAN};

    private PieItemBean[] mPieItems;
    private float totalValue;

    public PieChartView(Context context) {
        super(context);

        init(context);
    }

    public PieChartView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    public PieChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context);
    }

    private void init(Context context) {
        //init screen
        screenW = ScreenUtils.getScreenW(context);
        screenH = ScreenUtils.getScreenH(context);

        pieCenterX = screenW / 2;
        pieCenterY = ScreenUtils.dp2px(context, 100);
        pieRadius = screenW / 4;
        smallMargin = ScreenUtils.dp2px(context, 5);

        pieOval = new RectF();
        pieOval.left = pieCenterX - pieRadius;
        pieOval.top = pieCenterY - pieRadius;
        pieOval.right = pieCenterX + pieRadius;
        pieOval.bottom = pieCenterY + pieRadius;

        //The paint to draw text.
        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(ScreenUtils.dp2px(context, 16));

        //The paint to draw circle.
        piePaint = new Paint();
        piePaint.setAntiAlias(true);
        piePaint.setStyle(Paint.Style.FILL);

        //The paint to draw line to show the concrete text
        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setStrokeWidth(ScreenUtils.dp2px(context, 1));

    }

    //The degree position of the last item arc's center.
    private float lastDegree = 0;
    //The count of the continues 'small' item.
    private int addTimes = 0;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mPieItems != null && mPieItems.length > 0) {
            float start = 0.0f;
            for (int i = 0; i < mPieItems.length; i++) {
                //draw pie
                piePaint.setColor(mPieColors[i % mPieColors.length]);
                float sweep = mPieItems[i].getItemValue() / totalValue * 360;
                canvas.drawArc(pieOval, start, sweep, true, piePaint);

                //draw line away from the pie
                float radians = (float) ((start + sweep / 2) / 180 * Math.PI);
                float lineStartX = pieCenterX + pieRadius * 0.7f * (float) (Math.cos(radians));
                float lineStartY = pieCenterY + pieRadius * 0.7f * (float) (Math.sin(radians));

                float lineStopX, lineStopY;
                float rate;
                if (getOffset(start + sweep / 2) > 60) {
                    rate = 1.3f;
                } else if (getOffset(start + sweep / 2) > 30) {
                    rate = 1.2f;
                } else {
                    rate = 1.1f;
                }
                //If the item is very small, make the text further away from the pie to avoid being hided by other text.
                if (start + sweep / 2 - lastDegree < 30) {
                    addTimes++;
                    rate += 0.2f * addTimes;
                } else {
                    addTimes = 0;
                }
                lineStopX = pieCenterX + pieRadius * rate * (float) (Math.cos(radians));
                lineStopY = pieCenterY + pieRadius * rate * (float) (Math.sin(radians));
                canvas.drawLine(lineStartX, lineStartY, lineStopX, lineStopY, linePaint);

                //write text
                String itemTypeText = mPieItems[i].getItemType();
                float value  = mPieItems[i].getItemValue() / totalValue * 100 ;
                BigDecimal b   =   new   BigDecimal(value);
                String itemPercentText = b.setScale(2,BigDecimal.ROUND_HALF_UP).floatValue() + "%";
                float itemTypeTextLen = textPaint.measureText(itemTypeText);
                float itemPercentTextLen = textPaint.measureText(itemPercentText);
                float lineTextWidth = Math.max(itemTypeTextLen, itemPercentTextLen);

                float textStartX = lineStopX;
                float textStartY = lineStopY - smallMargin;
                float percentStartX = lineStopX;
                float percentStartY = lineStopY + textPaint.getTextSize();
                if (lineStartX > pieCenterX) {
                    textStartX += (smallMargin + Math.abs(itemTypeTextLen - lineTextWidth) / 2);
                    percentStartX += (smallMargin + Math.abs(itemPercentTextLen - lineTextWidth) / 2);
                } else {
                    textStartX -= (smallMargin + lineTextWidth - Math.abs(itemTypeTextLen - lineTextWidth) / 2);
                    percentStartX -= (smallMargin + lineTextWidth - Math.abs(itemPercentTextLen - lineTextWidth) / 2);
                }
                canvas.drawText(itemTypeText, textStartX, textStartY, textPaint);
                //draw percent text
                canvas.drawText(itemPercentText, percentStartX, percentStartY, textPaint);

                //draw text underline
                float textLineStopX = lineStopX;
                if (lineStartX > pieCenterX) {
                    textLineStopX += (lineTextWidth + smallMargin * 2);
                } else {
                    textLineStopX -= (lineTextWidth + smallMargin * 2);
                }
                canvas.drawLine(lineStopX, lineStopY, textLineStopX, lineStopY, linePaint);

                lastDegree = start + sweep / 2;
                start += sweep;
            }
        }
    }

    public PieItemBean[] getPieItems() {
        return mPieItems;
    }

    public void setPieItems(PieItemBean[] pieItems) {
        this.mPieItems = pieItems;

        totalValue = 0;
        for (PieItemBean item : mPieItems) {
            totalValue += item.getItemValue();
        }

        invalidate();
    }

    private float getOffset(float radius) {
        int a = (int) (radius % 360 / 90);
        switch (a) {
            case 0:
                return radius;
            case 1:
                return 180 - radius;
            case 2:
                return radius - 180;
            case 3:
                return 360 - radius;
        }

        return radius;
    }


    public static class PieItemBean {
        private String itemType;
        private float itemValue;

        public PieItemBean(String itemType, float itemValue) {
            this.itemType = itemType;
            this.itemValue = itemValue;
        }

        public String getItemType() {
            return itemType;
        }

        public void setItemType(String itemType) {
            this.itemType = itemType;
        }

        public float getItemValue() {
            return itemValue;
        }

        public void setItemValue(float itemValue) {
            this.itemValue = itemValue;
        }
    }
}