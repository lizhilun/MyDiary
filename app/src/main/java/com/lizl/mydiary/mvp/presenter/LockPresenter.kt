package com.lizl.mydiary.mvp.presenter

import android.hardware.biometrics.BiometricPrompt
import android.util.Log
import com.lizl.mydiary.UiApplication
import com.lizl.mydiary.config.AppConfig
import com.lizl.mydiary.event.UIEvent
import com.lizl.mydiary.mvp.contract.LockContract
import com.lizl.mydiary.util.BiometricAuthenticationUtil

class LockPresenter(private var view: LockContract.View?) : LockContract.Presenter
{

    private var TAG = this.javaClass.simpleName

    override fun startFingerprintAuthentication()
    {
        BiometricAuthenticationUtil.authenticate(object : BiometricPrompt.AuthenticationCallback()
        {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence?)
            {
                Log.d(TAG, "onAuthenticationError() called with: errorCode = [$errorCode], errString = [$errString]")
                super.onAuthenticationError(errorCode, errString)
            }

            override fun onAuthenticationFailed()
            {
                Log.d(TAG, "onAuthenticationFailed")
                super.onAuthenticationFailed()
            }

            override fun onAuthenticationHelp(helpCode: Int, helpString: CharSequence?)
            {
                Log.d(TAG, "onAuthenticationHelp() called with: helpCode = [$helpCode], helpString = [$helpString]")
                super.onAuthenticationHelp(helpCode, helpString)
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult?)
            {
                Log.d(TAG, "onAuthenticationSucceeded() called with: result = [$result]")
                super.onAuthenticationSucceeded(result)
                view?.onUnlockSuccess()
            }
        })
    }

    override fun checkInputPassword(password: String)
    {
        if (password == AppConfig.getSecurityConfig().getAppLockPassword())
        {
            view?.onUnlockSuccess()
        }
    }

    override fun handleUIEvent(uiEvent: UIEvent)
    {

    }

    override fun onDestroy()
    {
        view = null
    }
}