package com.saviola44.task.data.repository.search.pagination

import android.util.Log
import androidx.paging.PageKeyedDataSource
import com.saviola44.task.App
import com.saviola44.task.R
import com.saviola44.task.data.model.remote.Repo
import com.saviola44.task.data.repository.ServiceFactory
import com.saviola44.task.data.repository.ApiService
import com.saviola44.task.ui.common.Action
import com.saviola44.task.ui.common.SetProgressBarVisibility
import com.saviola44.task.ui.common.ShowError
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import java.net.UnknownHostException

class SearchDataSource(private val compositeDisposable: CompositeDisposable,
                       var query: String,
                       val actionHandler: (Action) -> Unit) : PageKeyedDataSource<Long, Repo>() {
    val apiService = ServiceFactory.createService(ApiService::class.java)


    override fun loadInitial(params: LoadInitialParams<Long>, callback: LoadInitialCallback<Long, Repo>) {
        executeQuery(1) { repoList ->
            callback.onResult(repoList, null, FIRST_PAGE + 1)
        }
    }

    override fun loadAfter(params: LoadParams<Long>, callback: LoadCallback<Long, Repo>) {
        executeQuery(params.key) { repoList ->
            callback.onResult(repoList, params.key+1)
        }
    }

    override fun loadBefore(params: LoadParams<Long>, callback: LoadCallback<Long, Repo>) {
        executeQuery(params.key) { repoList ->
            val adjacentKey = if(params.key>1) params.key else null
            callback.onResult(repoList, adjacentKey)
        }
    }


    private fun executeQuery(page: Long, callback: (List<Repo>) -> Unit) {
        if(query.isBlank())  {
            callback(arrayListOf())
        } else {
            actionHandler(SetProgressBarVisibility(true))
            val disposable = apiService.getRepos(page, PAGE_SIZE, query).subscribeBy(
                onNext = { repoList ->
                    callback(repoList.items)
                    actionHandler(SetProgressBarVisibility(false))
                }, onError = { error ->
                    actionHandler(SetProgressBarVisibility(false))
                    handleError(error)
                }
            )
            compositeDisposable.add(disposable)
        }
    }

    private fun handleError(error: Throwable) {
        Log.d(TAG, error.message + ", " + error.localizedMessage)
        when(error) {
            is UnknownHostException -> {
                actionHandler(ShowError(App.appCtx().getString(R.string.error_no_internet_connection)))
            }else -> {
                actionHandler(ShowError(App.appCtx().getString(R.string.error_occured)))
            }
        }
    }

    companion object {
        val TAG = SearchDataSource::class.java.simpleName
        const val PAGE_SIZE = 8
        private val FIRST_PAGE = 1L
    }
}