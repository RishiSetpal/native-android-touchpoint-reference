package com.optimusinfo.elasticpath.cortex.purchase;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.optimusinfo.elasticpath.cortex.cart.CartModel.Element;
import com.optimusinfo.elasticpath.cortex.cart.CartModel.ItemPrice;
import com.optimusinfo.elasticpath.cortex.cart.CartModel.ItemRate;
import com.optimusinfo.elasticpath.cortex.common.EPImageView;
import com.optimusinfo.elasticpath.cortexAPI.R;

/**
 * This class shows the list view for description of a product
 * 
 * @author Optimus
 * 
 */
public class PurchaseOrderAdapter extends ArrayAdapter<Element> {

	private final Context mCurrent;
	private final Element[] mListDetails;

	/**
	 * 
	 * @param context
	 * @param resource
	 * @param objects
	 */
	public PurchaseOrderAdapter(Context context, Element[] objects) {
		super(context, R.layout.item_list_purchase_desc, objects);
		mCurrent = context;
		mListDetails = objects;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mListDetails.length;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		View row = convertView;

		LayoutInflater inflater = (LayoutInflater) mCurrent
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		row = inflater.inflate(R.layout.item_list_purchase_desc, parent,
				false);

		Element objDetails = mListDetails[position];

		ItemPrice[] objItemPrice = objDetails.getItemsPrice();
		ItemRate[] objItemRate = objDetails.getItemsRate();

		if (objDetails.getItems()[0].getDefinitions()[0].getDisplayName() != null) {
			TextView tvTitle = (TextView) row
					.findViewById(R.id.tvPurchaseProductTitle);
			tvTitle.setText(objDetails.getItems()[0].getDefinitions()[0]
					.getDisplayName());
		}
		TextView tvPrice = (TextView) row
				.findViewById(R.id.tvPurchaseProductPrice);
		if (objItemPrice != null) {			
			tvPrice.setText("Price \t".concat(objItemPrice[0]
					.getProductPrices()[0].getDisplayPrice()));
		} else if (objItemRate !=null){
			tvPrice.setText("Price \t".concat(objItemRate[0]
					.getProductPrices()[0].getDisplayPrice()));
		}

		// Set the image
		try {

			EPImageView ivImage = (EPImageView) row
					.findViewById(R.id.epPurchaseProductImage);
			ivImage.setImageUrl(objDetails.getItems()[0].getDefinitions()[0]
					.getItemAssets()[0].getAssetsElements()[0].getImageUrl());

		} catch (NullPointerException e) {

		}
		return row;
	}
}
