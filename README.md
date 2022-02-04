
Architecture
---

* The Composable calls methods on its UiState 
* The UiState deals with UI logic
* The UiState talks to the ViewModel
* The ViewModel has initialised a Repository via the Application's ServiceLocator
* The ServiceLocator gives a different Repository based on the build flavour
* The ViewModel calls the Repository
* The Repository updates its state
* The Repository updates its MutableStateFlow
* The ViewModel reacts to the MutableStateFlow
* The ViewModel then updates the UiState 
* The Composable is updated

Notes
---

* The `UiState` classes have `Values` class so they can be parcelised easily
* The `UiState` persists through Activity death via `SavedStateHandle` - a callback to the UiState ensures this persistence
* The `ServiceLocator` is used as Google recommends in its architecture app; Hilt or Dagger could be used
* Composables take "slots" so you're not passing down state through multiple composable layers
* The *`Screen` classes thread together the uistate, viewmodels, composable content and other screen level dependencies
* The repositories don't take data sources, or use data objects, as this app is simple, but they could easily

Sources
---

* Google's UiState, *Screen and ViewModel video: https://www.youtube.com/watch?v=mymWGMy9pYI&feature=youtu.be
* Google's ServiceLocator: https://github.com/android/architecture-samples/blob/main/app/src/main/java/com/example/android/architecture/blueprints/todoapp/TodoApplication.kt
* Jetpack compose's "slots": https://developer.android.com/jetpack/compose/layouts/basics#slot-based-layouts
* Jetpack compose's uiState tutorial: https://developer.android.com/jetpack/compose/state#state-hoisting

