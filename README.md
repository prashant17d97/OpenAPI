OpenAPI
This is an Android application that retrieves the repository from the GitHub open API with a personal git token. It is built using MVVM architecture, Jetpack Compose for UI creating, Retrofit for Restful API calling, a Repository for API request processing, Hilt for dependency injection, GSON for parsing and targeting OS version 33.

Table of Contents
Getting Started
Prerequisites
Installation
Usage
Architecture
Dependencies
Authors
License
Getting Started
To get started with the project, clone the repository and open it in Android Studio.

sh
Copy code
git clone https://github.com/username/OpenAPI.git
Prerequisites
Android Studio 4.2 or higher
Gradle 7.0.2 or higher
Android SDK 30 or higher
Installation
Open Android Studio and select "Open an existing Android Studio project".
Navigate to the cloned repository and select the OpenAPI folder.
Wait for the project to sync and build.
Run the application on an emulator or a physical device.
Usage
The application has a single screen that allows the user to search for repositories on GitHub. On the top, there is a search bar where the user can input a query. Below the search bar, there is a list of repository detail cards. If the query doesn't return any result, the message "Nothing available to show" will be displayed.

Architecture
The application is built using the Model-View-ViewModel (MVVM) architecture. This architecture allows us to separate the presentation logic from the business logic of the application.

Project structure
The project is structured as follows:

data: Contains the Repository class which handles the API request processing and the data classes used for parsing the JSON response from the API.
di: Contains the Hilt dependency injection module.
ui: Contains the Composable functions for the UI components of the application and the ViewModel class used for handling the data logic.
Dependencies
The following dependencies were used in the project:

Kotlin
Jetpack Compose
Retrofit
GSON
Hilt
Authors
John Doe - Github
License
This project is licensed under the MIT License - see the LICENSE file for details.