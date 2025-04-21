package com.wzh.rewriteplay02.profile.login

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.view.animation.OvershootInterpolator
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.wzh.base.util.checkNetworkAvailable
import com.wzh.base.util.showToast
import com.wzh.base.view.base.BaseActivity
import com.wzh.rewriteplay02.R
import com.wzh.rewriteplay02.databinding.ActivityLoginBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginActivity : BaseActivity(), View.OnClickListener, TextWatcher {
    private var isLoginPage = 1.0
    private lateinit var binding: ActivityLoginBinding
    private val viewModel by viewModels<LoginViewModel>()
    private var mUserName = ""
    private var mPassWord = ""
    private var mIsLogin = true

    override fun getRealLayoutView(): View {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initView() {
        binding.loginButton.setOnClickListener(this)
        binding.loginTvRegister.setOnClickListener(this)
        binding.loginPassNumberEdit.addTextChangedListener(this)
        binding.loginPassNumberEdit.transformationMethod =
            PasswordTransformationMethod.getInstance()
        viewModel.state.observe(this) {
            when (it) {
                Logging -> {
                    toProgressVisible(true)
                }

                is LoginSuccess -> {
                    toProgressVisible(false)
                    finish()
                }

                LoginError -> {
                    toProgressVisible(false)
                }
            }
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.loginTvRegister -> {
                flipAnimatorXViewShow(binding.loginInputElements)
                if (isLoginPage > 0) {
                    binding.loginTitle.text = "注册"
                } else {
                    binding.loginTitle.text = "登录"
                }
                isLoginPage = -isLoginPage
            }

            R.id.loginButton -> {
                loginOrRegister()
            }
        }
    }

    private fun loginOrRegister() {
        if (!judge()) return
        viewModel.toLoginOrRegister(Account(mUserName, mPassWord, mIsLogin))
    }

    private fun updateState() {
        binding.loginTvRegister.text =
            if (mIsLogin) getString(com.wzh.base.R.string.return_login) else getString(com.wzh.base.R.string.register_account)
        binding.loginButton.text =
            if (mIsLogin) getString(com.wzh.base.R.string.register_account) else getString(com.wzh.base.R.string.login)
        mIsLogin = !mIsLogin
    }

    private fun flipAnimatorXViewShow(view: View) {
        val animator1 = ObjectAnimator.ofFloat(view, "rotationY", 0f, if (mIsLogin) 90f else -90f)
        val animator2 = ObjectAnimator.ofFloat(view, "rotationY", if (mIsLogin) -90f else 90f, 0f)
        animator2.interpolator = OvershootInterpolator(2.0f)
        animator1.setDuration(700).start()
        animator1.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {}
            override fun onAnimationEnd(animation: Animator) {
                animator2.setDuration(700).start()
                updateState()
            }

            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {}
        })
    }

    private fun judge(): Boolean {
        mUserName = binding.loginUserNumberEdit.text.toString()
        mPassWord = binding.loginPassNumberEdit.text.toString()
        if (TextUtils.isEmpty(mUserName) || mUserName.length < 6) {
            binding.loginUserNumberEdit.error = getString(com.wzh.base.R.string.enter_name_format)
            return false
        }
        if (TextUtils.isEmpty(mPassWord) || mPassWord.length < 6) {
            binding.loginPassNumberEdit.error =
                getString(com.wzh.base.R.string.enter_password_format)
            return false
        }
        if (!checkNetworkAvailable()) {
            showToast(getString(com.wzh.base.R.string.no_network))
            return false
        }
        return true
    }

    private fun toProgressVisible(visible: Boolean) {
        binding.loginProgressBar.isVisible = visible
        binding.loginInputElements.isVisible = !visible
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

    }

    override fun afterTextChanged(s: Editable?) {

    }

    companion object {
        fun actionStart(context: Context) {
            val intent = Intent(context, LoginActivity::class.java)
            context.startActivity(intent)
        }
    }

}
