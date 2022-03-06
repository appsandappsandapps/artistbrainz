
! See the PDF for a full description of the architecture !
--

[Install on Google Play](https://play.google.com/store/apps/details?id=appsandapps.artistbrainz)

Use the project to install the iPhone app to your phone.

Architecture
---
It goes roughly:

1. The Composable/View/SwiftUI calls a method on its UiModel
2. The UiModel talks to the ViewModel
3. The ViewModel calls the Repository
4. The Repository updates its StateFlow
5. The ViewModel reacts to the StateFlow and updates the UiModel
6. The Composable/View is updated via UiModel's StateFlow

Multiple fragments / composables / screens observe the same StateFlow to update simultaneously.

The architecture can seemlessly be used by Jetpack Compose replacing the fragments for composables, and
reusing the viewmodels and uistate. And obviously iOS.

Dependency injection
----
* The `ServiceLocator` is used as Google recommends in its architecture app; Hilt or Dagger could be used
* We only have Application level dependencies so it works particularly easily and well
* The ServiceLocator gives a different Repository based on the build flavour

Notes
---

* The `UIModel` classes have `Values` class so they can be parcelised easily
* The `UIModel` persists through Activity death via `SavedStateHandle` - a callback to the UiState ensures this persistence

Sources
---

* Google's UiState, *Screen and ViewModel video: https://www.youtube.com/watch?v=mymWGMy9pYI&feature=youtu.be
* Google's ServiceLocator: https://github.com/android/architecture-samples/blob/main/app/src/main/java/com/example/android/architecture/blueprints/todoapp/TodoApplication.kt
