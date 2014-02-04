package com.optimusinfo.elasticpath.cortex.purchase;

import com.optimusinfo.elasticpath.cortexAPI.R;
import com.optimusinfo.elasticpath.cortex.cart.CartActivity;
import com.optimusinfo.elasticpath.cortex.cart.CartModel;
import com.optimusinfo.elasticpath.cortex.cart.ListenerGetCompleteCartItems;
import com.optimusinfo.elasticpath.cortex.common.Constants;
import com.optimusinfo.elasticpath.cortex.common.NotificationUtils;
import com.optimusinfo.elasticpath.cortex.configuration.EPConfiguration;
import com.optimusinfo.elasticpath.cortex.configuration.EPCortex;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ListView;

public class PurchaseActivity extends FragmentActivity {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments representing each object in a collection. We use a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter} derivative,
	 * which will destroy and re-create fragments as needed, saving and
	 * restoring their state in the process. This is important to conserve
	 * memory and is a best practice when allowing navigation between objects in
	 * a potentially large collection.
	 */
	protected String mPurchaseUrl, mAccessToken;
	protected SharedPreferences mPreferences;

	protected ListenerGetPurchaseForm mGetPurchaseFormListener;
	protected ListenerGetCompleteCartItems mCartItemsListener;
	protected ListenerCompletePurchaseOrder mPostPurchaseListner;

	protected boolean mIsOrderConfirmed = false;

	protected ListView mLVCart;

	protected CartModel mobjCart;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

		setContentView(R.layout.activity_purchase);

		// Initialize Preferences
		mPreferences = getSharedPreferences(
				Constants.Preferences.PREFERENCES_FILE_NAME,
				Context.MODE_PRIVATE);
		mAccessToken = mPreferences.getString(
				Constants.Preferences.KEY_ACCESS_TOKEN_USER, "");

		// Get the cart values for current object
		mPurchaseUrl = getIntent().getStringExtra(
				Constants.PageUrl.INTENT_PURCHASE_URL);

		// Set up action bar.
		final ActionBar actionBar = getActionBar();
		// Specify that the Home button should show an "Up" caret, indicating
		// that touching the
		// button will take the user one step up in the application's hierarchy.
		actionBar.setDisplayHomeAsUpEnabled(true);
		// Initialize views
		initializeViews();
		// Get the cart after re-authenticating
		getCompleteCart();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// Incase user presses back button from top navigation
			Intent intent = new Intent(PurchaseActivity.this,
					CartActivity.class);
			if (mIsOrderConfirmed) {
				setResult(CartActivity.RESULT_CODE_PURCHASE_SUCESSFUL, intent);
			} else {
				setResult(0, intent);
			}
			finish();// finishing activity
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * This method initializes the page view elements
	 */
	private void initializeViews() {
		mLVCart = (ListView) findViewById(R.id.lvPurchaseItems);
	}

	/**
	 * 
	 * This function updates the views with data
	 * 
	 * 
	 */
	private void setViewData() {
		PurchaseOrderAdapter mListAdapter = new PurchaseOrderAdapter(
				getApplicationContext(),
				mobjCart.getLineItems()[0].getElements());
		mLVCart.setAdapter(mListAdapter);

	}

	/**
	 * This function fires gets the Add to cart form
	 */
	private void getPurchaseOrderForm() {

		mGetPurchaseFormListener = new ListenerGetPurchaseForm() {
			@Override
			public void onTaskSuccessful(final PurchaseFormModel objResponse) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						setProgressBarIndeterminateVisibility(false);
						if (objResponse.getPurchaseLinks() != null
								&& objResponse.getPurchaseLinks().length > 0) {
							postPurchaseOrder(objResponse.getPurchaseLinks()[0]
									.getHREF());
						} else {
							NotificationUtils.showNotificationToast(
									getApplicationContext(),
									"No able to process at this time");
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
		PurchaseFormModel.getPurchaseOrderForm(getApplicationContext(),
				mPurchaseUrl, mAccessToken, mGetPurchaseFormListener);
	}

	/**
	 * This function posts the add to cart object
	 * 
	 * @param mPostUrl
	 */
	public void postPurchaseOrder(String mPostUrl) {
		mPostPurchaseListner = new ListenerCompletePurchaseOrder() {
			@Override
			public void onTaskSuccessful(final int response) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						setProgressBarIndeterminateVisibility(false);
						mIsOrderConfirmed = true;
						setViewData();
						NotificationUtils.showNotificationToast(
								getApplicationContext(), "Order Confirmed");
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
						NotificationUtils.showErrorToast(
								getApplicationContext(),
								Constants.ErrorCodes.ERROR_SERVER);
					}
				});
			}
		};
		setProgressBarIndeterminateVisibility(true);
		PurchaseOrderModel.postPurchaseOrder(mPostUrl,
				Constants.Config.CONTENT_TYPE_PURCHASE_ORDER, mAccessToken,
				mPostPurchaseListner);
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
						// set data
						mobjCart = response;
						getPurchaseOrderForm();
						// populate the list view

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

	@Override
	public void onBackPressed() {
		// Incase user presses back button from top navigation
		Intent intent = new Intent(PurchaseActivity.this, CartActivity.class);
		if (mIsOrderConfirmed) {
			setResult(CartActivity.RESULT_CODE_PURCHASE_SUCESSFUL, intent);
		} else {
			setResult(0, intent);
		}
		finish();
	}

}
