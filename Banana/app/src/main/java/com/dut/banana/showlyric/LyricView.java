package com.dut.banana.showlyric;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.dut.banana.KaraLine;

import java.util.List;

/**
 * LyricView can display lyric file and Seek it.
 */
public class LyricView extends View implements ILyricView{

    public final static String TAG = "LrcView";

    /** normal display mode*/
    public final static int DISPLAY_MODE_NORMAL = 0;
    /** seek display mode */
    public final static int DISPLAY_MODE_SEEK = 1;
    /** scale display mode ,scale font size*/
    public final static int DISPLAY_MODE_SCALE = 2;

    private List<KaraLine> mLrcRows; 	// all lyric rows of one lyric file
    private int mMinSeekFiredOffset = 10; // min offset for fire seek action, px;
    private int mHignlightRow = 0;   // current singing row , should be highlighted.
    private int mHignlightRowColor = Color.YELLOW;
    private int mNormalRowColor = Color.WHITE;
    private int mSeekLineColor = Color.CYAN;
    private int mSeekLineTextColor = Color.CYAN;
    private int mSeekLineTextSize = 15;
    private int mMinSeekLineTextSize = 13;
    private int mMaxSeekLineTextSize = 18;
    private int mLrcFontSize = 23; 	// font size of lrc
    private int mMinLrcFontSize = 15;
    private int mMaxLrcFontSize = 35;
    private int mPaddingY = 10;		// padding of each row
    private int mSeekLinePaddingX = 0; // Seek line padding x
    private int mDisplayMode = DISPLAY_MODE_NORMAL;
    private LyricViewListener mLrcViewListener;

    private String mLoadingLrcTip = "No lyric!";

    private Paint mPaint;

    public LyricView(Context context,AttributeSet attr){
        super(context, attr);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTextSize(mLrcFontSize);
    }

    public void setListener(LyricViewListener l){
        mLrcViewListener = l;
    }

    public void setLoadingTipText(String text){
        mLoadingLrcTip = text;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        final int height = getHeight(); // height of this view
        final int width = getWidth() ; // width of this view
        if(mLrcRows == null || mLrcRows.size() == 0){
            if(mLoadingLrcTip != null){
                // draw tip when no lrc.
                mPaint.setColor(mHignlightRowColor);
                mPaint.setTextSize(mLrcFontSize);
                mPaint.setTextAlign(Align.CENTER);
                canvas.drawText(mLoadingLrcTip, width / 2, height / 2 - mLrcFontSize, mPaint);
            }
            return;
        }

        if (mHignlightRow < 0)
            return;

        int rowY = 0; // vertical point of each row.
        final int rowX = width / 2;
        int rowNum = 0;

        // 1, draw highlight row at center.
        // 2, draw rows above highlight row.
        // 3, draw rows below highlight row.

        // 1 highlight row// hang o giua
        String highlightText = mLrcRows.get(mHignlightRow).getLine();
        int highlightRowY = height / 2 - mLrcFontSize;
        mPaint.setColor(mHignlightRowColor);
        mPaint.setTextSize(mLrcFontSize);
        mPaint.setTextAlign(Align.CENTER);
        canvas.drawText(highlightText, rowX, highlightRowY, mPaint);

        if(mDisplayMode == DISPLAY_MODE_SEEK){
            // draw Seek line and current time when moving.
            mPaint.setColor(mSeekLineColor);
            canvas.drawLine(mSeekLinePaddingX, highlightRowY, width - mSeekLinePaddingX, highlightRowY, mPaint);
            mPaint.setColor(mSeekLineTextColor);
            mPaint.setTextSize(mSeekLineTextSize);
            mPaint.setTextAlign(Align.LEFT);
            canvas.drawText(mLrcRows.get(mHignlightRow).getLine(), 0, highlightRowY, mPaint);
        }

        // 2 above rows // cac hang ben tren
        mPaint.setColor(mNormalRowColor);
        mPaint.setTextSize(mLrcFontSize);
        mPaint.setTextAlign(Align.CENTER);
        rowNum = mHignlightRow - 1;
        rowY = highlightRowY - mPaddingY - mLrcFontSize;
        while( rowY > -mLrcFontSize && rowNum >= 0){
            String text = mLrcRows.get(rowNum).getLine();
            canvas.drawText(text, rowX, rowY, mPaint);
            rowY -=  (mPaddingY + mLrcFontSize);
            rowNum --;
        }

        // 3 below rows // cac hang phia duoi
        rowNum = mHignlightRow + 1;
        rowY = highlightRowY + mPaddingY + mLrcFontSize;
        while( rowY < height && rowNum < mLrcRows.size()){
            String text = mLrcRows.get(rowNum).getLine();
            canvas.drawText(text, rowX, rowY, mPaint);
            rowY += (mPaddingY + mLrcFontSize);
            rowNum ++;
        }
    }

    private void setNewFontSize(int scaleSize){
        mLrcFontSize += scaleSize;
        mSeekLineTextSize += scaleSize;
        mLrcFontSize = Math.max(mLrcFontSize, mMinLrcFontSize);
        mLrcFontSize = Math.min(mLrcFontSize, mMaxLrcFontSize);
        mSeekLineTextSize = Math.max(mSeekLineTextSize, mMinSeekLineTextSize);
        mSeekLineTextSize = Math.min(mSeekLineTextSize, mMaxSeekLineTextSize);
    }

    public void setNowDuration(long duration) {
        if (mLrcRows == null)
            return;
        int last = mHignlightRow;
        for (mHignlightRow = mLrcRows.size() - 1; mHignlightRow >= 0; mHignlightRow--) {
            if (mLrcRows.get(mHignlightRow).getStartTime().getDuration() <= duration) {
                break;
            }
        }
        if (last != mHignlightRow)
            invalidate();
    }

    // :| this code is fuck my life, it's just fake code for handle double click
    // event instead of long click event, i don't know why key down event don't raise
    // in onTouchEvent method, maybe Android fuck it :|
    // #NE
    private boolean lastClick = false;
    private final Handler handler = new Handler();
    private Runnable resetLastClick = new Runnable() {
        public void run() {
            lastClick = false;
        }
    };

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (lastClick) {
                performLongClick();
                lastClick = false;
                handler.removeCallbacks(resetLastClick);
            } else {
                lastClick = true;
                handler.postDelayed(resetLastClick, 1000);
            }
        }
        return true;
    }

    public void setLyric(List<KaraLine> lrcRows) {
        mLrcRows = lrcRows;
        // phuong thuc co chuc nang ve lien tuc len man hinh giup minh update duoc cac cau lyric line can ve
        //invalidate() se goi den ham onDraw(Canvas canvas)
        invalidate();
    }
}
