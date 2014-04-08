package com.optimusinfo.elasticpath.cortex.purchase;

/**
 * This is the listener for Product Listing asynchronous task
 * 
 * @author Optimus
 * 
 */
public interface ListenerCompletePurchaseOrder {

	void onTaskSuccessful(String orderUrl);

	void onTaskFailed(int errorCode);

	void onAuthenticationFailed();
}
