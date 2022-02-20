package appsandapps.artistbrainz.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val MyColours = lightColors(
  primary = DarkestBlue,
  onPrimary = Color.White,

  primaryVariant = DarkestBlue,
  secondaryVariant = DarkestBlue,

  secondary = DarkestBlue,
  onSecondary = Color.White,

  background = OffWhite,
  onBackground = Color(0xFF666666),

  surface = OffWhiteSurface,
  onSurface = Color(0xFF444444),
)

@Composable
fun ArtistBrainzTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  content: @Composable() () -> Unit
) {
  MaterialTheme(
    colors = MyColours,
    //typography = MyTypography,
    //shapes = Shapes,
    content = content
  )
}