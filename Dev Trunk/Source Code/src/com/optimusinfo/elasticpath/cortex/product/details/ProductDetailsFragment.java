package com.optimusinfo.elasticpath.cortex.product.details;

import com.optimusinfo.elasticpath.cortexAPI.R;
import com.optimusinfo.elasticpath.cortex.cart.CartFragment;
import com.optimusinfo.elasticpath.cortex.common.Constants;
import com.optimusinfo.elasticpath.cortex.common.EPFragment;
import com.optimusinfo.elasticpath.cortex.common.EPImageView;
import com.optimusinfo.elasticpath.cortex.common.NotificationUtils;
import com.optimusinfo.elasticpath.cortex.product.details.ProductDetail.ProductAddToCartForm;
import com.optimusinfo.elasticpath.cortex.product.details.ProductDetail.ProductAssets;
import com.optimusinfo.elasticpath.cortex.product.details.ProductDetail.ProductAvailability;
import com.optimusinfo.elasticpath.cortex.product.details.ProductDetail.ProductDefinition;
import com.optimusinfo.elasticpath.cortex.product.details.ProductDetail.ProductDetails;
import com.optimusinfo.elasticpath.cortex.product.details.ProductDetail.ProductLinks;
import com.optimusinfo.elasticpath.cortex.product.details.ProductDetail.ProductPrice;
import com.optimusinfo.elasticpath.cortex.product.details.ProductDetail.ProductRates;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Fragment class to show Product Details
 * 
 * @author Optimus
 * 
 */
public class ProductDetailsFragment extends EPFragment {

	protected String mProductBaseUrl;
	protected ListenerGetProductDetails mListenerGetProductDetails;
	protected ProductDetail mProductDef;
	protected View mViewParent;

	/**
	 * Constructure for this fragment
	 * 
	 * @param mProductBaseUrl
	 */
	public ProductDetailsFragment(String mProductBaseUrl) {
		super();
		this.mProductBaseUrl = mProductBaseUrl;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View viewNavigation = inflater.inflate(
				R.layout.fragment_product_details, container, false);
		mViewParent = viewNavigation;

		if (mProductDef != null) {
			initializeViews(viewNavigation);
		} else {
			// Get the product details
			getProductDetails();
		}

		return viewNavigation;

	}

	public void getProductDetails() {

		mListenerGetProductDetails = new ListenerGetProductDetails() {

			@Override
			public void onTaskSuccessful(final Object dataNavigations) {
				mProductDef = (ProductDetail) dataNavigations;
				getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						getActivity().setProgressBarIndeterminateVisibility(
								false);
						if (getMainFragment() instanceof ProductDetailsFragment) {
							// Update the views
							initializeViews(mViewParent);
						}
					}
				});
			}

			@Override
			public void onTaskFailed(final int errorCode) {
				getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						getActivity().setProgressBarIndeterminateVisibility(
								false);
						// TODO - For Future Req
						NotificationUtils.showErrorToast(getActivity(),
								errorCode);
					}
				});
			}

			@Override
			public void onAuthenticationFailed() {
				getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						getActivity().setProgressBarIndeterminateVisibility(
								false);
						// TODO For Future Req
					}
				});
			}
		};
		getActivity().setProgressBarIndeterminateVisibility(true);
		ProductDetail.getProuctListingFromServer(getActivity(),
				mProductBaseUrl, Constants.ZoomUrl.URL_ZOOM_PRODUCT_DETAILS,
				getUserAuthenticationToken(), mListenerGetProductDetails);
	}

	/**
	 * This function updates the views with data
	 */
	private void initializeViews(View parent) {

		LinearLayout llProductDetails = (LinearLayout) parent
				.findViewById(R.id.llProductDetails);
		llProductDetails.setVisibility(View.VISIBLE);
		// Set the product name
		ProductDefinition[] definition = mProductDef.getDefinition();
		TextView tvTitle = (TextView) parent
				.findViewById(R.id.tvProductDetailTitle);
		if (definition != null) {
			tvTitle.setText(definition[0].getDisplayName());
		}

		ProductDetails[] description = definition[0].getProductDetails();
		ListView lvDesc = (ListView) parent
				.findViewById(R.id.tvProductDetailDescription);
		if (description != null) {
			lvDesc.setAdapter(new DescriptionsAdapter(getActivity(),
					description));
		}

		// Set the product price
		ProductPrice[] price = mProductDef.getPrice();
		ProductRates[] rate = mProductDef.getRates();
		TextView tvPrice = (TextView) parent
				.findViewById(R.id.tvProductDetailPrice);
		if (price != null) {
			tvPrice.setText(price[0].getProductPrice()[0].getDisplay());
		} else if (rate != null) {
			tvPrice.setText(rate[0].getProductRates()[0].getRate());
		}

		// Set the product image
		ProductAssets[] assets = definition[0].getProductAssets();
		EPImageView ivImage = (EPImageView) parent
				.findViewById(R.id.ivProductDetail);
		if (assets != null) {
			ivImage.setImageUrl(assets[0].getProductImages()[0].getImageUrl());
		}

		// Initialize the add to cart button
		ProductAddToCartForm addToCartForm = mProductDef.getAddToCartForm()[0];
		final ProductLinks addToCartLinks = addToCartForm.getProductLinks();
		Button btAddToCart = (Button) parent
				.findViewById(R.id.btProductDetailAddToCart);
		btAddToCart.setCompoundDrawablePadding(5);
		btAddToCart.setCompoundDrawablesWithIntrinsicBounds(
				R.drawable.icon_cart, 0, 0, 0);
		if (addToCartLinks != null) {
			btAddToCart.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					CartFragment mObjFragment = new CartFragment(addToCartLinks
							.getHREF(), "1");
					addFragmentToBreadcrumb("Cart", R.id.fragment_container,
							mObjFragment);
				}
			});
		} else {
			btAddToCart.setEnabled(false);
		}

		// Initialize the product availability
		ProductAvailability[] availability = mProductDef.getAvailability();
		TextView tvAvailable = (TextView) parent
				.findViewById(R.id.tvProductDetailAvailable);
		tvAvailable.setCompoundDrawablePadding(5);
		if (availability != null) {
			String isAvailable = availability[0].getState();
			if (isAvailable.equalsIgnoreCase(Constants.STATE_AVAILABLE)) {
				tvAvailable.setText(R.string.labelProductAvailablle);
				tvAvailable.setCompoundDrawablesWithIntrinsicBounds(
						R.drawable.icon_in_stock, 0, 0, 0);
			} else {
				tvAvailable.setText(R.string.labelProductNotAvailablle);
				tvAvailable.setCompoundDrawablesWithIntrinsicBounds(
						R.drawable.icon_out_stock, 0, 0, 0);
				btAddToCart.setEnabled(false);
			}
		}
	}

	@Override
	public void onRefreshData() {
		// TODO Auto-generated method stub
		super.onRefreshData();
		getProductDetails();
	}

	@Override
	public void detachChildFragments() {
		// No child fragments added		
	}

}
