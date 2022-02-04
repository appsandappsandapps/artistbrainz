package com.example.swapcard.ui.searchlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.swapcard.R

class SearchListRecyclerView(
  public var items: List<SearchListUIState.ArtistUI>,
  val artistClick: (Int) -> Unit,
  val bookmarkListener: (Int) -> Unit,
  val debookmarkListener: (Int) -> Unit,
  val nextPaginateListener: () -> Unit,
) : RecyclerView.Adapter<SearchListRecyclerView.Holder>() {
  
  override fun onCreateViewHolder(vg: ViewGroup, type: Int): Holder {
    val v = LayoutInflater.from(vg.context)
      .inflate(R.layout.artist_search_item, vg, false)
    return Holder(v)
  }

  override fun onBindViewHolder(hld: Holder, pos: Int) {
    if(pos == items.size -1) nextPaginateListener()
    hld.textView.text = items[pos].name
    hld.checkBox.isChecked = items[pos].bookmarked
    hld.textView.setOnClickListener {
      artistClick(items[pos].id)
    }
    hld.checkBox.setOnClickListener {
      if(hld.checkBox.isChecked)
        bookmarkListener(items[pos].id)
      else
        debookmarkListener(items[pos].id)
    }
  }

  override fun getItemCount(): Int = items.size

  inner class Holder(v: View): RecyclerView.ViewHolder(v) {
    val textView: TextView
    val checkBox: CheckBox
    init {
      textView = v.findViewById(R.id.artist_name)
      checkBox = v.findViewById(R.id.checkbox)
    }
  }
}
