package com.saviola44.task.data.repository

import com.saviola44.task.data.model.remote.RepoResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("/search/repositories")
    fun getRepos(@Query("page") page: Long,
                 @Query("per_page") perPage: Int,
                 @Query("q" )query: String = "music"): Observable<RepoResponse>
}