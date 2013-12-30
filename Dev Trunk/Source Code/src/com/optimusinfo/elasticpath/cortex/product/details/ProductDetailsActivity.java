package com.optimusinfo.elasticpath.cortex.product.details;

import com.optimusinfo.elasticpath.cortex.R;
import com.optimusinfo.elasticpath.cortex.common.Constants;
import com.optimusinfo.elasticpath.cortex.common.EPImageView;
import com.optimusinfo.elasticpath.cortex.common.NotificationUtils;
import com.optimusinfo.elasticpath.cortex.product.details.ProductDetail.ProductAddToCartForm;
import com.optimusinfo.elasticpath.cortex.product.details.ProductDetail.ProductAssets;
import com.optimusinfo.elasticpath.cortex.product.details.ProductDetail.ProductAvailability;
import com.optimusinfo.elasticpath.cortex.product.details.ProductDetail.ProductDefinition;
import com.optimusinfo.elasticpath.cortex.product.details.ProductDetail.ProductLinks;
import com.optimusinfo.elasticpath.cortex.product.details.ProductDetail.ProductPrice;
import com.optimusinfo.elasticpath.cortex.product.details.ProductDetail.ProductRates;

import android.app.ActionBar;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ProductDetailsActivity extends FragmentActivity {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments representing each object in a collection. We use a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter} derivative,
	 * which will destroy and re-create fragments as needed, saving and
	 * restoring their state in the process. This is important to conserve
	 * memory and is a best practice when allowing navigation between objects in
	 * a potentially large collection.
	 */
	protected String mProductBaseUrl, mAccessToken;
	protected SharedPreferences mPreferences;
	protected ListenerGetProductDetails mListenerGetProductDetails;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

		setContentView(R.layout.activity_product_details);

		mProductBaseUrl = getIntent().getStringExtra(
				Constants.PageUrl.INTENT_BASE_URL);

		mPreferences = getSharedPreferences(
				Constants.Preferences.PREFERENCES_FILE_NAME,
				Context.MODE_PRIVATE);
		mAccessToken = mPreferences.getString(
				Constants.Preferences.KEY_ACCESS_TOKEN, "");

		// Set up action bar.
		final ActionBar actionBar = getActionBar();

		// Specify that the Home button should show an "Up" caret, indicating
		// that touching the
		// button will take the user one step up in the application's hierarchy.
		actionBar.setDisplayHomeAsUpEnabled(true);

		// Get the product details
		getProductDetails();

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

	public void getProductDetails() {

		mListenerGetProductDetails = new ListenerGetProductDetails() {

			@Override
			public void onTaskSuccessful(final Object dataNavigations) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						setProgressBarIndeterminateVisibility(false);
						// Update the views
						initializeViews((ProductDetail) dataNavigations);
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
		ProductDetail.getProuctListingFromServer(getApplicationContext(),
				mProductBaseUrl, Constants.ZoomUrl.URL_ZOOM_PRODUCT_DETAILS,
				mAccessToken, mListenerGetProductDetails);
	}

	/**
	 * This function updates the views with data
	 */
	private void initializeViews(ProductDetail product) {

		LinearLayout llProductDetails = (LinearLayout) findViewById(R.id.llProductDetails);
		llProductDetails.setVisibility(View.VISIBLE);

		// Set the product name
		ProductDefinition[] definition = product.getDefinition();
		TextView tvTitle = (TextView) findViewById(R.id.tvProductDetailTitle);
		if (definition != null) {
			tvTitle.setText(definition[0].getDisplayName());
		}

		// Set the product price
		ProductPrice[] price = product.getPrice();
		ProductRates[] rate = product.getRates();
		TextView tvPrice = (TextView) findViewById(R.id.tvProductDetailPrice);
		if (price != null) {
			tvPrice.setText(price[0].getProductPrice()[0].getDisplay());
		} else if (rate != null) {
			tvPrice.setText(rate[0].getProductRates()[0].getRate());
		}

		// Set the product image
		ProductAssets[] assets = definition[0].getProductAssets();
		EPImageView ivImage = (EPImageView) findViewById(R.id.ivProductDetail);
		if (assets != null) {
			ivImage.setImageUrl(assets[0].getProductImages()[0].getImageUrl());
		}

		// Initialize the add to cart button
		ProductAddToCartForm addToCartForm = product.getAddToCartForm()[0];
		final ProductLinks[] addToCartLinks = addToCartForm.getProductLinks();
		Button btAddToCart = (Button) findViewById(R.id.btProductDetailAddToCart);

		if (addToCartLinks != null && addToCartLinks.length != 0) {
			btAddToCart.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					NotificationUtils.showNotificationToast(
							getApplicationContext(),
							addToCartLinks[0].getHREF());
				}
			});
		} else {
			btAddToCart.setEnabled(false);
		}

		// Initialize the product availability
		ProductAvailability[] availability = product.getAvailability();
		TextView tvAvailable = (TextView) findViewById(R.id.tvProductDetailAvailable);
		if (availability != null) {
			String isAvailable = availability[0].getState();
			if (isAvailable.equalsIgnoreCase(Constants.STATE_AVAILABLE)) {
				tvAvailable.setText(R.string.labelProductAvailablle);
			} else {
				tvAvailable.setText(R.string.labelProductNotAvailablle);
				btAddToCart.setEnabled(false);
			}
		}
	}

}
