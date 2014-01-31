package com.optimusinfo.elasticpath.cortex.cart;

import java.lang.reflect.Array;

import com.google.gson.Gson;
import com.optimusinfo.elasticpath.cortexAPI.R;
import com.optimusinfo.elasticpath.cortex.authentication.AuthenticationActivity;
import com.optimusinfo.elasticpath.cortex.common.Constants;
import com.optimusinfo.elasticpath.cortex.common.NotificationUtils;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class CartActivity extends FragmentActivity {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments representing each object in a collection. We use a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter} derivative,
	 * which will destroy and re-create fragments as needed, saving and
	 * restoring their state in the process. This is important to conserve
	 * memory and is a best practice when allowing navigation between objects in
	 * a potentially large collection.
	 */
	protected String mAddToCartUrl, mAccessToken, mProductTitle, mProductPrice,
			mProductQuantity, mProductImage;
	protected int mLength;
	protected SharedPreferences mPreferences;
	protected ProductCartDetails[] mObjList;
	protected ListenerAddToCart mListener;
	protected Gson objGson;
	
	protected Button btPurchase;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

		setContentView(R.layout.activity_cart);

		// Initialize Preferences
		mPreferences = getSharedPreferences(
				Constants.Preferences.PREFERENCES_FILE_NAME,
				Context.MODE_PRIVATE);
		objGson = new Gson();
		btPurchase = (Button) findViewById(R.id.btPurchase);
		mAccessToken = mPreferences.getString(
				Constants.Preferences.KEY_ACCESS_TOKEN_USER, "");
		if(mAccessToken.equalsIgnoreCase("")){
			mAccessToken = mPreferences.getString(
					Constants.Preferences.KEY_ACCESS_TOKEN, "");
			btPurchase.setVisibility(View.VISIBLE);
		} else {
			btPurchase.setVisibility(View.INVISIBLE);
		}
				

		// Get the cart values for current object
		mAddToCartUrl = getIntent().getStringExtra(
				Constants.PageUrl.INTENT_CART_URL);
		mProductTitle = getIntent().getStringExtra(
				Constants.PageUrl.INTENT_PRODUCT_TITLE);
		mProductPrice = getIntent().getStringExtra(
				Constants.PageUrl.INTENT_PRODUCT_PRICE);
		mProductQuantity = getIntent().getStringExtra(
				Constants.PageUrl.INTENT_PRODUCT_QUANT);
		mProductImage = getIntent().getStringExtra(
				Constants.PageUrl.INTENT_PRODUCT_IMAGE);

		ProductCartDetails objCartDetails = new ProductCartDetails(
				mProductTitle, mProductImage, mAddToCartUrl, mProductQuantity,
				mProductPrice);
		// Initialize the cart list
		getCartList();
		mObjList = CartActivity.add(mObjList, objCartDetails);
		setCartList();

		// Set up action bar.
		final ActionBar actionBar = getActionBar();
		// Specify that the Home button should show an "Up" caret, indicating
		// that touching the
		// button will take the user one step up in the application's hierarchy.
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		// Perform the add to cart method
		addToCart();

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// Incase user presses back button from top navigation
			super.onBackPressed();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * This function fires the add to cart call
	 */
	private void addToCart() {

		mListener = new ListenerAddToCart() {

			@Override
			public void onTaskSuccessful(String response) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						setProgressBarIndeterminateVisibility(false);
						// Initialize views
						initializeViews();
					}
				});

			}

			@Override
			public void onTaskFailed(final int errorCode) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						setProgressBarIndeterminateVisibility(false);
						// TODO - For Future Req
						NotificationUtils.showErrorToast(
								getApplicationContext(), errorCode);
					}
				});
			}

			@Override
			public void onAuthenticationFailed() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						setProgressBarIndeterminateVisibility(false);
						// TODO For Future Req
					}
				});
			}
		};
		int quantiy = 0;
		if (mProductQuantity != null) {
			quantiy = Integer.parseInt(mProductQuantity);
		}
		setProgressBarIndeterminateVisibility(true);
		CartModel.addToCart(quantiy, mAddToCartUrl,
				Constants.Config.CONTENT_TYPE_ADD_TO_CART, mAccessToken,
				mListener);

	}

	/**
	 * This function updates the views with data
	 */
	private void initializeViews() {

		ListView lvCart = (ListView) findViewById(R.id.lvCartItems);
		final CartAdapter mListAdapter = new CartAdapter(
				getApplicationContext(), mObjList);
		lvCart.setAdapter(mListAdapter);

		lvCart.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				NotificationUtils.showNotificationToast(
						getApplicationContext(), "Not supported currently");
			}
		});
		
		
		btPurchase.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				Intent mAuthIntent = new Intent(CartActivity.this,
						AuthenticationActivity.class);
				startActivity(mAuthIntent);				
			}
		});
		

	}

	/**
	 * This class stores the cart items in shared preferences
	 * 
	 */
	private void setCartList() {

		mPreferences
				.edit()
				.putString(Constants.Preferences.LIST_CART,
						objGson.toJson(mObjList)).commit();

	}

	private void getCartList() {

		String listCart = mPreferences.getString(
				Constants.Preferences.LIST_CART, "");
		mObjList = objGson.fromJson(listCart, ProductCartDetails[].class);
	}
	
	

	@Override
	protected void onResume() {
		mAccessToken = mPreferences.getString(
				Constants.Preferences.KEY_ACCESS_TOKEN_USER, "");
		if(mAccessToken.equalsIgnoreCase("")){
			mAccessToken = mPreferences.getString(
					Constants.Preferences.KEY_ACCESS_TOKEN, "");
			btPurchase.setVisibility(View.VISIBLE);
		} else {
			btPurchase.setVisibility(View.INVISIBLE);
		}
		super.onResume();
	}



	class ProductCartDetails {

		protected String mProductTitle;
		protected String mProductImageUrl;
		protected String mProductAddToCart;
		protected String mProductQuantity;
		protected String mProductPrice;

		public ProductCartDetails(String mProductTitle,
				String mProductImageUrl, String mProductAddToCart,
				String mProductQuantity, String mProductPrice) {
			super();
			this.mProductTitle = mProductTitle;
			this.mProductImageUrl = mProductImageUrl;
			this.mProductAddToCart = mProductAddToCart;
			this.mProductQuantity = mProductQuantity;
			this.mProductPrice = mProductPrice;
		}

		public String getProductPrice() {
			return mProductPrice;
		}

		public void setProductPrice(String mProductPrice) {
			this.mProductPrice = mProductPrice;
		}

		public String getProductTitle() {
			return mProductTitle;
		}

		public void setProductTitle(String mProductTitle) {
			this.mProductTitle = mProductTitle;
		}

		public String getProductImageUrl() {
			return mProductImageUrl;
		}

		public void setProductImageUrl(String mProductImageUrl) {
			this.mProductImageUrl = mProductImageUrl;
		}

		public String getProductAddToCart() {
			return mProductAddToCart;
		}

		public void setProductAddToCart(String mProductAddToCart) {
			this.mProductAddToCart = mProductAddToCart;
		}

		public String getProductQuantity() {
			return mProductQuantity;
		}

		public void setProductQuantity(String mProductQuantity) {
			this.mProductQuantity = mProductQuantity;
		}
	}

	public static ProductCartDetails[] add(ProductCartDetails[] array,
			ProductCartDetails element) {
		final ProductCartDetails[] result;
		final int end;

		if (array != null) {
			end = array.length;
			result = (ProductCartDetails[]) Array.newInstance(
					ProductCartDetails.class, end + 1);
			System.arraycopy(array, 0, result, 0, end);
		} else {
			end = 0;
			result = (ProductCartDetails[]) Array.newInstance(
					ProductCartDetails.class, 1);
		}
		result[end] = element;
		return result;
	}
	
	
}
