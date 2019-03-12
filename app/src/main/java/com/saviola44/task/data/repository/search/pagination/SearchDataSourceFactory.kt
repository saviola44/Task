package com.saviola44.task.data.repository.search.pagination

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.saviola44.task.data.model.remote.Repo
import com.saviola44.task.ui.common.Action
import io.reactivex.disposables.CompositeDisposable

class SearchDataSourceFactory(val compositeDisposable: CompositeDisposable,
                              val actionhandler: (Action)-> Unit,
                              var query: String = "") : DataSource.Factory<Long, Repo>() {
    val searchDataSource = MutableLiveData<SearchDataSource>()

    override fun create(): DataSource<Long, Repo> {
        val source = SearchDataSource(compositeDisposable, query, actionhandler)
        this.searchDataSource.postValue(source)
        return source
    }

    fun updateQuery(query: String) {
        this.query = query
        searchDataSource?.value?.invalidate()
    }
}