package com.example.redbooklite

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.fragment.app.Fragment
import com.example.redbooklite.ui.feed.FeedFragment
//import com.example.redbooklite.ui.profile.ProfileFragment
//import com.example.redbooklite.ui.publish.PublishActivity

class MainActivity : AppCompatActivity() {

    private lateinit var tvPageTitle: TextView
    private lateinit var tvTabHome: TextView
    private lateinit var tvTabMarket: TextView
    private lateinit var tvTabMessage: TextView
    private lateinit var tvTabMe: TextView

    private val feedFragment = FeedFragment()
//    private val profileFragment = ProfileFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, true)
        setContentView(R.layout.activity_main)

        findViewById<android.view.View>(R.id.bottomNav)?.apply {
            elevation = 0f
            translationZ = 0f
        }

        tvPageTitle = findViewById(R.id.tvPageTitle)
        tvTabHome = findViewById(R.id.tvTabHome)
        tvTabMarket = findViewById(R.id.tvTabMarket)
        tvTabMessage = findViewById(R.id.tvTabMessage)
        tvTabMe = findViewById(R.id.tvTabMe)

        tvTabHome.setOnClickListener {
            showFragment(feedFragment, getString(R.string.feed_title))
            updateTabStyle(Tab.HOME)
        }
        tvTabMarket.setOnClickListener {
            showDevelopingToast()
        }
//        findViewById<android.view.View>(R.id.tabPublish).setOnClickListener {
//            startActivity(Intent(this, PublishActivity::class.java))
//        }
        findViewById<android.view.View>(R.id.tabMessage).setOnClickListener {
            showDevelopingToast()
        }
//        tvTabMe.setOnClickListener {
//            showFragment(profileFragment, getString(R.string.profile_title))
//            updateTabStyle(Tab.ME)
//        }

        if (savedInstanceState == null) {
            showFragment(feedFragment, getString(R.string.feed_title))
            updateTabStyle(Tab.HOME)
        }
    }

    private fun showFragment(fragment: Fragment, title: String) {
        tvPageTitle.text = title
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }

    private enum class Tab {
        HOME, ME
    }

    private fun updateTabStyle(selected: Tab) {
        val activeColor = ContextCompat.getColor(this, R.color.text_primary)
        val inactiveColor = ContextCompat.getColor(this, R.color.nav_inactive)

        setTabTextStyle(tvTabHome, selected == Tab.HOME, activeColor, inactiveColor)
        setTabTextStyle(tvTabMarket, false, activeColor, inactiveColor)
        setTabTextStyle(tvTabMessage, false, activeColor, inactiveColor)
        setTabTextStyle(tvTabMe, selected == Tab.ME, activeColor, inactiveColor)
    }

    private fun setTabTextStyle(
        textView: TextView,
        isSelected: Boolean,
        activeColor: Int,
        inactiveColor: Int
    ) {
        if (isSelected) {
            textView.setTextColor(activeColor)
            textView.setTypeface(null, Typeface.BOLD)
        } else {
            textView.setTextColor(inactiveColor)
            textView.setTypeface(null, Typeface.NORMAL)
        }
    }

    private fun showDevelopingToast() {
        Toast.makeText(this, R.string.developing_toast, Toast.LENGTH_SHORT).show()
    }
}