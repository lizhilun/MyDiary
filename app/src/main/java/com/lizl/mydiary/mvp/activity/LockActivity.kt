package com.lizl.mydiary.mvp.activity

import androidx.recyclerview.widget.GridLayoutManager
import com.lizl.mydiary.R
import com.lizl.mydiary.UiApplication
import com.lizl.mydiary.adapter.NumberKeyGridAdapter
import com.lizl.mydiary.custom.others.recylerviewitemdivider.GridDividerItemDecoration
import com.lizl.mydiary.mvp.base.BaseActivity
import com.lizl.mydiary.mvp.contract.LockContract
import com.lizl.mydiary.mvp.presenter.LockPresenter
import com.lizl.mydiary.util.BiometricAuthenticationUtil
import com.lizl.mydiary.util.UiUtil
import kotlinx.android.synthetic.main.activity_lock.*

class LockActivity : BaseActivity<LockPresenter>(), LockContract.View
{

    override fun getLayoutResId() = R.layout.activity_lock

    override fun initPresenter() = LockPresenter(this)

    override fun initView()
    {
        val numberKeyList: List<String> = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "*", "0", "#")
        val numberKeyGridAdapter = NumberKeyGridAdapter(numberKeyList)
        rv_number_key.layoutManager = GridLayoutManager(this, 3)
        rv_number_key.addItemDecoration(GridDividerItemDecoration())
        rv_number_key.adapter = numberKeyGridAdapter

        numberKeyGridAdapter.setOnNumberItemClickListener {

            when (it)
            {
                "*"  -> UiUtil.backToLauncher()
                "#"  -> tv_number.backspace()
                else -> tv_number.add(it)
            }

            presenter.checkInputPassword(tv_number.getInputText())
        }
    }

    override fun onStart()
    {
        super.onStart()

        if (BiometricAuthenticationUtil.isFingerprintSupport() && UiApplication.appConfig.isFingerprintLockOn())
        {
            tv_hint.text = getString(R.string.hint_verify_fingerprint_or_input_password)

            iv_lock.setOnClickListener { presenter.startFingerprintAuthentication() }
            presenter.startFingerprintAuthentication()

            iv_lock.setImageResource(R.drawable.ic_fingerprint)
        }
        else
        {
            tv_hint.text = getString(R.string.hint_input_password)
            iv_lock.setImageResource(R.drawable.ic_lock)
        }

        tv_number.clear()
    }

    override fun onUnlockSuccess()
    {
        UiApplication.appConfig.setAppLastStopTime(Long.MAX_VALUE)
        finish()
    }

    override fun onBackPressed()
    {
        UiUtil.backToLauncher()
    }
}