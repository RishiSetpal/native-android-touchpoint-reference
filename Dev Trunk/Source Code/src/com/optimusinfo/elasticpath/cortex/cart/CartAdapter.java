/*
 * Copyright © 2014 Elastic Path Software Inc. All rights reserved.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * 
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.optimusinfo.elasticpath.cortex.cart;

import android.content.Context;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.optimusinfo.elasticpath.cortex.cart.CartModel.Element;
import com.optimusinfo.elasticpath.cortex.cart.CartModel.ItemPrice;
import com.optimusinfo.elasticpath.cortex.cart.CartModel.ItemRate;
import com.optimusinfo.elasticpath.cortex.common.EPFragment;
import com.optimusinfo.elasticpath.cortex.common.imageutils.ImageLoader;
import com.optimusinfo.elasticpath.cortexAPI.R;

/**
 * This class shows the list view for description of a product
 * 
 * @author Optimus
 * 
 */
public class CartAdapter extends ArrayAdapter<Element> {

	private final EPFragment mCurrent;
	private final Element[] mListDetails;
	private ImageLoader mImageLoader;

	/**
	 * 
	 * @param context
	 * @param resource
	 * @param objects
	 */
	public CartAdapter(EPFragment mCurrent, Element[] objects) {
		super(mCurrent.getActivity(), R.layout.item_list_cart_description,
				objects);
		this.mCurrent = mCurrent;
		this.mListDetails = objects;
		this.mImageLoader = new ImageLoader(mCurrent.getActivity());
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mListDetails.length;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		View row = convertView;

		LayoutInflater inflater = (LayoutInflater) mCurrent.getActivity()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		row = inflater.inflate(R.layout.item_list_cart_description, parent,
				false);

		Element objDetails = mListDetails[position];

		final String mDeleteLink = mListDetails[position].mLink.mHref;

		ItemPrice[] objItemPrice = objDetails.getItems()[0].mUnitPrice;
		ItemRate[] objItemRate = objDetails.getItems()[0].mUnitRate;

		if (objDetails.getItems()[0].getDefinitions()[0].getDisplayName() != null) {
			TextView tvTitle = (TextView) row
					.findViewById(R.id.tvCartProductTitle);
			tvTitle.setText(objDetails.getItems()[0].getDefinitions()[0]
					.getDisplayName());
		}

		TextView tvPrice = (TextView) row.findViewById(R.id.tvCartProductPrice);
		if (objItemPrice != null) {
			tvPrice.setText("Price: \t".concat(objItemPrice[0]
					.getProductPrices()[0].getDisplayPrice()));
		} else if (objItemRate != null) {
			tvPrice.setText("Price: \t".concat(objItemRate[0]
					.getProductPrices()[0].getDisplayPrice()));
		}

		TextView tvCartRemove = (TextView) row
				.findViewById(R.id.btCartItemRemove);
		tvCartRemove.setCompoundDrawablePadding(5);
		tvCartRemove.setCompoundDrawablesWithIntrinsicBounds(0, 0,
				R.drawable.icon_remove, 0);

		tvCartRemove.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				((CartFragment) mCurrent).deleteCartItem(mDeleteLink);
			}
		});

		TextView tvAvailable = (TextView) row
				.findViewById(R.id.tvProductCartAvailable);
		tvAvailable.setCompoundDrawablePadding(5);
		tvAvailable.setCompoundDrawablesWithIntrinsicBounds(0,
				R.drawable.icon_in_stock, 0, 0);

		EditText etQuant = (EditText) row.findViewById(R.id.etCartItemQuantity);
		etQuant.setText(objDetails.getQuantity());
		etQuant.setImeOptions(EditorInfo.IME_ACTION_DONE);

		etQuant.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE) {
					String quant = v.getText().toString();
					if (TextUtils.isEmpty(quant)) {
						quant = "0";
					}
					((CartFragment) mCurrent)
							.updateCartItem(mDeleteLink, quant);
				}
				return false;
			}
		});

		try {
			ImageView ivImage = (ImageView) row
					.findViewById(R.id.epCartProductImage);
			ivImage.setTag(objDetails.getItems()[0].getDefinitions()[0]
					.getItemAssets()[0].getAssetsElements()[0].getImageUrl());
			mImageLoader.displayRoundedCornersImage(
					objDetails.getItems()[0].getDefinitions()[0]
							.getItemAssets()[0].getAssetsElements()[0]
							.getImageUrl(), ivImage, true);

		} catch (NullPointerException e) {

		}
		return row;
	}
}
