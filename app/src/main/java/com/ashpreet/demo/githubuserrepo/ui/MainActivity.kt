package com.ashpreet.demo.githubuserrepo.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ashpreet.demo.githubuserrepo.viewmodels.GitHubViewModel
import com.ashpreet.demo.githubuserrepo.adapters.GithubRepoItemAdapter
import com.ashpreet.demo.githubuserrepo.adapters.PastSearchAdapter
import com.ashpreet.demo.githubuserrepo.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityMainBinding

    private val viewModel: GitHubViewModel by viewModels()

    private lateinit var searchInput: EditText
    private lateinit var pastSearchesRecycler: RecyclerView
    private lateinit var githubRepoRecycler: RecyclerView
    private lateinit var pastSearchesContainer: View
    private lateinit var adapter: GithubRepoItemAdapter

    private val pastSearches = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        githubRepoRecycler = viewBinding.repoItemRecyclerView
        githubRepoRecycler.layoutManager = LinearLayoutManager(this)


        viewModel.repoList.observe(this) { repos ->
            repos?.let {
                Log.d("GitHubRepo", "Repositories: $it")
                // Set up RecyclerView
                if (it.isNotEmpty()){
                    adapter = GithubRepoItemAdapter(it)
                    githubRepoRecycler.adapter = adapter
                    setupScrollListener()
                }
                else{
                    showToast("No repositories found for this query")
                }

            }
        }

        viewModel.errorMessage.observe(this) { error ->
            Log.e("GitHubRepo", "Error: $error")
            showToast(error.toString())
        }

        searchInput = viewBinding.layoutSearchBar.searchInput
        pastSearchesRecycler = viewBinding.layoutSearchBar.pastSearchesRecycler
        pastSearchesContainer = viewBinding.layoutSearchBar.pastSearchesContainer

        // Set up RecyclerView
        pastSearchesRecycler.layoutManager = LinearLayoutManager(this)
        pastSearchesRecycler.adapter = PastSearchAdapter(pastSearches) { selectedSearch ->
            searchInput.setText(selectedSearch)  // Autofill search bar
            pastSearchesContainer.visibility = View.GONE  // Hide past searches
        }

        // Show past searches when search bar is focused
        searchInput.setOnFocusChangeListener { _, hasFocus ->
            if (pastSearches.isNotEmpty()){
                pastSearchesContainer.visibility = if (hasFocus) View.VISIBLE else View.GONE
            }
        }

        viewBinding.layoutSearchBar.searchIcon.setOnClickListener{
            val searchText = viewBinding.layoutSearchBar.searchInput.text.toString().trim()

            if (searchText.isNotEmpty() && !pastSearches.contains(searchText)) {
                pastSearches.add(0, searchText) // Add at the top (newest first)
                pastSearchesRecycler.adapter?.notifyItemInserted(0) // Notify adapter
            }

            viewModel.fetchRepos(searchText)
            searchInput.clearFocus()
        }

    }

    private fun showToast(message: String){
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    private fun setupScrollListener() {
        githubRepoRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0) {
                    adapter.loadMoreItems()  // Load next batch
                }
            }
        })
    }
}