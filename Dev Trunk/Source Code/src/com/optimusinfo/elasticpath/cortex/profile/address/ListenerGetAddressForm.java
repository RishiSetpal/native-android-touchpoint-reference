package com.optimusinfo.elasticpath.cortex.profile.address;

import com.optimusinfo.elasticpath.cortex.profile.address.AddressModel.CreateAddressFormModel;

/**
 * This is the listener for Product Listing asynchronous task
 * 
 * @author Optimus
 * 
 */
public interface ListenerGetAddressForm {

	void onTaskSuccessful(CreateAddressFormModel response);

	void onTaskFailed(int errorCode);

	void onAuthenticationFailed();
}
