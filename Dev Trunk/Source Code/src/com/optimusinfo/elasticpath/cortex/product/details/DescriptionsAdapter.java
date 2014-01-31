package com.optimusinfo.elasticpath.cortex.product.details;

import com.optimusinfo.elasticpath.cortex.product.details.ProductDetail.ProductDetails;

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
public class DescriptionsAdapter extends ArrayAdapter<ProductDetails> {

	private final Context mCurrent;
	private final ProductDetails[] mListDetails;
	private final int mColor1, mColor2;

	/**
	 * 
	 * @param context
	 * @param resource
	 * @param objects
	 */
	public DescriptionsAdapter(Context context, ProductDetails[] objects) {
		super(context, R.layout.item_list_product_description, objects);
		mCurrent = context;
		mListDetails = objects;

		mColor1 = mCurrent.getResources().getColor(
				R.color.bg_listview_product_desc_1);
		mColor2 = mCurrent.getResources().getColor(
				R.color.bg_listview_product_desc_2);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		LayoutInflater inflater = (LayoutInflater) mCurrent
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		row = inflater.inflate(R.layout.item_list_product_description, parent,
				false);

		if (position % 2 == 0) {
			row.setBackgroundColor(mColor1);
		} else {
			row.setBackgroundColor(mColor2);
		}

		TextView tvName = (TextView) row
				.findViewById(R.id.tvProductDescItemName);
		tvName.setText(mListDetails[position].getDisplayName());
		TextView tvValue = (TextView) row
				.findViewById(R.id.tvProductDescItemValue);
		tvValue.setText(mListDetails[position].getDisplayValue());

		return row;
	}
}
