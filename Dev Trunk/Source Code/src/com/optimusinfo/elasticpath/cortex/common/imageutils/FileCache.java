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

import android.content.Context;

public class FileCache {
	// Cache directory
	private File cacheDir;

	/**
	 * Initialize the cache
	 * 
	 * @param context
	 *            context of the activity
	 */
	public FileCache(Context context) {
		// Find the dir to save cached images
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			cacheDir = context.getCacheDir();
		} else {
			cacheDir = context.getCacheDir();
		}

		// Create a cache if it does not exist
		if (!cacheDir.exists()) {
			cacheDir.mkdirs();
		}
	}

	/**
	 * Get file from the URL
	 * 
	 * @param url
	 *            the URL of the image
	 * @return the FileObject for this image
	 */
	public File getFile(String url) {
		if (url == null) {
			return null;
		}

		String filename = String.valueOf(url.hashCode());
		File f = null;
		try {
			f = new File(cacheDir, filename);
		} catch (NullPointerException e) {
			return null;
		}
		return f;

	}

	/**
	 * Clears the cache
	 */
	public void clear() {
		File[] files = cacheDir.listFiles();

		if (files == null) {
			return;
		}

		for (File f : files) {
			f.delete();
		}
	}
}
