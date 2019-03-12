package com.saviola44.task.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.saviola44.task.R
import com.saviola44.task.databinding.ActivityMainBinding
import com.saviola44.task.ui.common.Action
import com.saviola44.task.ui.common.ShowError
import com.saviola44.task.ui.common.ShowMsg
import kotlinx.android.synthetic.main.activity_main.*
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.view.View
import com.saviola44.task.ui.common.SetProgressBarVisibility


class MainActivity : AppCompatActivity() {
    lateinit var vm: MainVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        vm = ViewModelProviders.of(this).get(MainVM::class.java)
        binding.vm = vm

        val adapter = RepoListAdapter()
        vm.itemPagedList.observe(this, Observer {
            adapter.submitList(it)
            setNoResultVisibility(it.size==0)
        })
        rv_main.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        vm.actions.observe(this, Observer { action ->
            onAction(action)
        })
    }

    override fun onResume() {
        super.onResume()
        changeStatusBarColor()
        vm.observeQuery(sv_main)
    }

    fun onAction(action: Action) {
        when(action) {
            is ShowMsg -> { showMsg(action.msg) }
            is ShowError -> { showErrorMsg(action.error) }
            is SetProgressBarVisibility -> setProgBarVisibility(action.isVisible)
        }
    }

    private fun showErrorMsg(error: String) {
        Snackbar.make(cl_main_container, error, Snackbar.LENGTH_LONG).apply {
            view.setBackgroundColor(ContextCompat.getColor(context, R.color.bckg_torch_red))
        }.show()
    }

    private fun showMsg(msg: String) = Snackbar.make(cl_main_container, msg, Snackbar.LENGTH_LONG).show()

    private fun setProgBarVisibility(isVisible: Boolean) {
        if(isVisible){
            pb_main.visibility = View.VISIBLE
        } else {
            pb_main.visibility = View.GONE
        }
    }

    private fun setNoResultVisibility(isVisible: Boolean) {
        if(isVisible){
            tv_main_empty_list_info.visibility = View.VISIBLE
        } else {
            tv_main_empty_list_info.visibility = View.GONE
        }
    }


    private fun changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(applicationContext, R.color.bckg_mako)
        } else {
            supportActionBar?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(applicationContext, R.color.bckg_mako)))
        }
    }
}
