package com.prashant.openapi.activity


/**

 *Defines an interface for showing or hiding a loader.
 *Any class that implements this interface must provide an implementation for the [showLoader] function.
 *@property boolean A boolean value indicating whether to show or hide the loader.
 */
interface ShowLoader {
    fun showLoader(boolean: Boolean)
}