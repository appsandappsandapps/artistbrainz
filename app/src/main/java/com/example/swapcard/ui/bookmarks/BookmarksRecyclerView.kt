package com.example.swapcard.ui.bookmarks

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.swapcard.R

class BookmarksRecyclerView(
  public var items: List<BookmarksUIState.BookmarkUI>,
  val artistClick: (String) -> Unit,
  val debookmarkListener: (String) -> Unit,
) : RecyclerView.Adapter<BookmarksRecyclerView.Holder>() {
  
  override fun onCreateViewHolder(vg: ViewGroup, type: Int): Holder {
    val v = LayoutInflater.from(vg.context)
      .inflate(R.layout.artist_search_item, vg, false)
    return Holder(v)
  }

  override fun onBindViewHolder(hld: Holder, pos: Int) {
    hld.textView.text = items[pos].name
    hld.checkBox.isChecked = true
    hld.checkBox.setOnClickListener {
      debookmarkListener(items[pos].id)
    }
    hld.textView.setOnClickListener {
      artistClick(items[pos].id)
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
