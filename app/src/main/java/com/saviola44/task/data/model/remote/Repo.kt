package com.saviola44.task.data.model.remote

data class RepoResponse(val items: List<Repo>)

data class Repo(
    val id: Long,
    val name: String,
    val owner: Owner,
    val stargazers_count: Int,
    val language: String,
    val html_url: String)

data class Owner(
    val id: Long,
    val avatar_url: String)