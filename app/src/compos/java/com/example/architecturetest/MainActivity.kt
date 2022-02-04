package com.example.architecturetest

import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import com.example.architecturetest.ui.theme.ArchitectureTestTheme

class MainActivity : androidx.activity.ComponentActivity() {

  override fun onCreate(savedInstanceState: android.os.Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      ArchitectureTestTheme() {
        Surface(color = MaterialTheme.colors.background) {
          Navigation()
        }
      }
    }
  }
}