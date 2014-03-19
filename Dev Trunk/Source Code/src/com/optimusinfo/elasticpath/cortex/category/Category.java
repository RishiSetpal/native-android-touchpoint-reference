package com.optimusinfo.elasticpath.cortex.category;

import android.content.Context;

import com.google.gson.annotations.SerializedName;

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
							.concat(urlZoom), accessToken, Category.class,
					mCatListener);
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

		@SerializedName("details")
		protected CategoryDetails[] mDetails;
		
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

		public CategoryDetails[] getDetails() {
			return mDetails;
		}

		public void setDetails(CategoryDetails[] mDetails) {
			this.mDetails = mDetails;
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

	/**
	 * This class serves as model for Links received in navigations class
	 * 
	 * @author Optimus
	 * 
	 */
	public class CategoryDetails {
		@SerializedName("display-value")
		protected String mItemDesc;

		public String getItemDesc() {
			return mItemDesc;
		}

		public void setItemDesc(String mItemDesc) {
			this.mItemDesc = mItemDesc;
		}		
	}

	/**
	 * This method returns the items url for Category
	 * 
	 * @param mObjElement
	 */
	public static String getItemsUrl(CategoryElement currElement) {
		if (currElement.getLinks() != null) {
			for (CategoryLink link : currElement.getLinks()) {
				if (link.getRelation() != null
						&& link.getRelation().length() != 0) {
					if (0 == link.getRelation().compareTo("items")) {
						return link.getItemsUrl();
					}
				}
			}
		}
		return "";

	}
}
