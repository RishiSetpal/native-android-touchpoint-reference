package com.optimusinfo.elasticpath.cortex.cart;

import com.optimusinfo.elasticpath.cortexAPI.R;
import com.optimusinfo.elasticpath.cortex.authentication.AuthenticationActivity;
import com.optimusinfo.elasticpath.cortex.common.Constants;
import com.optimusinfo.elasticpath.cortex.common.NotificationUtils;
import com.optimusinfo.elasticpath.cortex.configuration.EPConfiguration;
import com.optimusinfo.elasticpath.cortex.configuration.EPCortex;
import com.optimusinfo.elasticpath.cortex.purchase.PurchaseActivity;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class CartActivity extends FragmentActivity {

	public static int REQUEST_CODE_AUTHENTICATION = 27;
	public static int RESULT_CODE_AUTHENTICATION_SUCESSFUL = 28;

	public static int REQUEST_CODE_PURCHASE = 29;
	public static int RESULT_CODE_PURCHASE_SUCESSFUL = 30;

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments representing each object in a collection. We use a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter} derivative,
	 * which will destroy and re-create fragments as needed, saving and
	 * restoring their state in the process. This is important to conserve
	 * memory and is a best practice when allowing navigation between objects in
	 * a potentially large collection.
	 */
	protected String mAddToCartUrl, mAccessToken, mProductQuantity,
			mPurchaseUrl;
	protected SharedPreferences mPreferences;

	// Request call listeners
	protected ListenerGetCartForm mGetCartFormListener;
	protected ListenerAddToCart mAddToCartListner;
	protected ListenerGetCompleteCartItems mCartItemsListener;
	protected CartModel mObjCart;

	// Page views
	protected Button mBTPurchase;
	protected ListView mLVCart;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

		setContentView(R.layout.activity_cart);

		// Initialize Preferences
		mPreferences = getSharedPreferences(
				Constants.Preferences.PREFERENCES_FILE_NAME,
				Context.MODE_PRIVATE);
		mAccessToken = mPreferences.getString(
				Constants.Preferences.KEY_ACCESS_TOKEN, "");

		// Get the cart values for current object
		mAddToCartUrl = getIntent().getStringExtra(
				Constants.PageUrl.INTENT_CART_URL);
		mProductQuantity = getIntent().getStringExtra(
				Constants.PageUrl.INTENT_PRODUCT_QUANT);

		// Set up action bar.
		final ActionBar actionBar = getActionBar();
		// Specify that the Home button should show an "Up" caret, indicating
		// that touching the
		// button will take the user one step up in the application's hierarchy.
		actionBar.setDisplayHomeAsUpEnabled(true);
		// Initialize the view elements
		initializeViews();
		// Perform the add to cart method
		getAddToCartForm();

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
	 * This function fires gets the Add to cart form
	 */
	private void getAddToCartForm() {

		mGetCartFormListener = new ListenerGetCartForm() {
			@Override
			public void onTaskSuccessful(final AddToCartModel objResponse) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						setProgressBarIndeterminateVisibility(false);
						if (objResponse.getAddToCartLinks() != null
								&& objResponse.getAddToCartLinks().length > 0) {
							postAddToCart(objResponse.getAddToCartLinks()[0].mHREF);
						} else {
							NotificationUtils.showErrorToast(
									getApplicationContext(),
									Constants.ErrorCodes.ERROR_SERVER);
						}
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

		setProgressBarIndeterminateVisibility(true);
		AddToCartModel.getAddToCartForm(getApplicationContext(), mAddToCartUrl,
				mAccessToken, mGetCartFormListener);
	}

	/**
	 * This function posts the add to cart object
	 * 
	 * @param mPostUrl
	 */
	public void postAddToCart(String mPostUrl) {

		mAddToCartListner = new ListenerAddToCart() {
			@Override
			public void onTaskSuccessful(final int response) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						setProgressBarIndeterminateVisibility(false);
						// get the complete cart list
						getCompleteCart();
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
		} else {
			quantiy = 1;
		}
		setProgressBarIndeterminateVisibility(true);
		CartModel.addToCart(quantiy, mPostUrl,
				Constants.Config.CONTENT_TYPE_ADD_TO_CART, mAccessToken,
				mAddToCartListner);
	}

	/**
	 * This function gets the complete items cart
	 */
	public void getCompleteCart() {

		mCartItemsListener = new ListenerGetCompleteCartItems() {
			@Override
			public void onTaskSuccessful(final CartModel response) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						setProgressBarIndeterminateVisibility(false);
						mPurchaseUrl = response.getOrderitems()[0]
								.getPurchaseForms()[0].getPurchaseLinks()
								.getHREF();
						// populate the list view
						setViewData(response);
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
		setProgressBarIndeterminateVisibility(true);
		EPCortex objCortexParams = EPConfiguration.getConfigurationParameters(
				getApplicationContext(), Constants.Config.FILE_NAME_CONFIG);

		String cartsUrl = objCortexParams.getEndpoint().concat("carts/")
				.concat(objCortexParams.getScope())
				.concat(Constants.ZoomUrl.URL_ZOOM_CART);
		CartModel.getCartItems(getApplicationContext(), cartsUrl, mAccessToken,
				mCartItemsListener);
	}

	/**
	 * 
	 * This function updates the views with data
	 * 
	 * @param objCart
	 *            - cart elements array
	 */
	private void setViewData(CartModel objCart) {
		mBTPurchase.setVisibility(View.VISIBLE);
		CartAdapter mListAdapter = new CartAdapter(getApplicationContext(),
				objCart.getLineItems()[0].getElements());
		mLVCart.setAdapter(mListAdapter);

	}

	/**
	 * This method initializes the page view elements
	 */
	private void initializeViews() {
		// Initialize the purchase button
		mBTPurchase = (Button) findViewById(R.id.btPurchase);
		mBTPurchase.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent mAuthIntent = new Intent(CartActivity.this,
						AuthenticationActivity.class);
				startActivityForResult(mAuthIntent, REQUEST_CODE_AUTHENTICATION);
			}
		});
		mLVCart = (ListView) findViewById(R.id.lvCartItems);
		mLVCart.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				NotificationUtils.showNotificationToast(
						getApplicationContext(), "Not supported currently");
				// TODO - For future reference
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		if (resultCode == RESULT_CODE_AUTHENTICATION_SUCESSFUL) {
			mAccessToken = intent.getExtras().getString(
					Constants.Preferences.KEY_ACCESS_TOKEN_USER);

			Intent mPurchIntent = new Intent(CartActivity.this,
					PurchaseActivity.class);
			mPurchIntent.putExtra(Constants.PageUrl.INTENT_PURCHASE_URL,
					mPurchaseUrl);
			startActivityForResult(mPurchIntent, REQUEST_CODE_PURCHASE);
		} else if (resultCode == RESULT_CODE_PURCHASE_SUCESSFUL) {
			finish();
		}
	}
}
