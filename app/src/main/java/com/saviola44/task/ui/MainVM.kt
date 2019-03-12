package com.saviola44.task.ui

import androidx.lifecycle.ViewModel
import com.saviola44.task.ui.common.Action
import com.saviola44.task.ui.common.SingleLiveEvent
import io.reactivex.disposables.CompositeDisposable
import androidx.paging.PagedList
import androidx.lifecycle.LiveData
import com.saviola44.task.data.model.remote.Repo
import com.saviola44.task.data.repository.search.pagination.SearchDataSourceFactory
import androidx.appcompat.widget.SearchView
import androidx.paging.LivePagedListBuilder
import com.saviola44.task.data.repository.search.pagination.SearchDataSource
import com.saviola44.task.ui.common.RxSearchObservable
import java.util.concurrent.TimeUnit


class MainVM : ViewModel() {
    //for progress bar, snackbar etc
    val actions = SingleLiveEvent<Action>()
    var itemPagedList: LiveData<PagedList<Repo>>

    private val compositeDisposable = CompositeDisposable()
    private val searchReposSourceFactory = SearchDataSourceFactory(compositeDisposable, { action ->
        dispatchAction(action)
    })

    init {
        val pagedListConfig = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(SearchDataSource.PAGE_SIZE).build()

        itemPagedList = LivePagedListBuilder(searchReposSourceFactory, pagedListConfig).build()
    }

    fun observeQuery(sv: SearchView){
        val disposable = RxSearchObservable.fromView(sv)
            .debounce(DEBOUNCE, TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
            .subscribe{ query ->
                searchReposSourceFactory.updateQuery(query)
            }
        compositeDisposable.add(disposable)
    }


    private fun dispatchAction(action: Action) {
        actions.postValue(action)
    }

    companion object {
        private val TAG = MainVM::class.java.simpleName
        private const val DEBOUNCE = 400L
    }
}