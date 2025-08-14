# Roku App

This is a simple Android application that demonstrates modern Android development practices. The app fetches a list of applications from a remote server and displays them in a list, showing the app's name, ID, and image.

## Features

- Fetches app data from a remote JSON endpoint.
- Displays the apps in a scrollable list using Jetpack Compose.
- Uses Coil to asynchronously load and display images.
- Implements the MVVM architecture pattern.
- Handles loading and error states gracefully.

## Technologies Used

- [Kotlin](https://kotlinlang.org/)
- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Retrofit](https://square.github.io/retrofit/)
- [Kotlinx Serialization](https://github.com/Kotlin/kotlinx.serialization)
- [Coil](https://coil-kt.github.io/coil/)
- [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel)
- [StateFlow](https://developer.android.com/kotlin/flow/stateflow-and-sharedflow)

## Architecture: MVVM

The application follows the Model-View-ViewModel (MVVM) architectural pattern, which separates the UI from the business logic and data.

-   **Model**: Represents the data layer of the application. It is responsible for fetching data from the network and providing it to the ViewModel. In this app, the Model consists of:
    -   `App`: The data class that represents a single application.
    -   `Repository`: A singleton object that handles the logic for fetching data from the network.
    -   `ApiService`: A Retrofit interface that defines the API endpoints.
    -   `ApiResponse`: A data class that represents the response from the API.

-   **View**: The UI layer of the application, built with Jetpack Compose. It is responsible for displaying the data to the user and handling user interactions. The View observes the ViewModel for state changes and updates the UI accordingly. The main components of the View are:
    -   `MainActivity`: The main activity of the app, which hosts the Jetpack Compose UI.
    -   `AppScreen`: The main screen of the app, which displays the list of apps.
    -   `AppList`: A composable that displays the list of apps.
    -   `AppItem`: A composable that displays a single app in the list.

-   **ViewModel**: The `AppViewModel` acts as a bridge between the Model and the View. It is responsible for fetching data from the Model, managing the UI state, and exposing it to the View. The ViewModel uses a `StateFlow` to hold the UI state, which is observed by the View.

## Class Flow Logic

Here is a diagram that illustrates the flow of data and interactions between the main classes in the application:

```mermaid
graph TD
    A[View (MainActivity/Composables)] -- Observes State --> B(ViewModel - AppViewModel);
    B -- Calls getApps() --> C(Repository);
    C -- Uses --> D(Network - ApiService);
    D -- Makes API Call --> E[Remote API];
    E -- Returns Data --> D;
    D -- Returns Data --> C;
    C -- Returns Data --> B;
    B -- Updates UI State --> A;
    A -- Displays Image Using --> F(Coil - AsyncImage);

```

## Unit Tests

This project includes comprehensive unit tests following Android testing best practices. The tests cover all layers of the MVVM architecture and ensure code reliability and maintainability.

### Test Coverage

**Unit Tests Created:**
* **📱 AppTest.kt** - Model serialization/deserialization tests
* **🗂️ RepositoryTest.kt** - Repository layer tests with mocked API
* **🧠 AppViewModelTest.kt** - ViewModel business logic and state management tests
* **🌐 ApiServiceTest.kt** - Network layer tests with MockWebServer
* **📊 AppStateTest.kt** - State data class functionality tests
* **⚙️ ProjectSetupTest.kt** - Basic project setup verification

### Testing Libraries Used

- **JUnit 4** - Core testing framework
- **MockK** - Kotlin mocking library for unit tests
- **Coroutines Test** - Testing utilities for Kotlin coroutines
- **Turbine** - Testing library for Kotlin Flows
- **MockWebServer** - Mock HTTP server for API testing
- **AndroidX Test Core** - Android testing utilities

### Running Tests

```bash
# Run all unit tests
./gradlew test

# Run tests for debug variant only
./gradlew testDebugUnitTest

# Run tests with detailed output
./gradlew test --info

# Run tests with coverage report
./run_tests.sh --coverage

# Run specific test class
./gradlew test --tests "com.android.rokuapp.viewmodel.AppViewModelTest"

# Clean and run tests
./gradlew clean test
```

### Test Reports

After running tests, you can find detailed reports at:
- **Test Results:** `app/build/reports/tests/testDebugUnitTest/index.html`
- **Coverage Report:** `app/build/reports/coverage/test/debug/index.html`

### Key Testing Features

- ✅ **Dependency Injection** - Tests use proper mocking with injected dependencies
- ✅ **Coroutine Testing** - Proper handling of async operations with TestDispatcher
- ✅ **Flow Testing** - StateFlow testing with Turbine for reactive streams
- ✅ **Network Testing** - Mock API responses with MockWebServer
- ✅ **Error Handling** - Comprehensive error scenario testing
- ✅ **State Management** - UI state transitions and loading states
- ✅ **Data Validation** - JSON serialization and model validation

## How to Build and Run

1.  Clone the repository:
    ```bash
    git clone https://github.com/your-username/RokuApp.git
    ```
2.  Open the project in Android Studio.
3.  Let Gradle sync the project dependencies.
4.  Run the `app` configuration on an Android emulator or a physical device.
