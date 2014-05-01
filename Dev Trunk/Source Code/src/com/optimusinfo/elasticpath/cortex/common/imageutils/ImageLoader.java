/*
 * Copyright © 2014 Elastic Path Software Inc. All rights reserved.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * 
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.optimusinfo.elasticpath.cortex.common.imageutils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.optimusinfo.elasticpath.cortex.common.Utils;
import com.optimusinfo.elasticpath.cortexAPI.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

/**
 * ImageLoader is image manager for loading images
 * 
 * @author optimus
 * 
 */
public class ImageLoader {

	public static final long IMAGE_SIZE_LIMIT = 1 * 32 * 1024;

	// Memory cache
	MemoryCache memoryCache = new MemoryCache();

	// File cache
	static FileCache fileCache;

	// ImageView map
	private Map<ImageView, String> imageViews = Collections
			.synchronizedMap(new WeakHashMap<ImageView, String>());

	// Executor service
	ExecutorService executorService;

	Context currentContext;

	// Initial picture to be displayed while loading the image
	final int stub_id = R.drawable.img_placeholder;	

	private boolean showProgressAnimation;

	// Constructor
	public ImageLoader(Context context) {
		currentContext = context;
		fileCache = new FileCache(context);
		executorService = Executors.newFixedThreadPool(5);
		showProgressAnimation = true;		
	}

	

	/**
	 * Display the image
	 * 
	 * @param url
	 *            URL of the image
	 * @param imageView
	 *            image view in which h=this image is displayed
	 */
	public void displayImage(String url, ImageView imageView) {
		// Set the default ScaleType to FIT_CENTER
		displayImage(url, imageView, ScaleType.CENTER_INSIDE);
	}

	//
	/**
	 * Display the image
	 * 
	 * @param url
	 *            URL of the image
	 * @param image
	 *            view in which h=this image is displayed
	 * @param scaleType
	 *            value of the scale to which this image should be scled
	 */
	public void displayImage(String url, ImageView imageView,
			ScaleType scaleType) {
		imageViews.put(imageView, url);
		Bitmap bitmap = memoryCache.get(url);

		if (bitmap != null) {
			// Stop the animation and set the image
			imageView.clearAnimation();
			if (bitmap.getHeight() >= 300 && bitmap.getWidth() <= 375) {
				imageView.setScaleType(ScaleType.CENTER_INSIDE);
			} else {
				imageView.setScaleType(scaleType);
			}
			imageView.setImageBitmap(bitmap);

		} else {
			if (Utils.isNetworkAvailable((Activity) currentContext)) {
				queuePhoto(url, imageView, scaleType);

				if (showProgressAnimation) {
					// Set the default loading image and start the animation
					imageView.setImageResource(stub_id);
					imageView.setScaleType(ScaleType.CENTER_INSIDE);
				}
			}
		}
	}

	/**
	 * Display the image with rounded corners
	 * 
	 * @param url
	 *            URL of the image
	 * @param imageView
	 *            view in which h=this image is displayed
	 * @param isRoundedCorner
	 *            flag to tell whether the image has to be displayed with
	 *            rounded corner
	 */
	public void displayRoundedCornersImage(String url, ImageView imageView,
			boolean isRoundedCorner) {
		// Set the default ScaleType to FIT_CENTER
		displayRoundedCornersImage(url, imageView, ScaleType.CENTER_INSIDE,
				isRoundedCorner);
	}

	/**
	 * Display the image with rounded corners
	 * 
	 * @param url
	 *            URL of the image
	 * @param imageView
	 *            view in which h=this image is displayed
	 * @param scaleType
	 *            the value of scale to which this image should be scaled
	 * @param isRoundedCorner
	 *            flag to tell whether the image has to be displayed with
	 *            rounded corner
	 */
	public void displayRoundedCornersImage(String url, ImageView imageView,
			ScaleType scaleType, boolean isRoundedCorner) {
		imageViews.put(imageView, url);
		Bitmap bitmap = memoryCache.get(url);
		if (bitmap != null) {
			// Stop the animation and set the image
			imageView.clearAnimation();
			if (isRoundedCorner) {
				imageView.setImageBitmap(getRoundedCornerBitmap(bitmap));
			} else {
				imageView.setImageBitmap(bitmap);
			}
			if (bitmap.getHeight() >= 300 && bitmap.getWidth() <= 375) {
				imageView.setScaleType(ScaleType.CENTER_INSIDE);
			} else {
				imageView.setScaleType(scaleType);
			}
		} else {
			queuePhoto(url, imageView, scaleType);

			if (showProgressAnimation) {
				// Set the default loading image and start the animation
				imageView.setImageResource(stub_id);
				imageView.setScaleType(ScaleType.CENTER_INSIDE);
			}
		}
	}

	/**
	 * Display the image with rounded corners
	 * 
	 * @param url
	 *            URL of the image
	 * @param imageView
	 *            view in which h=this image is displayed
	 * @param scaleType
	 *            the value of scale to which this image should be scaled
	 * @param isRoundedCorner
	 *            flag to tell whether the image has to be displayed with
	 *            rounded corner
	 * @param showProgressAnimation
	 *            flag to tell whether the progress animation is to be displayed
	 *            as a placeholder
	 */
	public void displayRoundedCornersImage(String url, ImageView imageView,
			ScaleType scaleType, boolean isRoundedCorner,
			boolean showProgressAnimation, boolean save) {
		this.showProgressAnimation = showProgressAnimation;
		imageViews.put(imageView, url);
		Bitmap bitmap = memoryCache.get(url);
		if (bitmap != null) {
			// Stop the animation and set the image
			imageView.clearAnimation();
			if (isRoundedCorner) {
				imageView.setImageBitmap(getRoundedCornerBitmap(bitmap));
			} else {
				imageView.setImageBitmap(bitmap);
			}

		} else {
			queuePhoto(url, imageView, scaleType, save);

			// Set the default loading image and start the animation
			if (showProgressAnimation) {
				imageView.setImageResource(stub_id);
				imageView.setScaleType(ScaleType.CENTER_INSIDE);
			}
		}
	}

	/**
	 * Create a bitmap with rounded corners
	 * 
	 * @param bitmap
	 *            the bitmap object which has to be rounded F
	 * @return
	 */
	private Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		int pixels = 5;
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = pixels;

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}

	// Display the image with rounded corners
	public void displayRoundedCornersImage(String url, ImageView imageView,
			boolean isRoundedCorner, ScaleType argScaleType) {
		// Set the default ScaleType to FIT_CENTER
		displayRoundedCornersImage(url, imageView, argScaleType,
				isRoundedCorner);
	}

	/**
	 * Queue photo to the list
	 * 
	 * @param url
	 *            the URL of the image
	 * @param imageView
	 *            the view in which the image has to be displayed
	 * @param scaleType
	 *            the value of the scale to which the image has to be scaled
	 */
	private void queuePhoto(String url, ImageView imageView, ScaleType scaleType) {
		PhotoToLoad p = new PhotoToLoad(url, imageView, scaleType);
		executorService.submit(new PhotosLoader(p));
	}

	private void queuePhoto(String url, ImageView imageView,
			ScaleType scaleType, boolean save) {
		PhotoToLoad p = new PhotoToLoad(url, imageView, scaleType, save);
		executorService.submit(new PhotosLoader(p));
	}

	/**
	 * Retrieve the bitmap from the URL
	 * 
	 * @param url
	 *            the URL of the image
	 * @return the bitmap from the URL
	 * @throws NullPointerException
	 * @throws IOException
	 */
	public static Bitmap getBitmap(String url) throws NullPointerException,
			IOException {
		File f = fileCache.getFile(url);

		// from SD cache
		Bitmap b = decodeBitmapFile(f);
		if (b != null) {
			return b;
		}

		Bitmap bitmap = null;
		URL imageUrl = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
		conn.setConnectTimeout(30000);
		conn.setReadTimeout(30000);
		conn.setInstanceFollowRedirects(true);
		InputStream is = conn.getInputStream();
		OutputStream os = new FileOutputStream(f);
		Utils.CopyStream(is, os);
		os.close();
		bitmap = decodeBitmapFile(f);
		return bitmap;

	}

	/**
	 * Get a bitmap from the image path
	 * 
	 * @param imagePath
	 * 
	 * @return bitmap or null if read fails
	 */
	public static Bitmap decodeBitmapFile(File f) throws FileNotFoundException,
			NullPointerException {

		Double fileSize = (double) f.length() / (double) IMAGE_SIZE_LIMIT;
		int scale = 1;
		// decode image size
		if (0 == fileSize) {
			return null;
		}
		if (fileSize > 1) {
			scale = (int) Math.ceil(fileSize);
		} else {
			return BitmapFactory.decodeStream(new FileInputStream(f));
		}
		// decode with inSampleSize
		BitmapFactory.Options o2 = new BitmapFactory.Options();
		o2.inSampleSize = scale;
		return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
	}

	/**
	 * PhotoToLoad class handles the task for the queFue
	 */
	private class PhotoToLoad {
		public String url;
		public ImageView imageView;
		public ScaleType scaleType;
		public Boolean save;

		public PhotoToLoad(String u, ImageView i, ScaleType scaleType) {
			url = u;
			imageView = i;
			this.scaleType = scaleType;
			save = true;
		}

		public PhotoToLoad(String u, ImageView i, ScaleType scaleType,
				boolean save) {
			url = u;
			imageView = i;
			this.scaleType = scaleType;
			this.save = save;
		}
	}

	/**
	 * PhotosLoader Class that loads photos
	 */
	class PhotosLoader implements Runnable {
		PhotoToLoad photoToLoad;

		PhotosLoader(PhotoToLoad photoToLoad) {
			this.photoToLoad = photoToLoad;
		}

		public void run() {
			if (imageViewReused(photoToLoad))
				return;
			Bitmap bmp = null;
			try {
				bmp = getBitmap(photoToLoad.url);
			} catch (NullPointerException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (photoToLoad.save)
				memoryCache.put(photoToLoad.url, bmp);
			if (imageViewReused(photoToLoad))
				return;
			BitmapDisplayer bd = new BitmapDisplayer(bmp, photoToLoad);
			Activity a = (Activity) photoToLoad.imageView.getContext();
			a.runOnUiThread(bd);
		}
	}

	/**
	 * Check if imageView is reused
	 * 
	 * @param photoToLoad
	 *            object containing the image view
	 * @return true if image view is reused otherwise it returns false
	 */
	boolean imageViewReused(PhotoToLoad photoToLoad) {
		String tag = imageViews.get(photoToLoad.imageView);
		if (tag == null || !tag.equals(photoToLoad.url))
			return true;
		return false;
	}

	/**
	 * BitmapDisplayer class used to display bitmap in the UI thread
	 * 
	 */
	class BitmapDisplayer implements Runnable {
		Bitmap bitmap;
		PhotoToLoad photoToLoad;

		public BitmapDisplayer(Bitmap b, PhotoToLoad p) {
			bitmap = b;
			photoToLoad = p;
		}

		public void run() {
			if (imageViewReused(photoToLoad))
				return;
			if (bitmap != null) {
				// Stop the animation and set the image
				photoToLoad.imageView.setImageBitmap(bitmap);
				photoToLoad.imageView.clearAnimation();
				if (bitmap.getHeight() >= 300 && bitmap.getWidth() <= 375) {
					photoToLoad.imageView.setScaleType(ScaleType.CENTER_INSIDE);
				} else {
					photoToLoad.imageView.setScaleType(photoToLoad.scaleType);
				}
			} else {
				if (showProgressAnimation) {

					// Set the default loading image and start the animation
					photoToLoad.imageView.setImageResource(stub_id);
					photoToLoad.imageView.setScaleType(ScaleType.CENTER_INSIDE);
				}
			}
		}
	}

	/**
	 * Clear caches
	 */
	public void clearCache() {
		memoryCache.clear();
		fileCache.clear();
	}

}