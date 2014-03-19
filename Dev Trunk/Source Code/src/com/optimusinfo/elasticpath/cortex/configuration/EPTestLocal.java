package com.optimusinfo.elasticpath.cortex.configuration;

import java.io.IOException;

import android.content.Context;

import com.google.gson.Gson;
import com.optimusinfo.elasticpath.cortex.category.Category;
import com.optimusinfo.elasticpath.cortex.checkout.CheckoutModel;
import com.optimusinfo.elasticpath.cortex.common.Utils;
import com.optimusinfo.elasticpath.cortex.product.listing.ProductListing;

public class EPTestLocal {

	public static Category getCategories(Context mCurrent) {
		String data;
		try {
			data = Utils.getStringFromInputStream(mCurrent.getAssets().open(
					"category.json"));
			return new Gson().fromJson(data, Category.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static ProductListing getProductListing(Context mCurrent) {
		String data;
		try {
			data = Utils.getStringFromInputStream(mCurrent.getAssets().open(
					"itemlist.json"));
			return new Gson().fromJson(data, ProductListing.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static CheckoutModel getCheckout(Context mCurrent) {
		String data;
		try {
			data = Utils.getStringFromInputStream(mCurrent.getAssets().open(
					"checkout.json"));
			return new Gson().fromJson(data, CheckoutModel.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
