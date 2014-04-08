package com.optimusinfo.elasticpath.cortex.purchase;

/**
 * This is the listener for Order Details task
 * 
 * @author Optimus
 * 
 */
public interface ListenerOrderDetails {

	void onTaskSuccessful(OrderModel response);

	void onTaskFailed(int errorCode);

	void onAuthenticationFailed();
}
