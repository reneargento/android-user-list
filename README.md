# Android User List

Application that fetches a list of random users from a server and shows them in a list.  
Tapping on a user navigates to the user details screen.

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

# LICENSE
```
MIT License

Copyright (c) 2021 Rene Argento

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
