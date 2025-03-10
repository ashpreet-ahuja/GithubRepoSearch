GitHub Repositories Viewer

A modern Android application built with Kotlin that follows the MVVM (Model-View-ViewModel) architecture. This app fetches a user's public repositories from GitHub using the GitHub API and supports pagination for seamless browsing.

Features

Fetch and display repositories of a GitHub user.

MVVM architecture for better separation of concerns.

Dependency injection using Hilt.

Asynchronous network calls with Kotlin Coroutines.

Lifecycle-aware components.

Navigation Component for seamless navigation.

Pagination.

Tech Stack

Language: Kotlin

Architecture: MVVM (Model-View-ViewModel)

Dependency Injection: Hilt

Asynchronous Programming: Kotlin Coroutines, Flow

Networking: Retrofit, OkHttp

Navigation: Jetpack Navigation Component

Paging

UI Components: Jetpack Compose / XML Views (choose one based on your UI preference)

Getting Started

Prerequisites

Android Studio Flamingo or later.

API Key (if needed, but GitHub API usually works without authentication for public repos).

Installation
Clone the repository:

git clone https://github.com/your-username/your-repo.git

Open the project in Android Studio.

Sync Gradle dependencies.

Run the application on an emulator or physical device.

API Integration

The app fetches data from GitHub using the following endpoint:
https://api.github.com/users/{username}/repos

A sample app video:
https://github.com/user-attachments/assets/11f82de9-07f7-434b-8cf1-54a6d5a58a66

