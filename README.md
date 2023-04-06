## OpenAPI Android App
This is a simple Android app that allows users to search for GitHub repositories using the GitHub open API. The app uses the MVVM architecture, Jetpack Compose for UI creation, Retrofit for RESTful API calling, and Hilt for dependency injection. It targets Android OS version 33.

## ScreenShots

<img src="https://user-images.githubusercontent.com/84988691/230418123-7e43f63e-cdb8-4f47-bcdd-8e2dc935c86f.png" width="250" height="450">
<img src="https://user-images.githubusercontent.com/84988691/230418101-6ad6e3d7-5eff-4ad2-8bdb-211b962cd45d.png" width="250" height="450">

## Features
- Search for GitHub repositories using keywords
- Display the search results in a list
- Show a detail card for each repository
- Show a message when no search results are found

## Getting Started
To run this app on your local machine, follow these steps:

1. Clone this repository onto your machine using the command: git clone https://github.com/prashant17d97/OpenAPI.git

2. Open the project in Android Studio
  
3. Build and run the app on an Android emulator or a physical device

## Requirements
- Android Studio
- Android emulator or physical device running Android OS version 33 or higher
- Personal Git token (required to access the GitHub API)

## How to Use
1. When the app is launched, you will see a search bar at the top of the screen
2. Enter a search query in the search bar and tap on the "Search" button
3. The app will request data from the GitHub open API and display the search results in a list
4. If no search results are found, the app will display a message saying "Nothing available to show"

## Architecture
This app uses the MVVM (Model-View-ViewModel) architecture to separate concerns and ensure testability. The app's architecture includes the following components:

- View: This layer represents the UI of the app and is implemented using Jetpack Compose.
- ViewModel: This layer provides data to the UI and contains the app's business logic. It interacts with the repository to get data and updates the UI accordingly.
- Repository: This layer abstracts the data sources and provides a clean API for the ViewModel to use. In this app, the repository retrieves data from the GitHub open API using Retrofit.
- Model: This layer defines the data models used in the app. In this app, the data model is the repository data retrieved from the GitHub open API.

## Dependencies
This app uses the following dependencies:

- Hilt: A dependency injection library for Android apps
- Retrofit: A type-safe HTTP client for Android and Java
- Gson: A JSON parsing library for Kotlin and Java
- Jetpack Compose: A modern UI toolkit for Android
- ViewModel: This provides data to the UI and contains the app's business logic. 
