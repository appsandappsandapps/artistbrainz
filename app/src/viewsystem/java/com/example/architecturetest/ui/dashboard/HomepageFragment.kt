package com.example.architecturetest.ui.dashboard

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.apollographql.apollo3.ApolloClient
import com.example.architecturetest.ArtistsQuery
import com.example.architecturetest.R
import kotlinx.coroutines.launch

class HomepageFragment : Fragment(R.layout.homepage) {

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    lifecycleScope.launch{
      val apolloClient = ApolloClient.Builder()
        .serverUrl("https://graphbrainz.herokuapp.com/")
        .build()
      val resp = apolloClient.query(ArtistsQuery("Aphex Twin")).execute()
      val artists = resp.data?.search?.artists?.nodes
      ?.filter {
        it != null && it.artistBasicFragment != null
      }
      ?.map {
        it?.artistBasicFragment
      }
    }
  }

}

