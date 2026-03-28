package com.spacece.milestonetracker.ui.activity

import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import androidx.lifecycle.Observer
import com.spacece.milestonetracker.R
import com.spacece.milestonetracker.data.model.LoginRequest
import com.spacece.milestonetracker.data.model.User
import com.spacece.milestonetracker.data.model.ApiResponse
import com.spacece.milestonetracker.data.remote.STATUS_SUCCESS
import com.spacece.milestonetracker.databinding.ActivityLoginMilestoneBinding
import com.spacece.milestonetracker.ui.base.BaseActivity
import com.spacece.milestonetracker.utils.UserType
import com.spacece.milestonetracker.utils.bindLayout
import com.spacece.milestonetracker.utils.clearInputErrorOnTextChangeListeners
import com.spacece.milestonetracker.utils.gone
import com.spacece.milestonetracker.utils.isInternetAvailable
import com.spacece.milestonetracker.utils.isValidEmail
import com.spacece.milestonetracker.utils.setButtonProgress
import com.spacece.milestonetracker.utils.setOnClickListeners
import com.spacece.milestonetracker.utils.setupSpannableText
import com.spacece.milestonetracker.utils.setupText
import com.spacece.milestonetracker.utils.showToast
import com.spacece.milestonetracker.utils.startActivity
import com.spacece.milestonetracker.viewModel.vmHelper.Event

class LoginActivity : BaseActivity(), OnClickListener {
    private lateinit var binding: ActivityLoginMilestoneBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = bindLayout<ActivityLoginMilestoneBinding>(layoutId = R.layout.activity_login_milestone)
        setupViewsAndListeners()
        initViewModelObservers()
    }

    private fun setupViewsAndListeners() = with(binding) {
        if (sharedPrefs.getUserType() == UserType.PARENT.value) {
            tvLoginAs.setupText(getString(R.string.text_login_as_parent))
        } else {
            tvLoginAs.setupText(getString(R.string.text_login_as_admin))
        }

        val email = sharedPrefs.getEmail()
        if (email.isNotEmpty()) {
            tiEdtEmail.setText(email)
        }

        tvRegister.setupSpannableText(R.string.text_don_t_have_an_account)
        setOnClickListeners(listOf(ivBack, btnLogin, tvRegister, tvForgotPassword))
        clearInputErrorOnTextChangeListeners(listOf(tiEdtEmail, tiEdtPassword))
        progressBar.gone()
    }

    private fun initViewModelObservers() = with(binding) {
        authViewModel.loginResponse.observe(this@LoginActivity, Observer<Event<Result<ApiResponse<User>>>> {
            it.getContentIfNotHandled()?.let { result ->
                btnLogin.setButtonProgress(progressBar, false)
                val loginResult = result.getOrNull()
                if (loginResult?.status == STATUS_SUCCESS) {
                    sharedPrefs.saveUserDetails(loginResult.data)
                    if (sharedPrefs.getUserType() == UserType.PARENT.value) {
                        startActivity(ParentMainActivity::class.java)
                    } else {
                        startActivity(AdminMainActivity::class.java)
                    }
                    finish() // Changed finishAffinity() to finish()
                } else {
                    showToast(loginResult?.message ?: getString(R.string.text_something_went_wrong))
                }
            }
        })
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.iv_back -> {
                finish()
            }

            R.id.btn_login -> {
                checkAndInitiateLogin()
            }

            R.id.tv_forgot_password -> {
                startActivity(ForgotPasswordActivity::class.java)
            }

            R.id.tv_register -> {
                startActivity(SignupActivity::class.java)
            }
        }
    }

    private fun checkAndInitiateLogin() = with(binding) {
        val email = tiEdtEmail.text?.toString()
        val password = tiEdtPassword.text?.toString()

        if (email.isNullOrEmpty()) {
            tiEdtEmail.error = getString(R.string.text_please_enter_email)
            tiEdtEmail.requestFocus()
        } else if (!email.isValidEmail()) {
            tiEdtEmail.error = getString(R.string.text_invalid_email)
            tiEdtEmail.requestFocus()
        } else if (password.isNullOrEmpty()) {
            tiEdtPassword.error = getString(R.string.text_please_enter_password)
            tiEdtPassword.requestFocus()
        } else {
            if (isInternetAvailable()) {
                btnLogin.setButtonProgress(progressBar, true)
                authViewModel.login(LoginRequest(email, password, sharedPrefs.getUserType()))
            } else {
                showToast(getString(R.string.text_no_internet))
            }
        }
    }
}
