package com.example.architecturetest

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.architecturetest.ui.textinput.TextInputScreen
import com.example.architecturetest.ui.textlist.TextListScreen

@Composable
fun Navigation() {
  val navController = rememberNavController()
  NavHost(
    navController = navController,
    startDestination = "dashboard"
  ) {
    composable("about") {
      AboutContent(navController)
    }
    composable("dashboard") {
      Dashboard(navController)
    }
  }
}

@Composable
private fun Dashboard(navController: NavHostController) {
  DashboardContent(
    top = { modifier ->
      TextInputScreen(navController, modifier)
    },
    bottom = { modifier ->
      TextListScreen(navController, modifier)
    },
  )
}
