package com.optimusinfo.elasticpath.cortex.common;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.widget.ImageView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EPImageView extends ImageView {

	private static final int LOADING_THREADS = 4;
	private static ExecutorService threadPool = Executors
			.newFixedThreadPool(LOADING_THREADS);

	private EPImageTask currentTask;

	public EPImageView(Context context) {
		super(context);
	}

	public EPImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public EPImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	// Helpers to set image by URL
	public void setImageUrl(String url) {
		setImage(new WebImage(url));
	}

	public void setImageUrl(String url,
			EPImageTask.OnCompleteListener completeListener) {
		setImage(new WebImage(url), completeListener);
	}

	public void setImageUrl(String url, final Integer fallbackResource) {
		setImage(new WebImage(url), fallbackResource);
	}

	public void setImageUrl(String url, final Integer fallbackResource,
			EPImageTask.OnCompleteListener completeListener) {
		setImage(new WebImage(url), fallbackResource, completeListener);
	}

	public void setImageUrl(String url, final Integer fallbackResource,
			final Integer loadingResource) {
		setImage(new WebImage(url), fallbackResource, loadingResource);
	}

	public void setImageUrl(String url, final Integer fallbackResource,
			final Integer loadingResource,
			EPImageTask.OnCompleteListener completeListener) {
		setImage(new WebImage(url), fallbackResource, loadingResource,
				completeListener);
	}

	// Set image using SmartImage object
	public void setImage(final EPImage image) {
		setImage(image, null, null, null);
	}

	public void setImage(final EPImage image,
			final EPImageTask.OnCompleteListener completeListener) {
		setImage(image, null, null, completeListener);
	}

	public void setImage(final EPImage image, final Integer fallbackResource) {
		setImage(image, fallbackResource, fallbackResource, null);
	}

	public void setImage(final EPImage image, final Integer fallbackResource,
			EPImageTask.OnCompleteListener completeListener) {
		setImage(image, fallbackResource, fallbackResource, completeListener);
	}

	public void setImage(final EPImage image, final Integer fallbackResource,
			final Integer loadingResource) {
		setImage(image, fallbackResource, loadingResource, null);
	}

	public void setImage(final EPImage image, final Integer fallbackResource,
			final Integer loadingResource,
			final EPImageTask.OnCompleteListener completeListener) {
		// Set a loading resource
		if (loadingResource != null) {
			setImageResource(loadingResource);
		}

		// Cancel any existing tasks for this image view
		if (currentTask != null) {
			currentTask.cancel();
			currentTask = null;
		}

		// Set up the new task
		currentTask = new EPImageTask(getContext(), image);
		currentTask.setOnCompleteHandler(new EPImageTask.OnCompleteHandler() {
			@Override
			public void onComplete(Bitmap bitmap) {
				if (bitmap != null) {
					setImageBitmap(bitmap);
				} else {
					// Set fallback resource
					if (fallbackResource != null) {
						setImageResource(fallbackResource);
					}
				}

				if (completeListener != null) {
					completeListener.onComplete();
				}
			}
		});

		// Run the task in a threadpool
		threadPool.execute(currentTask);
	}

	public static void cancelAllTasks() {
		threadPool.shutdownNow();
		threadPool = Executors.newFixedThreadPool(LOADING_THREADS);
	}
}
