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
package com.optimusinfo.elasticpath.cortex.product.listing;

import android.content.Context;

import com.google.gson.annotations.SerializedName;
import com.optimusinfo.elasticpath.cortex.common.Constants;

/**
 * Model class for product listings
 * 
 * @author Optimus
 * 
 */
public class ProductListing {

	@SerializedName("_element")
	protected ProductElement[] mElements;

	@SerializedName("pagination")
	public Pagination Pagination;

	public ProductElement[] getElements() {
		return mElements;
	}

	public void setElements(ProductElement[] mElements) {
		this.mElements = mElements;
	}

	/**
	 * Gets the product list
	 * 
	 * @param current
	 * @param baseUrl
	 * @param pageUrl
	 * @param pageNumber
	 * @param zoomUrl
	 * @param accessToken
	 * @param mProdListener
	 */
	public static void getProuctListingFromServer(Context current,
			String baseUrl, String pageUrl, String pageNumber, String zoomUrl,
			String accessToken, ListenerGetProductListings mProdListener) {
		try {
			new AsyncTaskGetProductListing(current, baseUrl.concat(pageUrl)
					.concat(pageNumber).concat(zoomUrl), accessToken,
					Constants.RequestHeaders.CONTENT_TYPE_STRING,
					Constants.RequestHeaders.CONTENT_TYPE,
					Constants.RequestHeaders.AUTHORIZATION_STRING,
					Constants.RequestHeaders.AUTHORIZATION_INITIALIZER,
					mProdListener).execute();
		} catch (NullPointerException e) {
			e.printStackTrace();

		}
	}

	public class Pagination {
		@SerializedName("current")
		public String CurrentPage;

		@SerializedName("pages")
		public String TotalPages;

		@SerializedName("results")
		public String TotalResults;
	}

	/**
	 * Model class for product element
	 * 
	 * @author Optimus
	 * 
	 */
	public class ProductElement {

		@SerializedName("_addtocartform")
		protected ProductAddToCartForm[] mAddToCartForm;

		@SerializedName("_definition")
		protected ProductDefinition[] mDefinition;

		@SerializedName("_availability")
		protected ProductAvailability[] mAvailability;

		@SerializedName("_price")
		protected ProductPrice[] mPrice;

		@SerializedName("_rate")
		protected ProductRates[] mRates;

		@SerializedName("self")
		protected ProductDetailsLink mSelf;

		public ProductDetailsLink getSelf() {
			return mSelf;
		}

		public void setSelf(ProductDetailsLink mSelf) {
			this.mSelf = mSelf;
		}

		public ProductRates[] getRates() {
			return mRates;
		}

		public void setRates(ProductRates[] mRates) {
			this.mRates = mRates;
		}

		public ProductAddToCartForm[] getAddToCartForm() {
			return mAddToCartForm;
		}

		public void setAddToCartForm(ProductAddToCartForm[] mAddToCartForm) {
			this.mAddToCartForm = mAddToCartForm;
		}

		public ProductDefinition[] getDefinition() {
			return mDefinition;
		}

		public void setDefinition(ProductDefinition[] mDefinition) {
			this.mDefinition = mDefinition;
		}

		public ProductAvailability[] getAvailability() {
			return mAvailability;
		}

		public void setAvailability(ProductAvailability[] mAvailability) {
			this.mAvailability = mAvailability;
		}

		public ProductPrice[] getPrice() {
			return mPrice;
		}

		public void setPrice(ProductPrice[] mPrice) {
			this.mPrice = mPrice;
		}
	}

	/**
	 * Model class to get product details link
	 * 
	 * @author Optimus
	 * 
	 */
	public class ProductDetailsLink {
		@SerializedName("href")
		protected String mHref;

		public String getHref() {
			return mHref;
		}

		public void setHref(String mHref) {
			this.mHref = mHref;
		}
	}

	/**
	 * Model class for add to cart section
	 * 
	 * @author Optimus
	 * 
	 */
	public class ProductAddToCartForm {
		@SerializedName("self")
		protected ProductLinks mProductLinks;

		public ProductLinks getProductLinks() {
			return mProductLinks;
		}

		public void setProductLinks(ProductLinks mProductLinks) {
			this.mProductLinks = mProductLinks;
		}
	}

	/**
	 * Model class for product links section
	 * 
	 * @author Optimus
	 * 
	 */
	public class ProductLinks {

		@SerializedName("href")
		protected String mHREF;

		public String getHREF() {
			return mHREF;
		}

		public void setHREF(String mHREF) {
			this.mHREF = mHREF;
		}
	}

	/**
	 * Model Class for product definition section
	 * 
	 * @author Optimus
	 * 
	 */
	public class ProductDefinition {
		@SerializedName("display-name")
		protected String mDisplayName;

		@SerializedName("_assets")
		protected ProductAssets[] mProductAssets;

		public String getDisplayName() {
			return mDisplayName;
		}

		public void setDisplayName(String mDisplayName) {
			this.mDisplayName = mDisplayName;
		}

		public ProductAssets[] getProductAssets() {
			return mProductAssets;
		}

		public void setProductAssets(ProductAssets[] mProductAssets) {
			this.mProductAssets = mProductAssets;
		}
	}

	/**
	 * Model class for product available section
	 * 
	 * @author Optimus
	 * 
	 */
	public class ProductAvailability {
		@SerializedName("state")
		protected String mState;

		public String getState() {
			return mState;
		}

		public void setState(String mState) {
			this.mState = mState;
		}
	}

	/**
	 * Model class for product price section
	 * 
	 * @author Optimus
	 * 
	 */
	public class ProductPrice {

		@SerializedName("purchase-price")
		protected ProductPurchasePrice[] mProductPrice;

		public ProductPurchasePrice[] getProductPrice() {
			return mProductPrice;
		}

		public void setProductPrice(ProductPurchasePrice[] mProductPrice) {
			this.mProductPrice = mProductPrice;
		}
	}

	/**
	 * Model class for product purchase section
	 * 
	 * @author Optimus
	 * 
	 */
	public class ProductPurchasePrice {
		@SerializedName("amount")
		protected String mAmount;

		@SerializedName("currency")
		protected String mCurrency;

		@SerializedName("display")
		protected String mDisplay;

		public String getAmount() {
			return mAmount;
		}

		public void setAmount(String mAmount) {
			this.mAmount = mAmount;
		}

		public String getCurrency() {
			return mCurrency;
		}

		public void setCurrency(String mCurrency) {
			this.mCurrency = mCurrency;
		}

		public String getDisplay() {
			return mDisplay;
		}

		public void setDisplay(String mDisplay) {
			this.mDisplay = mDisplay;
		}
	}

	/**
	 * Model Class for Product Rates section
	 * 
	 * @author Optimus
	 * 
	 */
	public class ProductRates {
		@SerializedName("rate")
		protected ProductRatePrice[] mProductRates;

		public ProductRatePrice[] getProductRates() {
			return mProductRates;
		}

		public void setProductRates(ProductRatePrice[] mProductRates) {
			this.mProductRates = mProductRates;
		}

	}

	/**
	 * Model class for product rate
	 * 
	 * @author Optimus
	 * 
	 */
	public class ProductRatePrice {
		@SerializedName("display")
		protected String mRate;

		public String getRate() {
			return mRate;
		}

		public void setRate(String mRate) {
			this.mRate = mRate;
		}

	}

	/**
	 * Model class for Product Assets section
	 * 
	 * @author Optimus
	 * 
	 */
	public class ProductAssets {
		@SerializedName("_element")
		protected ProductImage[] mProductImages;

		public ProductImage[] getProductImages() {
			return mProductImages;
		}

		public void setProductImages(ProductImage[] mProductImages) {
			this.mProductImages = mProductImages;
		}

	}

	/**
	 * Model class for Product Image section
	 * 
	 * @author Optimus
	 * 
	 */
	public class ProductImage {
		@SerializedName("content-location")
		protected String mImageUrl;

		public String getImageUrl() {
			return mImageUrl;
		}

		public void setImageUrl(String mImageUrl) {
			this.mImageUrl = mImageUrl;
		}

	}

}
