package com.optimusinfo.elasticpath.cortex.purchase;

import android.content.Context;

import com.google.gson.annotations.SerializedName;
import com.optimusinfo.elasticpath.cortex.common.Constants;

public class PurchaseFormModel {

	@SerializedName("links")
	protected PurchaseFormLinks[] mPurchaseLinks;

	public PurchaseFormLinks[] getPurchaseLinks() {
		return mPurchaseLinks;
	}

	public void setPurchaseLinks(PurchaseFormLinks[] mPurchaseLinks) {
		this.mPurchaseLinks = mPurchaseLinks;
	}

	/**
	 * 
	 * @param quanity
	 * @param urlAddToCartForm
	 * @param contentType
	 * @param token
	 */
	public PurchaseFormModel(int quanity, String urlAddToCartForm,
			String contentType, String token) {
	}

	/**
	 * 
	 * @param current
	 * @param purchaseUrl
	 * @param accessToken
	 * @param mListner
	 */
	public static void getPurchaseOrderForm(Context current, String purchaseUrl,
			String accessToken, ListenerGetPurchaseForm mListner) {
		try {
			new AsyncTaskGetPurchaseForm(current, purchaseUrl, accessToken,
					Constants.RequestHeaders.CONTENT_TYPE_STRING,
					Constants.RequestHeaders.CONTENT_TYPE,
					Constants.RequestHeaders.AUTHORIZATION_STRING,
					Constants.RequestHeaders.AUTHORIZATION_INITIALIZER,
					mListner).execute();
		} catch (NullPointerException e) {
			e.printStackTrace();

		}
	}

	/**
	 * Model class for purchase links section
	 * 
	 * @author Optimus
	 * 
	 */
	public class PurchaseFormLinks {

		@SerializedName("href")
		protected String mHREF;

		@SerializedName("rel")
		protected String mRelation;

		public String getHREF() {
			return mHREF;
		}

		public void setHREF(String mHREF) {
			this.mHREF = mHREF;
		}

		public String getRelation() {
			return mRelation;
		}

		public void setRelation(String mRelation) {
			this.mRelation = mRelation;
		}

	}

}
