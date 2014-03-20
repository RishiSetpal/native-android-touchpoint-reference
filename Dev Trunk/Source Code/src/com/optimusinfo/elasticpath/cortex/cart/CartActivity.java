package com.optimusinfo.elasticpath.cortex.cart;

import com.optimusinfo.elasticpath.cortexAPI.R;
import com.optimusinfo.elasticpath.cortex.authentication.AuthenticationActivity;
import com.optimusinfo.elasticpath.cortex.checkout.CheckoutActivity;
import com.optimusinfo.elasticpath.cortex.common.Constants;
import com.optimusinfo.elasticpath.cortex.common.EPFragmentActivity;
import com.optimusinfo.elasticpath.cortex.common.NotificationUtils;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CartActivity extends EPFragmentActivity {

	public static int REQUEST_CODE_AUTHENTICATION = 27;
	public static int RESULT_CODE_AUTHENTICATION_SUCESSFUL = 28;

	public static int REQUEST_CODE_CHECKOUT = 29;
	public static int RESULT_CODE_PURCHASE_SUCESSFUL = 30;

	public static boolean isUserLoggedIn = false;

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments representing each object in a collection. We use a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter} derivative,
	 * which will destroy and re-create fragments as needed, saving and
	 * restoring their state in the process. This is important to conserve
	 * memory and is a best practice when allowing navigation between objects in
	 * a potentially large collection.
	 */
	protected String mAddToCartUrl, mProductQuantity, mPurchaseUrl;

	protected String mCheckOutLink;

	// Request call listeners
	protected ListenerGetCartForm mGetCartFormListener;
	protected ListenerAddToCart mAddToCartListner;
	protected ListenerGetCompleteCartItems mCartItemsListener;
	protected CartModel mObjCart;
	// Page views
	protected RelativeLayout mLayout;
	protected Button mBTCheckout;
	protected ListView mLVCart;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Set the content view
		setContentView(R.layout.activity_cart);
		// Initialize the params objects
		super.initializeParams();
		// Disable the title
		mObjActionBar.setDisplayShowTitleEnabled(false);

		// Get the cart values for current object
		mAddToCartUrl = getIntent().getStringExtra(
				Constants.PageUrl.INTENT_CART_URL);
		mProductQuantity = getIntent().getStringExtra(
				Constants.PageUrl.INTENT_PRODUCT_QUANT);
		// Initialize the view elements
		initializeViews();

		if (savedInstanceState != null) {
			mObjCart = (CartModel) savedInstanceState
					.getSerializable("CartObject");
		}

		if (mObjCart != null) {
			mPurchaseUrl = mObjCart.getOrderitems()[0].getPurchaseForms()[0]
					.getPurchaseLinks().getHREF();
			mCheckOutLink = mObjCart.getOrderitems()[0].getSelf()
					.getCheckOutLink();

			setViewData();
		} else {
			// Perform the add to cart method
			getAddToCartForm();
		}

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		try {
			outState.putSerializable("CartObject", mObjCart);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
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
		mLayout.setVisibility(View.GONE);
		AddToCartModel.getAddToCartForm(getApplicationContext(), mAddToCartUrl,
				getUserAuthenticationToken(), mGetCartFormListener);
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
				Constants.Config.CONTENT_TYPE_ADD_TO_CART,
				getUserAuthenticationToken(), mAddToCartListner);
	}

	/**
	 * This function gets the complete items cart
	 */
	public void getCompleteCart() {

		mCartItemsListener = new ListenerGetCompleteCartItems() {
			@Override
			public void onTaskSuccessful(CartModel response) {

				mObjCart = response;
				mPurchaseUrl = response.getOrderitems()[0].getPurchaseForms()[0]
						.getPurchaseLinks().getHREF();
				mCheckOutLink = response.getOrderitems()[0].getSelf()
						.getCheckOutLink();

				// Pass control back to main thread
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						setProgressBarIndeterminateVisibility(false);
						// populate the list view
						setViewData();
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
		String cartsUrl = mObjCortexParams.getEndpoint().concat("carts/")
				.concat(mObjCortexParams.getScope())
				.concat(Constants.ZoomUrl.URL_ZOOM_CART);
		CartModel.getCartItems(getApplicationContext(), cartsUrl,
				getUserAuthenticationToken(), mCartItemsListener);
	}

	/**
	 * 
	 * This function updates the views with data
	 * 
	 * @param objCart
	 *            - cart elements array
	 */
	private void setViewData() {

		mLayout.setVisibility(View.VISIBLE);
		mBTCheckout.setVisibility(View.VISIBLE);
		CartAdapter mListAdapter = new CartAdapter(getApplicationContext(),
				mObjCart.getLineItems()[0].getElements());
		mLVCart.setAdapter(mListAdapter);

		// Set the total quantity
		TextView tvTotalQuantity = (TextView) findViewById(R.id.tvCartHeaderQuantity);
		tvTotalQuantity.setText(mObjCart.getTotalQuantity().concat(" ")
				.concat(this.getString(R.string.prefixItems)));

		TextView tvOrderTotal = (TextView) findViewById(R.id.tvCartHeaderTotal);
		tvOrderTotal.setText(mObjCart.getCartTotal()[0].getCosts()[0]
				.getTotalCost());
		TextView tvCartFooterTotal = (TextView) findViewById(R.id.tvCartFooterTotal);
		tvCartFooterTotal.setText(mObjCart.getCartTotal()[0].getCosts()[0]
				.getTotalCost());
	}

	/**
	 * This method initializes the page view elements
	 */
	private void initializeViews() {
		mLayout = (RelativeLayout) findViewById(R.id.rlCart);

		// Initialize the purchase button
		mBTCheckout = (Button) findViewById(R.id.btCheckOut);
		mBTCheckout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isUserLoggedIn) {
					Intent mPurchIntent = new Intent(CartActivity.this,
							CheckoutActivity.class);
					mPurchIntent.putExtra(Constants.PageUrl.INTENT_CHECKOUT,
							mCheckOutLink);
					startActivityForResult(mPurchIntent, REQUEST_CODE_CHECKOUT);
				} else {
					Intent mAuthIntent = new Intent(CartActivity.this,
							AuthenticationActivity.class);
					startActivityForResult(mAuthIntent,
							REQUEST_CODE_AUTHENTICATION);
				}

			}
		});
		mLVCart = (ListView) findViewById(R.id.lvCartItems);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		if (resultCode == RESULT_CODE_AUTHENTICATION_SUCESSFUL) {
			isUserLoggedIn = true;
			
			getAddToCartForm();
		} else if (resultCode == RESULT_CODE_PURCHASE_SUCESSFUL) {
			finish();
		}
	}
}
