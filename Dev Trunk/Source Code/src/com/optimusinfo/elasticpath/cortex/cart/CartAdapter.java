package com.optimusinfo.elasticpath.cortex.cart;

import com.optimusinfo.elasticpath.cortex.cart.CartActivity.ProductCartDetails;
import com.optimusinfo.elasticpath.cortex.common.EPImageView;
import com.optimusinfo.elasticpath.cortexAPI.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * This class shows the list view for description of a product
 * 
 * @author Optimus
 * 
 */
public class CartAdapter extends ArrayAdapter<ProductCartDetails> {

	private final Context mCurrent;
	private final ProductCartDetails[] mListDetails;

	/**
	 * 
	 * @param context
	 * @param resource
	 * @param objects
	 */
	public CartAdapter(Context context, ProductCartDetails[] objects) {
		super(context, R.layout.item_list_cart_description, objects);
		mCurrent = context;
		mListDetails = objects;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		View row = convertView;

		LayoutInflater inflater = (LayoutInflater) mCurrent
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		row = inflater.inflate(R.layout.item_list_cart_description, parent,
				false);

		ProductCartDetails objDetails = mListDetails[position];

		if (objDetails.getProductTitle() != null) {
			TextView tvTitle = (TextView) row
					.findViewById(R.id.tvCartProductTitle);
			tvTitle.setText(objDetails.getProductTitle());
		}

		if (objDetails.getProductPrice() != null) {
			TextView tvPrice = (TextView) row
					.findViewById(R.id.tvCartProductPrice);
			tvPrice.setText("Price \t".concat(objDetails.getProductPrice()));
		}

		if (objDetails.getProductImageUrl() != null) {
			EPImageView ivImage = (EPImageView) row
					.findViewById(R.id.epCartProductImage);
			ivImage.setImageUrl(objDetails.getProductImageUrl());
		}
		return row;
	}
}
