package appsandapps.artistbrainz.utils.ext

/**
 * Eases making ViewModels with
 * - Application Context
 * - SavedStateHandle
 * - default parameters.
 * in Composable
 *
 * USED: in the *Screen composibles to start the view models
 * for the component.
 */
/*
@Composable
inline fun <reified VM: ViewModel> viewModelWithSavedState(
  crossinline f: (Application, SavedStateHandle) -> VM
) : VM {
  val savedStateOwner = LocalSavedStateRegistryOwner.current
  val app = LocalContext.current.applicationContext as Application
  val fact  = object : AbstractSavedStateViewModelFactory(savedStateOwner, null) {
    override fun <VM : ViewModel?> create(
      key: String,
      modelClass: Class<VM>,
      handle: SavedStateHandle
    ): VM = f(app, handle) as VM
  }
  return viewModel<VM>(factory = fact)
}
*/