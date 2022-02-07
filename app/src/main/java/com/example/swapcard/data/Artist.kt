package com.example.swapcard.data

import java.util.*

data class Artist (
  val id: String = "",
  val name: String = "",
  val bookmarked: Boolean = false,
  val disambiguation: String = "",
  val rating: Rating = Rating(),
  val error: String = "",
  val resultData: Long = Date().time,
)