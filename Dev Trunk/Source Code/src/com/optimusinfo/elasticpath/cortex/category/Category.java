package com.optimusinfo.elasticpath.cortex.category;

import android.content.Context;

import com.google.gson.annotations.SerializedName;
import com.optimusinfo.elasticpath.cortex.common.Constants;

/**
 * JSON data representation for the response returned by the cortex Navigations
 * API
 * 
 * @author Optimus
 * 
 */
public class Category {

	@SerializedName("_element")
	protected CategoryElement[] element;

	/**
	 * Get the list of navigations by starting the task
	 */
	public static void getCategoriesFromServer(Context current,
			String urlEndPoint, String urlRoute, String urlScope,
			String urlZoom, String accessToken,
			ListenerGetCategories mCatListener) {
		try {
			AsyncTaskGetCategories taskGetNavigations = new AsyncTaskGetCategories(
					current, urlEndPoint.concat(urlRoute).concat(urlScope)
							.concat(urlZoom), accessToken,
					Constants.RequestHeaders.CONTENT_TYPE_STRING,
					Constants.RequestHeaders.CONTENT_TYPE,
					Constants.RequestHeaders.AUTHORIZATION_STRING,
					Constants.RequestHeaders.AUTHORIZATION_INITIALIZER,
					Category.class, mCatListener);
			taskGetNavigations.execute();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This class serves as models for
	 * 
	 * @author Optimus
	 * 
	 */
	public class CategoryElement {
		@SerializedName("name")
		protected String mDisplayName;

		@SerializedName("links")
		protected CategoryLink[] mLinks;

		public String getDisplayName() {
			return mDisplayName;
		}

		public void setDisplayName(String displayName) {
			this.mDisplayName = displayName;
		}

		public CategoryLink[] getLinks() {
			return mLinks;
		}

		public void setLinks(CategoryLink[] links) {
			this.mLinks = links;
		}
	}

	/**
	 * This class serves as model for Links received in navigations class
	 * 
	 * @author Optimus
	 * 
	 */
	public class CategoryLink {
		@SerializedName("href")
		protected String mItemsUrl;

		@SerializedName("rel")
		protected String mRelation;

		public String getItemsUrl() {
			return mItemsUrl;
		}

		public void setItemsUrl(String mItemsUrl) {
			this.mItemsUrl = mItemsUrl;
		}

		public String getRelation() {
			return mRelation;
		}

		public void setRelation(String mRelation) {
			this.mRelation = mRelation;
		}

	}
}
