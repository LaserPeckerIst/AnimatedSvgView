package com.jaredrummler.android.animatedsvgview.demo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;

import com.jaredrummler.android.widget.AnimatedSvgView;

/**
 * @author <a href="mailto:angcyo@126.com">angcyo</a>
 * @since 2022/06/28
 */
public class TextPathAnimateView extends AnimatedSvgView {

    public String animateText;
    public Path animateTextPath;

    public Paint paint;
    public Rect textBounds;
    public RectF pathBounds;

    public TextPathAnimateView(Context context) {
        super(context);
        initView();
    }

    public TextPathAnimateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public TextPathAnimateView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        updateTextPath();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        /*try {
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.RED);
            canvas.drawPath(animateTextPath, paint);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    public void initView() {
        animateTextPath = new Path();
        textBounds = new Rect();
        pathBounds = new RectF();

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(180);//设置字体大小, 影响Path的大小

        //setFillStart(); //开始fill的时间
        //setFillTime(); //fill需要多少时间

        //光线的颜色
        setTraceColors(new int[]{Color.BLACK});
        //最后填充的颜色
        setFillColors(new int[]{Color.BLACK});
    }

    public void updateTextPath() {
        if (animateText == null || animateText.length() == 0) {
            return;
        }
        animateTextPath.rewind();
        paint.getTextBounds(animateText, 0, animateText.length(), textBounds);
        paint.getTextPath(animateText, 0, animateText.length(), -textBounds.left, -textBounds.top, animateTextPath);
        setGlyphPaths(animateTextPath);

        animateTextPath.computeBounds(pathBounds, true);
        setViewportSize(pathBounds.width(), pathBounds.height());

        if (getMeasuredWidth() > 0 && getMeasuredHeight() > 0) {
            rebuildGlyphData();
        }
    }

    /**
     * 设置动画动画
     */
    public void setAnimateText(String animateText) {
        this.animateText = animateText;
        updateTextPath();

        //start();
    }
}
