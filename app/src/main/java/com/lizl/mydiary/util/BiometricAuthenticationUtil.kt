package com.lizl.mydiary.util

import android.content.Context
import android.content.DialogInterface
import android.hardware.biometrics.BiometricManager
import android.hardware.biometrics.BiometricPrompt
import android.os.CancellationSignal
import androidx.core.content.ContextCompat
import com.lizl.mydiary.R
import com.lizl.mydiary.UiApplication

object BiometricAuthenticationUtil
{
    private val mBiometricPrompt: BiometricPrompt by lazy {
        BiometricPrompt.Builder(UiApplication.instance).setTitle(UiApplication.instance.getString(R.string.fingerprint_authentication_dialog_title))
            .setDescription(UiApplication.instance.getString(R.string.fingerprint_authentication_dialog_description))
            .setNegativeButton(UiApplication.instance.getString(R.string.cancel),
                ContextCompat.getMainExecutor(UiApplication.instance),
                DialogInterface.OnClickListener { _, _ -> }).build()
    }

    private val mBiometricManager: BiometricManager by lazy { UiApplication.instance.getSystemService(Context.BIOMETRIC_SERVICE) as BiometricManager }

    fun authenticate(authenticateCallback: BiometricPrompt.AuthenticationCallback)
    {
        mBiometricPrompt.authenticate(CancellationSignal(), ContextCompat.getMainExecutor(UiApplication.instance), authenticateCallback)
    }

    fun isFingerprintSupport() = mBiometricManager.canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS
}