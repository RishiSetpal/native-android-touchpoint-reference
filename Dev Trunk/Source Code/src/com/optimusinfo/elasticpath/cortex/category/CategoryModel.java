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
public class CategoryModel {

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
							.concat(urlZoom), accessToken, CategoryModel.class,
					mCatListener);
			taskGetNavigations.execute();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Get the list of child by starting the task
	 */
	public static void getChildCategoriesFromServer(Context current,
			String urlCategory, String accessToken,
			ListenerGetCategories mCatListener) {
		try {
			AsyncTaskGetCategories taskGetNavigations = new AsyncTaskGetCategories(
					current, urlCategory, accessToken, CategoryElement.class,
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

		@SerializedName("value")
		protected String mItemDesc;

		@SerializedName("name")
		protected String mItemType;

		public String getItemType() {
			return mItemType;
		}

		public void setItemType(String itemType) {
			this.mItemType = itemType;
		}

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

	/**
	 * This method returns the child category url for the current Category
	 * 
	 * @param mObjElement
	 */
	public static String getChildCategoriesUrl(CategoryElement currElement) {
		if (currElement.getLinks() != null) {
			for (CategoryLink link : currElement.getLinks()) {
				if (link.getRelation() != null
						&& link.getRelation().length() != 0) {
					if (0 == link.getRelation().compareTo("child")) {
						return link.getItemsUrl();
					}
				}
			}
		}
		return "";
	}

	/**
	 * This method returns the category image
	 * 
	 * @param mObjElement
	 */
	public static String getCategoryImage(CategoryElement currElement) {
		if (currElement.getDetails() != null) {
			for (CategoryDetails detail : currElement.getDetails()) {
				if (detail.getItemType() != null
						&& detail.getItemType().length() != 0) {
					if (0 == detail.getItemType().compareTo("CategoryImageURL")) {
						return detail.getItemDesc();
					}
				}
			}
		}
		return "";
	}

	/**
	 * This method returns the category description
	 * 
	 * @param mObjElement
	 */
	public static String getCategoryDesc(CategoryElement currElement) {
		if (currElement.getDetails() != null) {
			for (CategoryDetails detail : currElement.getDetails()) {
				if (detail.getItemType() != null
						&& detail.getItemType().length() != 0) {
					if (0 == detail.getItemType().compareTo("catDescription")) {
						return detail.getItemDesc();
					}
				}
			}
		}
		return "";
	}

}
