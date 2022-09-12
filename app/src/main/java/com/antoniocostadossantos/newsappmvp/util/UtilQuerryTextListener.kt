package com.antoniocostadossantos.newsappmvp.util

import android.widget.SearchView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.coroutineScope
import com.antoniocostadossantos.newsappmvp.util.Constants.Companion.SEARCH_NEWS_DELAY
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

internal class UtilQuerryTextListener(
    lifecycle: Lifecycle,
    private val utilQuerryTextListener: (String?) -> Unit
) : SearchView.OnQueryTextListener, LifecycleObserver {

    private val coroutines = lifecycle.coroutineScope
    private var searchJob: Job? = null

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        searchJob?.cancel()
        searchJob = coroutines.launch {
            newText?.let {
                delay(SEARCH_NEWS_DELAY)
                utilQuerryTextListener(newText)
            }
        }
        return false
    }

}