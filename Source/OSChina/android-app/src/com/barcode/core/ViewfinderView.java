/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.barcode.core;

import com.barcode.camera.CameraManager;
import com.google.zxing.ResultPoint;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import net.oschina.app.R;

/**
 * 二维码扫描框视图
 * 
 */
public final class ViewfinderView extends View {

	private static final int[] SCANNER_ALPHA = { 0, 64, 128, 192, 255, 192,
			128, 64 };
	private static final long ANIMATION_DELAY = 30L;
	private static final int CURRENT_POINT_OPACITY = 0xA0;
	private static final int MAX_RESULT_POINTS = 20;
	private static final int POINT_SIZE = 6;

	private CameraManager cameraManager;
	private final Paint paint;
	private final Paint paintCorner;
	private Bitmap resultBitmap;
	private final int maskColor;
	private final int resultColor;
	private final int laserColor;
	private final int resultPointColor;
	private int scannerAlpha;
	private List<ResultPoint> possibleResultPoints;
	private List<ResultPoint> lastPossibleResultPoints;
	int loopTop, loopBottom;
	boolean isFirst;
	boolean isRun = true;

	public boolean isRun() {
		return isRun;
	}

	public void setRun(boolean isRun) {
		this.isRun = isRun;
	}

	// This constructor is used when the class is built from an XML resource.
	public ViewfinderView(Context context, AttributeSet attrs) {
		super(context, attrs);

		// Initialize these once for performance rather than calling them every
		// time in onDraw().
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paintCorner = new Paint(Paint.DITHER_FLAG);
		Resources resources = getResources();
		maskColor = resources.getColor(R.color.viewfinder_mask);
		resultColor = resources.getColor(R.color.result_view);
		laserColor = resources.getColor(R.color.viewfinder_laser);
		resultPointColor = resources.getColor(R.color.possible_result_points);
		scannerAlpha = 0;
		possibleResultPoints = new ArrayList<ResultPoint>(5);
		lastPossibleResultPoints = null;
	}

	public void setCameraManager(CameraManager cameraManager) {
		this.cameraManager = cameraManager;
	}

	@Override
	public void onDraw(Canvas canvas) {
		// Log.d("steven", "ondraw invoke:"+System.currentTimeMillis());
		if (cameraManager == null) {
			return; // not ready yet, early draw before done configuring
		}
		Rect frame = cameraManager.getFramingRect();
		if (frame == null) {
			return;
		}
		if (!isFirst) {
			loopTop = frame.top;
			isFirst = true;
			loopBottom = frame.bottom;
		}

		int width = canvas.getWidth();
		int height = canvas.getHeight();

		// Draw the exterior (i.e. outside the framing rect) darkened
		// 这里画取景框四周的四个阴影矩形
		paint.setColor(resultBitmap != null ? resultColor : maskColor);
		canvas.drawRect(0, 0, width, frame.top, paint);
		canvas.drawRect(0, frame.top, frame.left, frame.bottom + 1, paint);
		canvas.drawRect(frame.right + 1, frame.top, width, frame.bottom + 1,
				paint);
		canvas.drawRect(0, frame.bottom + 1, width, height, paint);
		// 这里画取景框四个角落的绿色夹角
		paintCorner.setColor(Color.GREEN);
		paintCorner.setAntiAlias(true);
		paintCorner.setStrokeWidth(5);
		canvas.drawLine(frame.left - 2.5f, frame.top, frame.left + 22,
				frame.top, paintCorner);
		canvas.drawLine(frame.left, frame.top, frame.left, frame.top + 22,
				paintCorner);
		canvas.drawLine(frame.right - 22, frame.top, frame.right + 2.5f,
				frame.top, paintCorner);
		canvas.drawLine(frame.right, frame.top, frame.right, frame.top + 22,
				paintCorner);
		canvas.drawLine(frame.left - 2.5f, frame.bottom, frame.left + 22,
				frame.bottom, paintCorner);
		canvas.drawLine(frame.left, frame.bottom - 22, frame.left,
				frame.bottom, paintCorner);
		canvas.drawLine(frame.right - 22, frame.bottom, frame.right + 2.5f,
				frame.bottom, paintCorner);
		canvas.drawLine(frame.right, frame.bottom - 22, frame.right,
				frame.bottom, paintCorner);
		
		if (resultBitmap != null) {
			// Draw the opaque result bitmap over the scanning rectangle
			paint.setAlpha(CURRENT_POINT_OPACITY);
			canvas.drawBitmap(resultBitmap, null, frame, paint);
		} else {

			// Draw a red "laser scanner" line through the middle to show
			// decoding is active
			paint.setColor(laserColor);
			paint.setAlpha(SCANNER_ALPHA[scannerAlpha]);
			scannerAlpha = (scannerAlpha + 1) % SCANNER_ALPHA.length;
			// 绘制中间的红线
			loopTop++;
			if (loopTop >= frame.bottom) {
				loopTop = frame.top;
			}
			Rect lineRect = new Rect(frame.left, loopTop - 10, frame.right, loopTop + 10);
            canvas.drawBitmap(((BitmapDrawable)(getResources().getDrawable(R.drawable.qrcode_scan_line))).getBitmap(), null, lineRect, paint);  

			Rect previewFrame = cameraManager.getFramingRectInPreview();
			float scaleX = frame.width() / (float) previewFrame.width();
			float scaleY = frame.height() / (float) previewFrame.height();

			List<ResultPoint> currentPossible = possibleResultPoints;
			List<ResultPoint> currentLast = lastPossibleResultPoints;
			int frameLeft = frame.left;
			int frameTop = frame.top;
			if (currentPossible.isEmpty()) {
				lastPossibleResultPoints = null;
			} else {
				possibleResultPoints = new ArrayList<ResultPoint>(5);
				lastPossibleResultPoints = currentPossible;
				paint.setAlpha(CURRENT_POINT_OPACITY);
				paint.setColor(resultPointColor);
				synchronized (currentPossible) {
					for (ResultPoint point : currentPossible) {
						canvas.drawCircle(frameLeft
								+ (int) (point.getX() * scaleX), frameTop
								+ (int) (point.getY() * scaleY), POINT_SIZE,
								paint);
					}
				}
			}
			// 绘制一闪一闪的黄点
			if (currentLast != null) {
				paint.setAlpha(CURRENT_POINT_OPACITY / 2);
				paint.setColor(resultPointColor);
				synchronized (currentLast) {
					float radius = POINT_SIZE / 2.0f;
					for (ResultPoint point : currentLast) {
						canvas.drawCircle(frameLeft
								+ (int) (point.getX() * scaleX), frameTop
								+ (int) (point.getY() * scaleY), radius, paint);
					}
				}
			}

			// 仅仅刷新中间的红线，让其一闪一闪的动画,不是整个矩形刷新
			loopTop+=2;
			// if(isRun){
			postInvalidateDelayed(ANIMATION_DELAY, frame.left - POINT_SIZE,
					frame.top - POINT_SIZE, frame.right + POINT_SIZE,
					frame.bottom + POINT_SIZE);
			// }
		}

	}

	public void drawViewfinder() {
		Bitmap resultBitmap = this.resultBitmap;
		this.resultBitmap = null;
		if (resultBitmap != null) {
			resultBitmap.recycle();
		}
		invalidate();
	}

	public void addPossibleResultPoint(ResultPoint point) {
		List<ResultPoint> points = possibleResultPoints;
		synchronized (points) {
			points.add(point);
			int size = points.size();
			if (size > MAX_RESULT_POINTS) {
				// trim it
				points.subList(0, size - MAX_RESULT_POINTS / 2).clear();
			}
		}
	}

}
