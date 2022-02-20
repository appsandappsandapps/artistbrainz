package appsandapps.artistbrainz

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import appsandapps.artistbrainz.ui.artistdetail.ArtistDetailScreen
import appsandapps.artistbrainz.ui.homepage.HomepageScreen

lateinit var NavControllerLocal: ProvidableCompositionLocal<NavHostController>

/**
 * Either go to the main screen (view pager)
 * or the artist detail page, passing an ID
 */
@Composable
fun AppNavigationScreen() {
  val controller = rememberNavController()
  NavControllerLocal = compositionLocalOf { controller }
  CompositionLocalProvider(NavControllerLocal provides controller) {
    NavHost(navController = controller,
      startDestination = "mainpage") {
      composable("mainpage") { HomepageScreen() }
      composable("artistdetail/{artistId}", listOf(navArgument("artistId") {
        type = NavType.StringType
      }))
      {
        ArtistDetailScreen(
          it.arguments!!.getString("artistId")!!
        )
      }
    }
  }
}
