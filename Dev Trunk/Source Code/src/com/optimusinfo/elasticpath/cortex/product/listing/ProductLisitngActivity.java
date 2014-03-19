package com.optimusinfo.elasticpath.cortex.product.listing;

import com.optimusinfo.elasticpath.cortexAPI.R;
import com.optimusinfo.elasticpath.cortex.common.Constants;
import com.optimusinfo.elasticpath.cortex.common.EPFragmentActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

public class ProductLisitngActivity extends EPFragmentActivity {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments representing each object in a collection. We use a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter} derivative,
	 * which will destroy and re-create fragments as needed, saving and
	 * restoring their state in the process. This is important to conserve
	 * memory and is a best practice when allowing navigation between objects in
	 * a potentially large collection.
	 */
	protected String mProductBaseUrl, mCatHeader, mCatDesc;

	protected ProductListFragment mObjFragment;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Set the activity layout
		setContentView(R.layout.activity_product_listing);
		// Initialize the param objects
		super.initializeParams();
		// Disable the title
		mObjActionBar.setDisplayShowTitleEnabled(false);
		// Specify that the Home button should show an "Up" caret, indicating
		// that touching the
		// button will take the user one step up in the application's hierarchy.
		mObjActionBar.setDisplayHomeAsUpEnabled(true);

		mProductBaseUrl = getIntent().getStringExtra(
				Constants.PageUrl.INTENT_BASE_URL);
		mCatHeader = getIntent().getStringExtra(
				Constants.Content.INTENT_CAT_HEADER);
		mCatDesc = getIntent()
				.getStringExtra(Constants.Content.INTENT_CAT_DESC);
		setUpViews();
		if (savedInstanceState == null) {
			mObjFragment = new ProductListFragment();
			addFragment(R.id.fragment_container, mObjFragment);
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

	private void setUpViews() {
		((TextView) findViewById(R.id.tvProductListingCategoryTitle))
				.setText(mCatHeader);
		((TextView) findViewById(R.id.tvProductListingCategoryDesc))
				.setText(mCatDesc);
	}

}
