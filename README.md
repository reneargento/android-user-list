# Android User List

Application that fetches a list of random users from a server and shows them in a list.  
Tapping on a user navigates to the user details screen.

Debug APK for the application can be found here:  
https://drive.google.com/file/d/1pQGneJQg7SK2fysIrcMYjjn2ZfZkucBY/view?usp=sharing

You can also download and install the app by scanning the QRCode below:

<p align="center">
  <img src="https://user-images.githubusercontent.com/3593362/133911187-58ed3153-d3a4-4ec1-aa5d-f6ae2451c5ce.png" align="center" width=200>
</p>

## Architecture
The project's architecture is MVVM with Clean Architecture.

### Presentation Layer
On the presentation layer, View Models are used to handle data visualization. Activities and Fragments reactively interact with View Models, through the use of LiveData. The presentation layer communicates with the domain layer through UseCases, invoked by View Models.

### Domain Layer
This layer contains the "representation" of the business logic/features of the app. This representation exists through the use of UseCases. UseCases invoke the Data Layer (through repositories), map "data entities" to entities that are used in the rest of the app, and are kept as simple as possible. UseCases make use of mapper functions to properly map data objects to domain objects.

### Data Layer
Data Layer contains repositories, data types that map to external API types, and other things related to the app external communication. All API calls are dispatched from this layer exclusively. This layer is never accessed directly from the presentation layer. Repositories are used by UseCases.

### Dependency Injection
Hilt was chosen as the framework to facilitate dependency injection through the app.

## Components used

<b>View Binding</b> for binding views.  
<b>View Model</b> to store and manage UI-related data in a lifecycle conscious way.  
<b>Live Data</b> for observing data holder classes.  
<b>Jetpack Navigation</b> for navigating between screens.  
<b>Coroutines</b> for asynchronous operations.  
<b>Room</b> for the database storage.  
<b>DataStore</b> for persisting key-value pairs.  
<b>Retrofit</b> for making network calls.  
<b>Glide</b> for image loading and caching.  
<b>Hilt</b> for dependency injection.  
<b>JUnit</b> and <b>Mockito</b> for unit tests.  
<b>Espresso</b> for UI tests.  
<b>MockWebServer</b> for mocking web server responses to local json files.

## Screenshots

<p align="center">
  <img src="https://user-images.githubusercontent.com/3593362/133809484-13fa73f1-1ac8-4545-a0ce-bd549b73c784.png" align="center" width=200>
  <img src="https://user-images.githubusercontent.com/3593362/133910125-6debfe3f-c752-4d3a-ba24-34d14a42a618.png" align="center" width=200>
</p>
