package appsandapps.artistbrainz.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

val PREFS_NAME_SETTINGS = "settings"
val PREF_KEY_COMPOSE_TOGGLE = booleanPreferencesKey("compose_version_toggle")
// As advised by the docs
val Context.prefsDataStore: DataStore<Preferences>
  by preferencesDataStore(name = PREFS_NAME_SETTINGS)
// So we can use a stateflow in places
suspend fun Context.composeToggleSetting(scope: CoroutineScope): StateFlow<Boolean> =
  prefsDataStore.data
    .map { it[PREF_KEY_COMPOSE_TOGGLE] ?: false }
    .stateIn(scope)
