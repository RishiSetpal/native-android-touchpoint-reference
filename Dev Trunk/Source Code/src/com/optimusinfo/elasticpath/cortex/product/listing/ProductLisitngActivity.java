package com.optimusinfo.elasticpath.cortex.product.listing;

import com.optimusinfo.elasticpath.cortexAPI.R;
import com.optimusinfo.elasticpath.cortex.cart.CartActivity;
import com.optimusinfo.elasticpath.cortex.common.Constants;
import com.optimusinfo.elasticpath.cortex.common.EPImageView;
import com.optimusinfo.elasticpath.cortex.common.NotificationUtils;
import com.optimusinfo.elasticpath.cortex.product.details.ProductDetailsActivity;
import com.optimusinfo.elasticpath.cortex.product.listing.ProductListing.ProductAddToCartForm;
import com.optimusinfo.elasticpath.cortex.product.listing.ProductListing.ProductAssets;
import com.optimusinfo.elasticpath.cortex.product.listing.ProductListing.ProductAvailability;
import com.optimusinfo.elasticpath.cortex.product.listing.ProductListing.ProductDefinition;
import com.optimusinfo.elasticpath.cortex.product.listing.ProductListing.ProductElement;
import com.optimusinfo.elasticpath.cortex.product.listing.ProductListing.ProductLinks;
import com.optimusinfo.elasticpath.cortex.product.listing.ProductListing.ProductPrice;
import com.optimusinfo.elasticpath.cortex.product.listing.ProductListing.ProductRates;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class ProductLisitngActivity extends FragmentActivity {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments representing each object in a collection. We use a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter} derivative,
	 * which will destroy and re-create fragments as needed, saving and
	 * restoring their state in the process. This is important to conserve
	 * memory and is a best practice when allowing navigation between objects in
	 * a potentially large collection.
	 */
	protected ProductListingPagerAdapter mProductListingAdapter;
	protected String mProductBaseUrl, mAccessToken;
	protected SharedPreferences mPreferences;
	protected ListenerGetProductListings mListenerGetProductListings;

	/**
	 * The {@link android.support.v4.view.ViewPager} that will display the
	 * object collection.
	 */
	ViewPager mViewPager;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_product_listing);

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

		// Set up the ViewPager, attaching the adapter.
		mViewPager = (ViewPager) findViewById(R.id.pagerProductListing);
		getProductListing();

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

	public void getProductListing() {
		mListenerGetProductListings = new ListenerGetProductListings() {

			@Override
			public void onTaskSuccessful(final Object dataNavigations) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						setProgressBarIndeterminateVisibility(false);
						// Create an adapter that when requested, will return a
						// fragment
						// representing an object in
						// the collection.
						if (((ProductListing) dataNavigations).getElements() != null) {
							mProductListingAdapter = new ProductListingPagerAdapter(
									getSupportFragmentManager(),
									(ProductListing) dataNavigations);
							mViewPager.setAdapter(mProductListingAdapter);
						} else {
							NotificationUtils.showNotificationToast(
									getApplicationContext(),
									getApplicationContext().getString(
											R.string.msgNoProductsPresent));
						}
					}
				});
			}

			@Override
			public void onTaskFailed(int errorCode) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						setProgressBarIndeterminateVisibility(false);
						// TODO - For Future Req
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
		ProductListing.getProuctListingFromServer(getApplicationContext(),
				mProductBaseUrl, Constants.PageUrl.pageUrl, "1",
				Constants.ZoomUrl.URL_ZOOM_PRODUCT_LISTING, mAccessToken,
				mListenerGetProductListings);

	}

	/**
	 * A {@link android.support.v4.app.FragmentStatePagerAdapter} that returns a
	 * fragment representing an object in the collection.
	 */
	public static class ProductListingPagerAdapter extends
			FragmentStatePagerAdapter {
		protected ProductListing mProducts;

		/**
		 * Constructor
		 * 
		 * @param fm
		 * @param products
		 */
		public ProductListingPagerAdapter(FragmentManager fm,
				ProductListing products) {
			super(fm);
			this.mProducts = products;
		}

		@Override
		public Fragment getItem(int i) {
			Fragment fragment = new ProductObjectFragment(
					this.mProducts.getElements()[i]);
			Bundle args = new Bundle();
			args.putInt(ProductObjectFragment.ARG_OBJECT, i + 1);
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount() {
			if (mProducts.getElements() != null) {
				return mProducts.getElements().length;
			}
			return 0;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return this.mProducts.getElements()[position].getDefinition()[0].mDisplayName;
		}
	}

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public static class ProductObjectFragment extends Fragment {

		protected ProductElement mElement;

		public static final String ARG_OBJECT = "object";

		public ProductObjectFragment(ProductElement mElement) {
			super();
			this.mElement = mElement;
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(
					R.layout.fragment_product_definition, container, false);

			rootView.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// Start the product details activity
					Intent intent = new Intent(getActivity(),
							ProductDetailsActivity.class);
					intent.putExtra(Constants.PageUrl.INTENT_BASE_URL, mElement
							.getSelf().getHref());
					startActivity(intent);
				}
			});

			ProductDefinition[] definition = mElement.getDefinition();
			ProductPrice[] price = mElement.getPrice();
			ProductRates[] rate = mElement.getRates();
			ProductAssets[] asset = definition[0].getProductAssets();
			ProductAvailability[] availability = mElement.getAvailability();
			final ProductAddToCartForm addToCartForm = mElement
					.getAddToCartForm()[0];
			final ProductLinks[] addToCartLinks = addToCartForm
					.getProductLinks();
			// Start the product details activity
			final Intent mCartIntent = new Intent(getActivity(),
					CartActivity.class);

			// Set product title
			if (definition != null) {
				((TextView) rootView.findViewById(R.id.tvProductListingTitle))
						.setText(definition[0].getDisplayName());
				mCartIntent.putExtra(Constants.PageUrl.INTENT_PRODUCT_TITLE,
						definition[0].getDisplayName());
			}

			// set product price
			if (price != null) {
				((TextView) rootView.findViewById(R.id.tvProductListingPrice))
						.setText("Price\t".concat(price[0].getProductPrice()[0]
								.getDisplay()));
				mCartIntent.putExtra(Constants.PageUrl.INTENT_PRODUCT_PRICE,
						price[0].getProductPrice()[0].getDisplay());
			} else if (rate != null) {
				((TextView) rootView.findViewById(R.id.tvProductListingPrice))
						.setText("Price\t".concat(rate[0].getProductRates()[0]
								.getRate()));
				mCartIntent.putExtra(Constants.PageUrl.INTENT_PRODUCT_PRICE,
						rate[0].getProductRates()[0].getRate());
			}
			// set product image
			if (asset != null) {
				((EPImageView) rootView.findViewById(R.id.ivProductListing))
						.setImageUrl(asset[0].getProductImages()[0]
								.getImageUrl());
				mCartIntent.putExtra(Constants.PageUrl.INTENT_PRODUCT_IMAGE,
						asset[0].getProductImages()[0].getImageUrl());
			}
			// set product availability
			if (availability != null) {
				String isAvailable = availability[0].getState();
				if (isAvailable.equalsIgnoreCase(Constants.STATE_AVAILABLE)) {
					((TextView) rootView
							.findViewById(R.id.tvProductListingAvailable))
							.setText(R.string.labelProductAvailablle);
				} else {
					((TextView) rootView
							.findViewById(R.id.tvProductListingAvailable))
							.setText(R.string.labelProductNotAvailablle);
					((Button) rootView.findViewById(R.id.btAddToCart))
							.setEnabled(false);
				}
			}

			if (addToCartLinks != null && addToCartLinks.length != 0) {
				((Button) rootView.findViewById(R.id.btAddToCart))
						.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								mCartIntent.putExtra(
										Constants.PageUrl.INTENT_CART_URL,
										addToCartLinks[0].getHREF());
								startActivity(mCartIntent);
							}
						});
			} else {
				((Button) rootView.findViewById(R.id.btAddToCart))
						.setEnabled(false);
			}
			return rootView;
		}
	}
}
