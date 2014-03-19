package com.optimusinfo.elasticpath.cortex.checkout;

/**
 * This is the listener for Product Listing asynchronous task
 * 
 * @author Optimus
 * 
 */
public interface ListenerCheckOutOrder {

	void onTaskSuccessful(CheckoutModel response);

	void onTaskFailed(int errorCode);

	void onAuthenticationFailed();
}
