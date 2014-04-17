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
package com.optimusinfo.elasticpath.cortex.cart;

import java.io.Serializable;

import android.content.Context;

import com.google.gson.annotations.SerializedName;
import com.optimusinfo.elasticpath.cortex.common.Constants;

public class CartModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@SerializedName("_lineitems")
	protected CartLineItems[] mLineItems;

	@SerializedName("_order")
	protected CartOrder[] mOrderitems;

	@SerializedName("_total")
	protected CartTotal[] mCartTotal;

	@SerializedName("total-quantity")
	protected String mTotalQuantity;

	public CartLineItems[] getLineItems() {
		return mLineItems;
	}

	public CartTotal[] getCartTotal() {
		return mCartTotal;
	}

	public void setCartTotal(CartTotal[] mCartTotal) {
		this.mCartTotal = mCartTotal;
	}

	public void setLineItems(CartLineItems[] mLineItems) {
		this.mLineItems = mLineItems;
	}

	public CartOrder[] getOrderitems() {
		return mOrderitems;
	}

	public void setOrderitems(CartOrder[] mOrderitems) {
		this.mOrderitems = mOrderitems;
	}

	public String getTotalQuantity() {
		return mTotalQuantity;
	}

	public void setTotalQuantity(String mTotalQuantity) {
		this.mTotalQuantity = mTotalQuantity;
	}

	/**
	 * Initializes the variables and adds the quantity to the cart
	 * 
	 * @param quanity
	 *            the quantity to add
	 * @param urlAddToCartForm
	 *            the url of the form to post this add to cart request
	 * @param contentType
	 *            the content type of the request
	 * @param token
	 *            the token used for authentication
	 */
	public CartModel(int quanity, String urlAddToCartForm, String contentType,
			String token) {
	}

	/**
	 * 
	 * @param quanity
	 * @param urlAddToCartForm
	 * @param contentType
	 * @param accessToken
	 */
	public static void addToCart(int quanity, String urlAddToCartForm,
			String contentType, String accessToken, ListenerAddToCart listener) {
		try {
			AsyncTaskAddToCart taskAddToCart = new AsyncTaskAddToCart(
					urlAddToCartForm, quanity, urlAddToCartForm, accessToken,
					listener);
			taskAddToCart.execute();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	public static void getCartItems(Context current, String url, String token,
			ListenerGetCompleteCartItems listener) {
		try {
			AsyncTaskGetCompleteCart taskCompleteCart = new AsyncTaskGetCompleteCart(
					current, url, token,
					Constants.RequestHeaders.CONTENT_TYPE_STRING,
					Constants.RequestHeaders.CONTENT_TYPE,
					Constants.RequestHeaders.AUTHORIZATION_STRING,
					Constants.RequestHeaders.AUTHORIZATION_INITIALIZER,
					listener);
			taskCompleteCart.execute();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}

	}

	/**
	 * This method deletes an item from cart
	 * 
	 * @param current
	 * @param url
	 * @param token
	 * @param listener
	 */
	public static void deleteCartItems(Context current, String url,
			String token, ListenerDeleteCartItems listener) {
		try {
			AsyncTaskDeleteCartItem taskDelete = new AsyncTaskDeleteCartItem(
					current, url, token,
					Constants.RequestHeaders.CONTENT_TYPE_STRING,
					Constants.RequestHeaders.CONTENT_TYPE,
					Constants.RequestHeaders.AUTHORIZATION_STRING,
					Constants.RequestHeaders.AUTHORIZATION_INITIALIZER,
					listener);
			taskDelete.execute();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	public static void updateCartItems(Context current, String url,
			String token, ListenerUpdateCartItems listener, String quantiy) {
		try {
			AsyncTaskUpdateCartItem taskUpdate = new AsyncTaskUpdateCartItem(
					current, url, token,
					Constants.RequestHeaders.CONTENT_TYPE_STRING,
					Constants.RequestHeaders.CONTENT_TYPE,
					Constants.RequestHeaders.AUTHORIZATION_STRING,
					Constants.RequestHeaders.AUTHORIZATION_INITIALIZER,
					listener, quantiy);
			taskUpdate.execute();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}

	}

	public class CartLineItems implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		@SerializedName("_element")
		protected Element[] mElements;

		public Element[] getElements() {
			return mElements;
		}

		public void setElements(Element[] mElements) {
			this.mElements = mElements;
		}

	}

	public class Element implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@SerializedName("_item")
		protected Item[] mItems;

		@SerializedName("_price")
		protected ItemPrice[] mItemsPrice;

		@SerializedName("_rate")
		protected ItemRate[] mItemsRate;

		@SerializedName("quantity")
		protected String mQuantity;

		@SerializedName("self")
		protected DeleteLink mLink;

		public String getQuantity() {
			return mQuantity;
		}

		public void setQuantity(String mQuantity) {
			this.mQuantity = mQuantity;
		}

		public ItemRate[] getItemsRate() {
			return mItemsRate;
		}

		public void setItemsRate(ItemRate[] mItemsRate) {
			this.mItemsRate = mItemsRate;
		}

		public Item[] getItems() {
			return mItems;
		}

		public void setItems(Item[] mItems) {
			this.mItems = mItems;
		}

		public ItemPrice[] getItemsPrice() {
			return mItemsPrice;
		}

		public void setItemsPrice(ItemPrice[] mItemsPrice) {
			this.mItemsPrice = mItemsPrice;
		}

		public class DeleteLink implements Serializable {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			@SerializedName("href")
			String mHref;
		}

	}

	/**
	 * Model class to get the Item details
	 * 
	 * @author Optimus
	 * 
	 */
	public class Item implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@SerializedName("_definition")
		protected ItemDefinition[] mDefinitions;

		@SerializedName("_price")
		protected ItemPrice[] mUnitPrice;

		@SerializedName("_rate")
		protected ItemRate[] mUnitRate;

		public ItemDefinition[] getDefinitions() {
			return mDefinitions;
		}

		public void setDefinitions(ItemDefinition[] mDefinitions) {
			this.mDefinitions = mDefinitions;
		}

	}

	/**
	 * Model class to get the item definitions
	 * 
	 * @author Optimus
	 * 
	 */
	public class ItemDefinition implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@SerializedName("display-name")
		protected String mDisplayName;

		@SerializedName("_assets")
		protected ItemAssets[] mItemAssets;

		public String getDisplayName() {
			return mDisplayName;
		}

		public void setDisplayName(String mDisplayName) {
			this.mDisplayName = mDisplayName;
		}

		public ItemAssets[] getItemAssets() {
			return mItemAssets;
		}

		public void setItemAssets(ItemAssets[] mItemAssets) {
			this.mItemAssets = mItemAssets;
		}
	}

	/**
	 * Model class to get the item assets
	 * 
	 * @author Optimus
	 * 
	 */
	public class ItemAssets implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		@SerializedName("_element")
		protected AssetElements[] mAssetsElements;

		public AssetElements[] getAssetsElements() {
			return mAssetsElements;
		}

		public void setAssetsElements(AssetElements[] mAssetsElements) {
			this.mAssetsElements = mAssetsElements;
		}
	}

	/**
	 * Model class to get the image url
	 * 
	 * @author Optimus
	 * 
	 */
	public class AssetElements implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		@SerializedName("content-location")
		protected String mImageUrl;

		public String getImageUrl() {
			return mImageUrl;
		}

		public void setImageUrl(String mImageUrl) {
			this.mImageUrl = mImageUrl;
		}
	}

	/**
	 * Model class to get the items price section
	 * 
	 * @author Optimus
	 * 
	 */

	public class ItemPrice implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		@SerializedName("purchase-price")
		protected PriceDetails[] mProductPrices;

		public PriceDetails[] getProductPrices() {
			return mProductPrices;
		}

		public void setProductPrices(PriceDetails[] mProductPrices) {
			this.mProductPrices = mProductPrices;
		}

	}

	/**
	 * Model class to get the items price section
	 * 
	 * @author Optimus
	 * 
	 */

	public class ItemRate implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		@SerializedName("rate")
		protected PriceDetails[] mProductPrices;

		public PriceDetails[] getProductPrices() {
			return mProductPrices;
		}

		public void setProductPrices(PriceDetails[] mProductPrices) {
			this.mProductPrices = mProductPrices;
		}

	}

	/**
	 * Model class to get the product price
	 * 
	 * @author Optimus
	 * 
	 */
	public class PriceDetails implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		@SerializedName("display")
		protected String mDisplayPrice;

		public String getDisplayPrice() {
			return mDisplayPrice;
		}

		public void setDisplayPrice(String mDisplayPrice) {
			this.mDisplayPrice = mDisplayPrice;
		}

	}

	/**
	 * Model class to get the purchase order items
	 * 
	 * @author Optimus
	 * 
	 */
	public class CartOrder implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@SerializedName("_purchaseform")
		protected PurchaseForm[] mPurchaseForms;

		@SerializedName("self")
		protected CheckOut mSelf;

		public PurchaseForm[] getPurchaseForms() {
			return mPurchaseForms;
		}

		public void setPurchaseForms(PurchaseForm[] mPurchaseForms) {
			this.mPurchaseForms = mPurchaseForms;
		}

		public CheckOut getSelf() {
			return mSelf;
		}

		public void setSelf(CheckOut mSelf) {
			this.mSelf = mSelf;
		}
	}

	public class CartTotal implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		@SerializedName("cost")
		public Cost[] mCosts;

		public Cost[] getCosts() {
			return mCosts;
		}

		public void setCosts(Cost[] mCosts) {
			this.mCosts = mCosts;
		}
	}

	public class Cost implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		@SerializedName("display")
		protected String mTotalCost;

		public String getTotalCost() {
			return mTotalCost;
		}

		public void setTotalCost(String mTotalCost) {
			this.mTotalCost = mTotalCost;
		}
	}

	public class CheckOut implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		@SerializedName("href")
		protected String mCheckOutLink;

		public String getCheckOutLink() {
			return mCheckOutLink;
		}

		public void setCheckOutLink(String mCheckOutLink) {
			this.mCheckOutLink = mCheckOutLink;
		}

	}

	/**
	 * Model class to get the Purchase Forms
	 * 
	 * @author Optimus
	 * 
	 */
	public class PurchaseForm implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		@SerializedName("self")
		protected PurchaseLinks mPurchaseLinks;

		public PurchaseLinks getPurchaseLinks() {
			return mPurchaseLinks;
		}

		public void setPurchaseLinks(PurchaseLinks mPurchaseLinks) {
			this.mPurchaseLinks = mPurchaseLinks;
		}

	}

	/**
	 * Model class to get the Purchase links
	 * 
	 * @author Optimus
	 * 
	 */
	public class PurchaseLinks implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		@SerializedName("href")
		protected String mHREF;

		public String getHREF() {
			return mHREF;
		}

		public void setHREF(String mHREF) {
			this.mHREF = mHREF;
		}

	}
}
