package com.optimusinfo.elasticpath.cortex.product.listing;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.optimusinfo.elasticpath.cortex.common.EPFragment;
import com.optimusinfo.elasticpath.cortexAPI.R;

public class ProductListFragment extends EPFragment {

	private GridView mProductListGridView;
	private ProductListingAdapter mProductsAdapter;
	protected String mProductBaseUrl;
	private ProductListing mObjProducts;

	/**
	 * Constructor for this fragment
	 * 
	 * @param mProductBaseUrl
	 * @param mObjProducts
	 */
	public ProductListFragment(String mProductBaseUrl,
			ProductListing mObjProducts) {
		super();
		this.mProductBaseUrl = mProductBaseUrl;
		this.mObjProducts = mObjProducts;
	}

	/**
	 * This method will only be called once when the retained Fragment is first
	 * created.
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the view
		View viewNavigation = inflater.inflate(
				R.layout.fragment_product_listing, container, false);
		if (viewNavigation != null) {
			mProductListGridView = (GridView) viewNavigation
					.findViewById(R.id.gvProducts);
		}
		if (mObjProducts != null) {
			mProductsAdapter = new ProductListingAdapter(this, mObjProducts);
			// Set the adapter
			mProductListGridView.setAdapter(mProductsAdapter);
		}
		return viewNavigation;
	}

	@Override
	public void detachChildFragments() {
		// No child fragments added		
	}

}
