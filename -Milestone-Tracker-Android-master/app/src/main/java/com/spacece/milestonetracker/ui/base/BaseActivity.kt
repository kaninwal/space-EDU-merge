package com.spacece.milestonetracker.ui.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.spacece.milestonetracker.data.local.SharedPrefs
import com.spacece.milestonetracker.viewModel.AuthViewModel
import com.spacece.milestonetracker.viewModel.MainViewModel
import com.spacece.milestonetracker.viewModel.UserViewModel
import com.spacece.milestonetracker.viewModel.ViewModelFactory

open class BaseActivity : AppCompatActivity() {
    lateinit var sharedPrefs: SharedPrefs
    lateinit var authViewModel: AuthViewModel
    lateinit var mainViewModel: MainViewModel
    lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPrefs = SharedPrefs(this)
        authViewModel = ViewModelProvider(this, ViewModelFactory(this))[AuthViewModel::class.java]
        mainViewModel = ViewModelProvider(this, ViewModelFactory(this))[MainViewModel::class.java]
        userViewModel = ViewModelProvider(this, ViewModelFactory(this))[UserViewModel::class.java]
    }
}
