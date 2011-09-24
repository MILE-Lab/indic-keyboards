package org.iisc.mile.indicapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * A custom EditText that draws lines between each line of text that is displayed.
 */
public class LinedEditText extends EditText {
	private Rect mRect;
	private Paint mPaint;

	// we need this constructor for LayoutInflater
	public LinedEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		mRect = new Rect();
		mPaint = new Paint();
		mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
		mPaint.setColor(Color.GRAY);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		int count = getLineCount();
		Rect r = mRect;
		Paint paint = mPaint;
		int baseline = getLineBounds(0, r);
		for (int i = 0; i < count; i++) {
			baseline = getLineBounds(i, r);
			canvas.drawLine(r.left, baseline + 1, r.right, baseline + 1, paint);
		}
		int estimatedLineCount = getHeight() / getLineHeight();
		for (int i = count; i < estimatedLineCount; i++) {
			baseline += getLineHeight(); // next line
			canvas.drawLine(r.left, baseline + 1, r.right, baseline + 1, paint);
		}
		super.onDraw(canvas);
	}
}
