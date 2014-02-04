package com.optimusinfo.elasticpath.cortex.purchase;

/**
 * This is the listener for Product Listing asynchronous task
 * 
 * @author Optimus
 * 
 */
public interface ListenerCompletePurchaseOrder {

	void onTaskSuccessful(int responseCode);

	void onTaskFailed(int errorCode);

	void onAuthenticationFailed();
}
