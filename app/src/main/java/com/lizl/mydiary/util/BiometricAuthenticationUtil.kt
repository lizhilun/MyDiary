package com.lizl.mydiary.util

import android.content.DialogInterface
import android.hardware.biometrics.BiometricPrompt
import android.os.CancellationSignal
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.core.hardware.fingerprint.FingerprintManagerCompat
import com.lizl.mydiary.R
import com.lizl.mydiary.UiApplication

object BiometricAuthenticationUtil
{
    private val TAG = "BiometricAuthenticationUtil"

    private lateinit var mBiometricPrompt: BiometricPrompt

    private fun getBiometricPrompt(): BiometricPrompt
    {
        if (!this::mBiometricPrompt.isInitialized)
        {
            mBiometricPrompt =
                    BiometricPrompt.Builder(UiApplication.instance).setTitle(UiApplication.instance.getString(R.string.fingerprint_authentication_dialog_title))
                        .setDescription(UiApplication.instance.getString(R.string.fingerprint_authentication_dialog_description))
                        .setNegativeButton(UiApplication.instance.getString(R.string.cancel), ContextCompat.getMainExecutor(UiApplication.instance),
                                DialogInterface.OnClickListener { _, _ -> Log.d(TAG, "cancel button click") }).build()
        }
        return mBiometricPrompt
    }

    fun authenticate(authenticateCallback: BiometricPrompt.AuthenticationCallback)
    {
        getBiometricPrompt().authenticate(CancellationSignal(), ContextCompat.getMainExecutor(UiApplication.instance), authenticateCallback)
    }

    fun isFingerprintSupport(): Boolean
    {
        return try
        {
            val mFingerprintManager = FingerprintManagerCompat.from(UiApplication.instance)
            mFingerprintManager.isHardwareDetected && mFingerprintManager.hasEnrolledFingerprints()
        }
        catch (e: ClassNotFoundException)
        {
            false
        }
    }
}