package com.yuan.tafewallet

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.yuan.tafewallet.history.HistoryFragment
import com.yuan.tafewallet.history.HistoryTransactionsFragment
import com.yuan.tafewallet.home.HomeFragment
import com.yuan.tafewallet.refund.RefundFragment
import com.yuan.tafewallet.topup.TopupFragment

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
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navController = findViewById(R.id.nav_host_fragment)

        homeFragment = HomeFragment()
        topupFragment = TopupFragment()
        historyFragment = HistoryFragment()
        refundFragment = RefundFragment()

        val navigation = findViewById(R.id.nav_view) as BottomNavigationView
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)


        setFragment(homeFragment)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        if (supportFragmentManager == null) {
            super.onBackPressed()
            this.overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out)
            return
        }
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
            if (supportFragmentManager.backStackEntryCount == 1) {
                clearBackStack()
                supportActionBar!!.hide()
            }
        } else {
            super.onBackPressed()
            this.overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out)
        }
    }

    fun setFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment, fragment)
            .commit()
    }

    fun gotoFragment(fragment: Fragment, tag: String) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment, fragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .addToBackStack(null)
            .commit()
    }

    fun clearBackStack() {
        for (i in 0 until supportFragmentManager.backStackEntryCount) {
            supportFragmentManager.popBackStack()
        }
    }

    fun showAlert() {
        val alert = AlertDialog.Builder(this@MainActivity)
        alert.setTitle("Whoops...")
            .setMessage("Action failed, please try again later")
            .setPositiveButton(android.R.string.ok, null)
            .show()
    }
}
