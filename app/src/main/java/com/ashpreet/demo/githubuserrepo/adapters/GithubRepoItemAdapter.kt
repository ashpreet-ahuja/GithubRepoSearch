package com.ashpreet.demo.githubuserrepo.adapters

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ashpreet.demo.githubuserrepo.R
import com.ashpreet.demo.githubuserrepo.databinding.ItemGithubRepoBinding
import com.ashpreet.demo.githubuserrepo.models.GitHubRepo
import com.squareup.picasso.Picasso
import kotlin.math.min

class GithubRepoItemAdapter(
    private val githubRepoList: List<GitHubRepo>,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val displayedRepos = mutableListOf<GitHubRepo>() // Items currently displayed
    private val itemsPerPage = 4 // Load 4 items at a time
    private var isLoading = false

    private val ITEM_TYPE_REPO = 0
    private val ITEM_TYPE_LOADER = 1

    init {
        loadMoreItems()  // Load first set of items
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return if (viewType == ITEM_TYPE_REPO) {
            val binding = ItemGithubRepoBinding.inflate(inflater, parent, false)
            GithubRepoItemViewHolder(binding)
        } else {
            val view = inflater.inflate(R.layout.item_loader, parent, false)
            LoaderViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is GithubRepoItemViewHolder) {
            val githubItem = displayedRepos[position] // Use displayed items
            holder.bind(githubItem)
        }
    }

    override fun getItemCount(): Int = displayedRepos.size + if (isLoading) 1 else 0

    override fun getItemViewType(position: Int): Int {
        return if (position < displayedRepos.size) ITEM_TYPE_REPO else ITEM_TYPE_LOADER
    }

    inner class LoaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    inner class GithubRepoItemViewHolder(private var viewBinding: ItemGithubRepoBinding) : RecyclerView.ViewHolder(viewBinding.root) {

        fun bind(item: GitHubRepo) {
            with(viewBinding) {
                textRepoName.text = item.owner.login ?: ""
                textRepoTitle.text = item.name ?: ""
                textRepoDescription.text = item.description ?: ""
                textStars.text = item.stargazersCount?.toString() ?: ""
                textForks.text = item.forksCount?.toString() ?: ""
                textLanguage.text = item.language ?: ""

                // Visibility handling
                textRepoDescription.visibility = if (item.description.isNullOrEmpty()) View.GONE else View.VISIBLE
                textStars.visibility = if (item.stargazersCount == null) View.GONE else View.VISIBLE
                textForks.visibility = if (item.forksCount == null) View.GONE else View.VISIBLE
                textLanguage.visibility = if (item.language.isNullOrEmpty()) View.GONE else View.VISIBLE

                val density = root.resources.displayMetrics.density.toInt()
                val imageUrl = item.owner.avatarUrl

                if(imageUrl.isNotEmpty()){
                    Picasso.get()
                        .load(imageUrl)
                        .resize(density * 60, density * 60)
                        .centerCrop()
                        .placeholder(R.drawable.git)
                        .error(R.drawable.git)
                        .into(viewBinding.repoIcon)
                }
                else{
                    viewBinding.repoIcon.setImageResource(R.drawable.git)
                }

            }
        }
    }

    fun loadMoreItems() {
        if (isLoading || displayedRepos.size >= githubRepoList.size) return

        isLoading = true
        notifyItemInserted(displayedRepos.size) // Show loader

        Handler(Looper.getMainLooper()).postDelayed({
            val start = displayedRepos.size
            val end = min(start + itemsPerPage, githubRepoList.size)

            displayedRepos.addAll(githubRepoList.subList(start, end))
            isLoading = false
            notifyDataSetChanged() // Refresh UI after loading new items
        }, 1500) // Delay of 1.5 seconds to simulate loading
    }
}