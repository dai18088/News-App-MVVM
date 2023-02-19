package com.example.newsapiclient.domain.usecase

import android.app.DownloadManager.Query
import com.example.newsapiclient.data.model.APIResponse
import com.example.newsapiclient.data.util.Resource
import com.example.newsapiclient.domain.repository.NewsRepository

class GetSearchedNewsUseCase(private val newsRepository: NewsRepository) {

    suspend fun execute(county: String, searchQuery: String, page: Int): Resource<APIResponse>{
        return newsRepository.getSearchNews(county, searchQuery, page)
    }

}