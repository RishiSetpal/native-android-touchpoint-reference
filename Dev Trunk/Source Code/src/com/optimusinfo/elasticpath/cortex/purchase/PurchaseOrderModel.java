package com.optimusinfo.elasticpath.cortex.purchase;

public class PurchaseOrderModel {

	/**
	 * 
	 * @param url
	 * @param contentType
	 * @param token
	 * @param listener
	 */
	public static void postPurchaseOrder(String url, String contentType,
			String token, ListenerCompletePurchaseOrder listener) {
		try {
			AsyncTaskPostPurchaseOrder taskPurchaseOrder = new AsyncTaskPostPurchaseOrder(
					url, contentType, token, listener);
			taskPurchaseOrder.execute();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

}
