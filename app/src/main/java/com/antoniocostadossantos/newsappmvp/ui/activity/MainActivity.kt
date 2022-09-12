package com.antoniocostadossantos.newsappmvp.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.antoniocostadossantos.newsappmvp.R
import com.antoniocostadossantos.newsappmvp.ui.adapter.MainAdapter
import com.antoniocostadossantos.newsappmvp.databinding.ActivityMainBinding
import com.antoniocostadossantos.newsappmvp.local.model.Article
import com.antoniocostadossantos.newsappmvp.repository.NewsDataSource
import com.antoniocostadossantos.newsappmvp.presenter.ViewHome
import com.antoniocostadossantos.newsappmvp.presenter.news.NewsPresenter

class MainActivity : AppCompatActivity() {
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews(binding)
    }

    private fun initViews(binding: ActivityMainBinding) {
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController
        navController.addOnDestinationChangedListener { _, destination, _ ->
            title = destination.label
        }

        binding.bottomNavigation.apply {
            setupWithNavController(navController)
            setOnNavigationItemReselectedListener { }
        }
    }


}