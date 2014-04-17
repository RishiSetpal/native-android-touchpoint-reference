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

import android.content.Context;

import com.google.gson.annotations.SerializedName;
import com.optimusinfo.elasticpath.cortex.common.Constants;

/**
 * This class server as model for add to cart links
 * 
 * @author Optimus
 * 
 */
public class AddToCartModel {

	@SerializedName("links")
	protected AddToCartLinks[] mAddToCartLinks;

	public AddToCartLinks[] getAddToCartLinks() {
		return mAddToCartLinks;
	}

	public void setAddToCartLinks(AddToCartLinks[] mAddToCartLinks) {
		this.mAddToCartLinks = mAddToCartLinks;
	}

	/**
	 * Default constructor
	 */
	public AddToCartModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * This requests gets the add to cart form
	 * 
	 * @param current
	 * @param baseUrl
	 * @param zoomUrl
	 * @param accessToken
	 * @param mListner
	 */
	public static void getAddToCartForm(Context current, String addtoCartUrl,
			String accessToken, ListenerGetCartForm mListner) {
		try {
			new AsyncTaskGetCartForm(current, addtoCartUrl, accessToken,
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
	 * Model class for product links section
	 * 
	 * @author Optimus
	 * 
	 */
	public class AddToCartLinks {

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
