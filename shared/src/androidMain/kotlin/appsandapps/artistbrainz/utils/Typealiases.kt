package appsandapps.artistbrainz.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.parcelize.Parcelize

/**
 * These all differ in a KMM project
 */

actual typealias Parcelable =  android.os.Parcelable
actual typealias Parcelize = kotlinx.parcelize.Parcelize
// Okay okay, this is a value
actual val IODispatcher get() = Dispatchers.IO

