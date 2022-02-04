package com.example.architecturetest.ui.about

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.architecturetest.R
import com.example.architecturetest.databinding.AboutBinding
import com.example.architecturetest.ui.textinput.TextInputFragment
import com.google.android.material.tabs.TabLayoutMediator

class AboutFragment : Fragment(R.layout.about) {

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    val bindings = AboutBinding.bind(view)
    val name:String = arguments?.let { it.getString("name") } ?: "DAVID"
    bindings.text.text = "It\'s an app, ${name}"
    val adapter = PagerAdapter(
      requireActivity(),
      listOf(Page1(), Page2(), TextInputFragment()),
    )
    bindings.viewpager2.adapter = adapter
    TabLayoutMediator(bindings.tabLayout, bindings.viewpager2) { tab, pos ->
      tab.text = "Tab ${pos}"
    }.attach()
  }

}

private class PagerAdapter(
  fa: FragmentActivity,
  val frags: List<Fragment>,
) : FragmentStateAdapter(fa) {
  override fun getItemCount(): Int = frags.size
  override fun createFragment(position: Int): Fragment = frags[position]
}

class Page1() : Fragment(R.layout.page1) {}
class Page2() : Fragment(R.layout.page2) {}
class Page3() : Fragment(R.layout.page3) {}
