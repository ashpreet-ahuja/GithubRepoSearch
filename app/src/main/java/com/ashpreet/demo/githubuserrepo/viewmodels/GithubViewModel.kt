package com.ashpreet.demo.githubuserrepo.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ashpreet.demo.githubuserrepo.utils.Utils
import com.ashpreet.demo.githubuserrepo.models.GitHubRepo
import com.ashpreet.demo.githubuserrepo.repository.GitHubRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GitHubViewModel @Inject constructor(
    app: Application,
    private val repository: GitHubRepository
) : AndroidViewModel(app) {

    val repoList = MutableLiveData<List<GitHubRepo>?>()
    val errorMessage = MutableLiveData<String>()

    fun fetchRepos(username: String) {
        viewModelScope.launch {
            if (Utils.isNetworkAvailable(getApplication())){
                try {
                    val repos = repository.getUserRepos(username)
                    if (repos != null) {
                        repoList.postValue(repos)
                    } else {
                        errorMessage.postValue("Failed to fetch repositories")
                    }
                } catch (e: Exception) {
                    errorMessage.postValue(e.message ?: "Unknown error")
                }
            }
            else{
                errorMessage.postValue("Check if device is connected to internet!")
            }

        }
    }
}