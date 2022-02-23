package appsandapps.artistbrainz.utils

import kotlinx.coroutines.CoroutineDispatcher

/**
 * These all differ in a KMM project
 */

expect interface Parcelable

@OptIn(ExperimentalMultiplatform::class)
@OptionalExpectation
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.BINARY)
expect annotation class Parcelize()

expect val IODispatcher: CoroutineDispatcher

