package com.kernal.plateid.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.View;

/***
 * @author user
 * 识别框展示
 */
@SuppressLint("ViewConstructor")
public  final class ViewfinderView extends View {
	private final Paint paint;
	private final Paint paintLine;
	public Rect frame;
	private boolean isPortrait;
	public int width, height,top,left,wLength,hLength;
	public ViewfinderView(Context context, Point srcPoint, boolean isPortrait) {
		super(context);
		paint = new Paint();
		paintLine = new Paint();
		this.width = srcPoint.x;
		this.height = srcPoint.y;
		this.isPortrait = isPortrait;
	}

	@SuppressLint("DrawAllocation")
	@Override
	public void onDraw(Canvas canvas) {
		 //这个矩形就是中间的扫描框
		//left : 扫描框左坐标
		//top： 扫描框顶点坐标
		//Wlength: 扫描框宽度
		//Hlength：扫描框高度
		//真实的识别区域的算法代码在MyPreviewCallback 类中，其原理是将扫描框映射到预览图像上
		if(isPortrait){
			left =width/24;
			top  = height/4;
			wLength =width*11/12;
			hLength = height/3;
		}else {
			left = width /4;
			top = height  /4;
			wLength = width / 2;
			hLength = height / 2;
		}
		frame = new Rect(left, top, left+wLength, top+hLength);
			// 画阴影部分，分四部分，从屏幕上方到扫描框的上方，从屏幕左边到扫描框的左边
			// 从扫描框右边到屏幕右边，从扫描框底部到屏幕底部
		paint.setColor(Color.argb(100, 0, 0, 0));
		canvas.drawRect(0, 0, width, frame.top, paint);
		canvas.drawRect(0, frame.top, frame.left, frame.bottom, paint);
		canvas.drawRect(frame.right , frame.top, width, frame.bottom ,
					paint);
		canvas.drawRect(0, frame.bottom, width, height, paint);
			// 外框
			paintLine.setColor(Color.rgb(43, 171, 172));
			paintLine.setStrokeWidth(5);
			paintLine.setAntiAlias(true);
		canvas.drawLine(frame.left, frame.top, frame.right, frame.top, paintLine);
		canvas.drawLine(frame.left, frame.top-2, frame.left, frame.bottom, paintLine);
		canvas.drawLine(frame.left-2, frame.bottom, frame.right, frame.bottom, paintLine);
		canvas.drawLine(frame.right, frame.top-2, frame.right,frame.bottom+2, paintLine);
		//内外框中间部分
		paintLine.setColor(Color.rgb(179, 181, 183));
		paintLine.setStrokeWidth(12);
		paintLine.setAntiAlias(true);
		canvas.drawLine(frame.left+7, frame.top+7, frame.right-7, frame.top+7, paintLine);
		canvas.drawLine(frame.left+7, frame.top+2, frame.left+7, frame.bottom-7, paintLine);
		canvas.drawLine(frame.left+2, frame.bottom-7, frame.right-7, frame.bottom-7, paintLine);
		canvas.drawLine(frame.right-7, frame.top+2, frame.right-7,frame.bottom-2, paintLine);
		// 内框
		paintLine.setColor(Color.rgb(43, 171, 172));
		paintLine.setStrokeWidth(5);
		paintLine.setAntiAlias(true);
		canvas.drawLine(frame.left+15, frame.top+15, frame.right-15, frame.top+15, paintLine);
		canvas.drawLine(frame.left+15, frame.top+13, frame.left+15, frame.bottom-15, paintLine);
		canvas.drawLine(frame.left+13, frame.bottom-15, frame.right-15, frame.bottom-15, paintLine);
		canvas.drawLine(frame.right-15, frame.top+13, frame.right-15,frame.bottom-13, paintLine);


		if (frame == null) {
			return;
		}

	}

}
