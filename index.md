---
layout: master
permalink: /
title: Home
weight: 1
---


## Setting up local dev environment

The Cortex Android frontend requires the following installed.

* [Android SDK](http://developer.android.com/sdk/index.html?hl=sk)
* [GIT Client](http://git-scm.com/downloads)
* [Java](https://www.java.com/en/download/)

Once the Android SDK is installed, open the Android IDE and check for the SDK setups. 

* Go to `Window -> Android SDK Manager`. 
* Verify if the following are present in your SDK.
	* Android 4.4.2 (API 19) / SDK Platform
	* Android 4.4.2 (API 19) / ARM EABI v7a System Image
	* Extras / Android Support Library
	* Extras / Google USB Driver

## Running the Android Cortex Storefront

* Fetch the Android Cortex Frontend Source code
	* https://github.com/elasticpath/native-android-touchpoint-reference
* Import the Project to Android IDE.
	* Go to `File -> Import` . 
	* Select `Android -> Existing Android Code Into Workspace -> Next`.
	* Browse to Android Cortex Frontend Source code directory and click **Ok**.
	* Select both the projects to import `PullToRefreshLibrary` and `SourceCode`
	* Click **Finish**.
	* Right click on the project folder and select `Run As` -> `Android Application`.
	* A screen will appear **Android Device Chooser** with the list of all the connected Mobile devices and available emulators.
	* You can connect an android mobile device via USB and enabling the **USB debugging** (Go to `Settings -> Developer Options -> USB debugging`).
	* If you want to run the application on an emulator, please refer to section “Setting up an Emulator”.

## Setting up an Android Emulator
	
* Open the Android IDE
* Goto `Window` -> `Android Virtual Device Manager`.
* To create a new emulator, follow the documentation mentioned at Managing Android Virtual Devices -http://developer.android.com/tools/devices/managing-avds.html .

## Technical Architecture
The Android application will follows the same abstraction as of HTML5 application.The implementation points for Cortex Android Frontend is as follows :-

* The config.json (Assets -> config.json) file will be used to configure features like EndPoints, Scope , Path etc which will be stored as Application preferences.
* The XML Styles and Drawables will constitute the part of “Presentation layer”. The main application theming will be driven by these files and user can modify them to change the application color schemes, fonts etc.
* The Modules section defines a complete module like Category, Authentication etc corresponding to different features. Each module will follow MVC pattern.
	* The `<Module>` Fragment and `<Module>` adapters will work as “Controller” and will be responsible for loading and populating views.
	* The <Module> XML layout files serves as “Views” defined in HTML5 application.
	* The <Module> Entity corresponds to the “Models”.
* The Utility Layer will contain the reusable code for Background/Async Tasks, XML /JSON Parsers, ImageUtilities etc.
	
## List of Components
	
The following is the list of components developed by Optimus.

* Home (com.optimusinfo.elasticpath.cortex) - This module is the starting point of the applications.
	* EPHomeActivity.java - This is main activity class which shows all the other modules as child fragments, also this class handles the features like Breadcrumbs, menu buttons for cart , profile and logout. 
	* Authentication (com.optimusinfo.elasticpath.cortex.authentication) - This module handles the login functionality.
	* AuthenticationActivity.java - This is the main activity class for authentication. 
	* Authentication.java - This is the model class for Authentication module.
	* AsyncTaskAuthentication.java - This class handles the asynchronous calls for Authentication requests.
* Cart (com.optimusinfo.elasticpath.cortex.cart) - This package handles the functionality related to cart module.
	* CartModel.java, AddToCartModel.java,  - This is the model class for cart module.
	* CartFragment.java - This is the fragment to show the cart screen. It also handles the events and requests generated in cart module.
	* CartAdapter.java - This is adapter to show the cart list items.
	* AsyncTaskAddToCart , AsyncTaskDeleteCartItem , AsyncTaskGetCartForm , AsyncTaskGetCompleteCart , AsyncTaskUpdateCartItem - These classes handles the asynchronous requests for Cart Module.
	* Listeners - Interfaces to handled the asynchronous API call requests.
* Category (com.optimusinfo.elasticpath.cortex.category) - This package handles the functionality related to categories and subcategories modules.
* Checkout (com.optimusinfo.elasticpath.cortex.checkout) - This package handles the functionality related to checkout modules.
* Product Listing (com.optimusinfo.elasticpath.cortex.product.listing) - This package handles the functionality related to the showing the product listing under each category.
* Product Details (com.optimusinfo.elasticpath.cortex.product.details) - This module handles the functionality related to showing of product definitions
* Profile (com.optimusinfo.elasticpath.cortex.profile) - This package handles the functionality related to profile module.
* Address (com.optimusinfo.elasticpath.cortex.profile.address) - This package handles the functionality for adding and editing address.
* Purchase (com.optimusinfo.elasticpath.cortex.purchase) - This package handles the order details and confirmation related functionality.
* Logout (com.optimusinfo.elasticpath.cortex.profile.logout) - This package contains the LogoutFragment.java which shows the user the Logout Modal.
* Utilities (com.optimusinfo.elasticpath.cortex.common , com.optimusinfo.elasticpath .cortex.common.imageutils ) - This package contains the commonly used methods like ImageUtilities, Notification Utilities, Server Utilities.
* Configuration (com.optimusinfo.elasticpath.cortex.configuration) - This package contains the modules related to setting of the common configuration settings for the applications.

## Configuration Settings
	
The configuration file is present in “Assets” -> config.json file. The structure of this file is same as that of the config file from HTML5 storefront. 

User can configure the following parameters

* Path
* Scope
* Endpoint
* Role

~~~
"cortexApi":{
	"path":"integrator",
	"scope":"campus",
	"endpoint":  "http://54.200.118.70:8080/cortex/",
	"role": "PUBLIC"
}
~~~

{% include legal.html %}

