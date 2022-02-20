package appsandapps.artistbrainz.utils.ext

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.fragment.app.Fragment

fun Activity.gotoUrl(url: String) {
  startActivity(Intent(Intent.ACTION_VIEW).apply {
    setData(
      Uri.parse(url)
    )
  })
}