package com.douniao.music.Utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public final class LyricView extends View {

	public LyricView(Context context) {
		super(context);
	}

	// ��ʶ���
	private SongLyric lrc = null;
	// ��ǰ����ʱ��
	private long time = 0l;
	// ���廭��
	private Paint fontPaint = null;
	// ��ǰ������廭��
	private Paint lrcPaint = null;
	// ������ɫ
	private int fontColor = Color.GRAY;
	// ��ǰ���������ɫ
	private int lrcColor = Color.WHITE;
	// �����С
	private int fontSize = 12;

	/**
	 * ���ø�ʶ���
	 * 
	 * @param lrc
	 */
	public void setLyric(SongLyric lrc) {
		this.lrc = lrc;
	}

	/**
	 * ���õ�ǰʱ��
	 * 
	 * @param ms
	 */
	public void setTime(long ms) {
		this.time = ms;
	}

	/**
	 * ���ø��������ɫ
	 * 
	 * @param color
	 */
	public void setFontColor(int color) {
		this.fontColor = color;
	}

	/**
	 * ���õ�ǰ���������ɫ
	 * 
	 * @param color
	 */
	public void setLyricColor(int color) {
		this.lrcColor = color;
	}

	/**
	 * ���������С
	 * 
	 * @param size
	 */
	public void setFontSize(int size) {
		this.fontSize = size;
	}

	/**
	 * �ػ���ͼ
	 */
	@Override
	protected void onDraw(Canvas c) {
		super.onDraw(c);
		if (lrc != null) {
			try {
				if (fontPaint == null) {
					fontPaint = new Paint();
				}
				if (lrcPaint == null) {
					lrcPaint = new Paint();
				}
				fontPaint.setColor(fontColor);
				fontPaint.setTextSize(fontSize);
				lrcPaint.setColor(lrcColor);
				lrcPaint.setTextSize(fontSize);
				// ��ȡ��ǰҪ���Ÿ�ʵ�����
				int cIndex = lrc.getIndex(time);
				// ������Ƹ��y����
				int h = getHeight()
						/ 2
						- cIndex
						* fontSize
						* 3
						/ 2
						- (int) ((fontSize * 3 / 2) * (lrc.getOffset(time) / (float) lrc
								.getNextTime(time)));
				Long[] ts = lrc.getAllTimes();
				// ѭ������ÿһ�и�ʣ���ǰ���Ÿ���������
				for (Long l : ts) {
					c.drawText(lrc.get(l), 0, h,
							lrc.getIndex(l) == cIndex ? lrcPaint : fontPaint);
					h += fontSize * 3 / 2;
				}
			} catch (Exception e) {
			}
		}
	}

}