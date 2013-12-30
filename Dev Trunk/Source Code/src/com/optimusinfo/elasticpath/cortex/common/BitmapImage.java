package com.optimusinfo.elasticpath.cortex.common;

import android.content.Context;
import android.graphics.Bitmap;

public class BitmapImage implements EPImage {
	private Bitmap bitmap;

	public BitmapImage(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	public Bitmap getBitmap(Context context) {
		return bitmap;
	}
}