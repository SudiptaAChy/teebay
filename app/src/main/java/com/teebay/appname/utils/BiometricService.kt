package com.teebay.appname.utils

import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity

object BiometricService {
    private const val AUTHENTICATOR = BiometricManager.Authenticators.BIOMETRIC_STRONG

    fun isBiometricAvailable(context: Context): Pair<Boolean, String> {
        val biometricManager = BiometricManager.from(context)
        return when (biometricManager.canAuthenticate(AUTHENTICATOR)) {
            BiometricManager.BIOMETRIC_SUCCESS -> Pair(true, "")
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                Pair(false, "No biometric hardware available.")
            }
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                Pair(false, "Biometric hardware unavailable.")
            }
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                Pair(false, "No biometrics enrolled.")
            }
            else -> Pair(false, "Biometric feature not available.")
        }
    }

    fun authenticate(
        activity: FragmentActivity,
        onSuccess: () -> Unit,
    ) {
        val executor = ContextCompat.getMainExecutor(activity)

        val biometricPrompt = BiometricPrompt(activity, executor, object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                onSuccess()
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
            }
        })

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric Authentication")
            .setSubtitle("Log in using your biometric credentials")
            .setNegativeButtonText("Cancel")
            .setAllowedAuthenticators(AUTHENTICATOR)
            .build()

        biometricPrompt.authenticate(promptInfo)
    }

    fun promptEnrollBiometric(context: Context) {
        val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED, AUTHENTICATOR)
            }
        } else {
            Intent(Settings.ACTION_SECURITY_SETTINGS)
        }
        context.startActivity(intent)
    }
}
