package com.yuan.tafewallet

import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.yuan.tafewallet.ui.history.HistoryFragment
import com.yuan.tafewallet.ui.home.HomeFragment
import com.yuan.tafewallet.ui.refund.RefundFragment
import com.yuan.tafewallet.ui.topup.TopupFragment

class MainActivity : AppCompatActivity() {
    private var navController: FrameLayout? = null

    private lateinit var homeFragment: HomeFragment
    private lateinit var topupFragment: TopupFragment
    private lateinit var historyFragment: HistoryFragment
    private lateinit var refundFragment: RefundFragment

    private val mOnNavigationItemSelectedListener = object : BottomNavigationView.OnNavigationItemSelectedListener {
        override fun onNavigationItemSelected(item: MenuItem): Boolean {
            when (item.itemId) {
                R.id.navigation_home -> {
                    setFragment(homeFragment)
                    return true
                }
                R.id.navigation_topup -> {
                    setFragment(topupFragment)
                    return true
                }
                R.id.navigation_history -> {
                    setFragment(historyFragment)
                    return true
                }
                R.id.navigation_refund -> {
                    setFragment(refundFragment)
                    return true
                }
            }
            return false
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        navController = findViewById(R.id.nav_host_fragment)

        homeFragment = HomeFragment()
        topupFragment = TopupFragment()
        historyFragment = HistoryFragment()
        refundFragment = RefundFragment()

        val navigation = findViewById(R.id.nav_view) as BottomNavigationView
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)


        setFragment(homeFragment)
    }

    private fun setFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment, fragment)
            .addToBackStack(null)
            .commit()
    }

    fun gotoFragment(fragment: Fragment, tag: String) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment, fragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .addToBackStack(null)
            .commit()
    }
}
