package com.saviola44.task.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.saviola44.task.R
import com.saviola44.task.data.model.remote.Repo
import com.saviola44.task.databinding.ItemRepoBinding
import android.content.Intent
import android.net.Uri
import com.saviola44.task.App


class RepoListAdapter : PagedListAdapter<Repo, RepoListAdapter.ViewHolder>(diffCallbackHandler) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_repo, parent, false)
        val binding = DataBindingUtil.bind<ItemRepoBinding>(view)
        return ViewHolder(binding!!)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val repo = getItem(position)
        repo?.let {
            holder.bindView(it)
        }
    }


    class ViewHolder(val binding: ItemRepoBinding): RecyclerView.ViewHolder(binding.root) {
        val repoObservable = ObservableField<Repo>()
        fun bindView(repo: Repo) {
            repoObservable.set(repo)
            binding.vh = this
        }

        fun onItemClick() {
            repoObservable.get()?.let { repo ->
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(repo.html_url)
                App.appCtx().startActivity(i)
            }
        }
    }

    companion object {
        private val diffCallbackHandler = object : DiffUtil.ItemCallback<Repo>() {
            override fun areItemsTheSame(oldItem: Repo, newItem: Repo): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Repo, newItem: Repo): Boolean = oldItem == newItem
        }
    }
}