package com.example.architecturetest.ui.textlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.architecturetest.R

class TextListRecyclerView(
  val items: List<String>,
  val clickListener: (String) -> Unit,
) : RecyclerView.Adapter<TextListRecyclerView.Holder>() {
  
  override fun onCreateViewHolder(vg: ViewGroup, type: Int): Holder {
    val v = LayoutInflater.from(vg.context)
      .inflate(R.layout.textlist_item, vg, false)
    return Holder(v)
  }

  override fun onBindViewHolder(hld: Holder, pos: Int) {
    hld.textView.text = items[pos]
  }

  override fun getItemCount(): Int = items.size

  inner class Holder(v: View): RecyclerView.ViewHolder(v) {
    val textView: TextView
    init {
      textView = v.findViewById(R.id.textlist_item_text)
    }
  }
}
