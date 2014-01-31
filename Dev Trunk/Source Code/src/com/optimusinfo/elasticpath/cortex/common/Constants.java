package com.optimusinfo.elasticpath.cortex.common;

public class Constants {

	public class Config {
		public static final String FILE_NAME_CONFIG = "config.json";
		public static final String CONTENT_TYPE = "application/x-www-form-urlencoded";
		public static final String CONTENT_TYPE_ADD_TO_CART = "application/json";
	}

	public class Routes {
		public static final String AUTH_ROUTE = "oauth2/tokens";
		public static final String NAVIGATION_ROUTE = "navigations/";
	}

	public class RequestHeaders {
		public static final String CONTENT_TYPE_STRING = "Content-Type";
		public static final String CONTENT_TYPE = "application/json";
		public static final String AUTHORIZATION_STRING = "Authorization";
		public static final String AUTHORIZATION_INITIALIZER = "Bearer";
	}

	public class ZoomUrl {
		public static final String URL_ZOOM_NAVIGATIONS = "?zoom=element";
		public static final String URL_ZOOM_PRODUCT_LISTING = "?zoom=element,element:availability,element:definition,element:definition:assets:element,element:price,element:rate,element:addtocartform";
		public static final String URL_ZOOM_PRODUCT_DETAILS = "?zoom=availability,addtocartform,price,rate,definition,definition:assets:element";
	}

	public class PageUrl {
		public static final String pageUrl = "/pages/";
		public static final String INTENT_BASE_URL = "baseURL";
		public static final String INTENT_CART_URL = "addToCartURL";
		public static final String INTENT_PRODUCT_TITLE = "productTitle";
		public static final String INTENT_PRODUCT_PRICE = "productPrice";
		public static final String INTENT_PRODUCT_IMAGE = "productImage";
		public static final String INTENT_PRODUCT_QUANT = "productQuantity";
	}

	public class Preferences {
		public static final String PREFERENCES_FILE_NAME = "EP_PRIVATE_SHARED_PREFERENCES";
		public static final String KEY_END_POINT = "CORTEX_END_POINT";
		public static final String KEY_PATH = "CORTEX_PATH";
		public static final String KEY_SCOPE = "CORTEX_SCOPE";
		public static final String KEY_ROLE = "CORTEX_ROLE";
		public static final String KEY_ACCESS_TOKEN = "CORTEX_ACCESS_TOKEN";
		public static final String LIST_CART = "LIST_CART";
		public static final String KEY_ACCESS_TOKEN_USER = "KEY_ACCESS_TOKEN_USER";
	}

	public class ApiResponseCode {
		public static final int UNAUTHORIZED_ACCESS = 401;
		public static final int REQUEST_SUCCESSFUL_UPDATED = 200;
		public static final int REQUEST_SUCCESSFUL_CREATED = 201;
	}

	public class ErrorCodes {
		public static final int ERROR_NETWORK = 1001;
		public static final int ERROR_SERVER = 1002;
	}

	public static final String STATE_AVAILABLE = "available";

}