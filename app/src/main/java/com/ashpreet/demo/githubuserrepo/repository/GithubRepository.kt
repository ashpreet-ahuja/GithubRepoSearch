package com.ashpreet.demo.githubuserrepo.repository

import android.util.Log
import com.ashpreet.demo.githubuserrepo.models.GitHubRepo
import com.ashpreet.demo.githubuserrepo.api.ApiService
import javax.inject.Inject

class GitHubRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun getUserRepos(username: String): List<GitHubRepo>? {
        val response = apiService.getUserRepos(username)
        Log.d("GithubRepo: ", response.body()?.joinToString("\n") ?: "Empty response")
        return if (response.isSuccessful) {
            response.body()
        } else {
            null
        }
    }
}