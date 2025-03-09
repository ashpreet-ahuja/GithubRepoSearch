package com.ashpreet.demo.githubuserrepo.api

import com.ashpreet.demo.githubuserrepo.models.GitHubRepo
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("users/{username}/repos")
    suspend fun getUserRepos(
        @Path("username") username: String
    ): Response<List<GitHubRepo>>

}