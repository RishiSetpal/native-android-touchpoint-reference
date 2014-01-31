package com.optimusinfo.elasticpath.cortex.cart;

public class CartModel {
	/**
	 * Initializes the variables and adds the quantity to the cart
	 * 
	 * @param quanity
	 *            the quantity to add
	 * @param urlAddToCartForm
	 *            the url of the form to post this add to cart request
	 * @param contentType
	 *            the content type of the request
	 * @param token
	 *            the token used for authentication
	 */
	public CartModel(int quanity, String urlAddToCartForm, String contentType,
			String token) {
	}

	/**
	 * 
	 * @param quanity
	 * @param urlAddToCartForm
	 * @param contentType
	 * @param accessToken
	 */
	public static void addToCart(int quanity, String urlAddToCartForm,
			String contentType, String accessToken, ListenerAddToCart listener) {
		try {
			AsyncTaskAddToCart taskAddToCart = new AsyncTaskAddToCart(
					urlAddToCartForm, quanity, urlAddToCartForm, accessToken,
					listener);
			taskAddToCart.execute();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}
}
