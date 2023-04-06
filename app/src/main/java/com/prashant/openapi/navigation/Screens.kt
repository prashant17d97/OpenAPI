package com.prashant.openapi.navigation


/**
 *Sealed class that defines different screens in the app, along with their names and routes.

 *@param name The name of the screen.

 *@param route The route to navigate to the screen.
 */
sealed class Screens(val name: String, val route: String) {

    /**
     *Object that represents the Search Screen.
     */
    object SearchScreen : Screens(name = Search, route = Search)


    companion object {
        /**
         * Constant that represents the name of the Search Screen.
         */
        const val Search = "Search"
    }
}