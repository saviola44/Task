package com.saviola44.task.ui.common

sealed class Action

//common
data class ShowMsg(val msg: String): Action()
data class ShowError(val error: String): Action()
data class SetProgressBarVisibility(val isVisible: Boolean): Action()