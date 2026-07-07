package com.nels.master.testsoaint.utils

import android.content.Context
import android.content.SharedPreferences
import android.security.keystore.KeyPermanentlyInvalidatedException
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.nels.master.testsoaint.domain.model.Usuario
import dagger.hilt.android.qualifiers.ApplicationContext
import java.security.KeyStoreException
import java.security.UnrecoverableKeyException
import javax.crypto.AEADBadTagException
import javax.inject.Inject
import javax.inject.Singleton
import androidx.core.content.edit

@Singleton
class PreferencesManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private var prefs: SharedPreferences? = null

    private fun getPrefs(): SharedPreferences {
        if (prefs == null) {
            prefs = try {
                val masterKey = MasterKey.Builder(context)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build()
                EncryptedSharedPreferences.create(
                    context,
                    PREFS_NAME,
                    masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
                )
            } catch (e: Exception) {
                context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                    .edit { clear() }
                SafeLog.w(TAG, "Encrypted prefs corrupt, recreating: ${e.message}")
                val masterKey = MasterKey.Builder(context)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build()
                EncryptedSharedPreferences.create(
                    context,
                    PREFS_NAME,
                    masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
                )
            }
        }
        return prefs!!
    }

    private fun readPrefsSafe(block: SharedPreferences.() -> String?): String? {
        return try {
            getPrefs().block()
        } catch (e: SecurityException) {
            handleCorruption()
            null
        } catch (e: KeyStoreException) {
            handleCorruption()
            null
        } catch (e: AEADBadTagException) {
            handleCorruption()
            null
        } catch (e: UnrecoverableKeyException) {
            handleCorruption()
            null
        } catch (e: KeyPermanentlyInvalidatedException) {
            handleCorruption()
            null
        }
    }

    private fun handleCorruption(): Nothing? {
        SafeLog.w(TAG, "Prefs corruption detected, clearing data")
        getPrefs().edit().clear().apply()
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit().clear().apply()
        return null
    }

    fun saveToken(token: String) {
        getPrefs().edit().putString(KEY_TOKEN, token).apply()
    }

    fun getToken(): String? = readPrefsSafe { getString(KEY_TOKEN, null) }

    fun saveUser(user: Usuario) {
        getPrefs().edit()
            .putString(KEY_USERNAME, user.username)
            .putString(KEY_ROL, user.rol)
            .apply()
    }

    fun clear() {
        getPrefs().edit().clear().apply()
    }

    companion object {
        private const val PREFS_NAME = "auth_prefs"
        private const val KEY_TOKEN = "jwt_token"
        private const val KEY_USERNAME = "username"
        private const val KEY_ROL = "rol"
        private const val TAG = "PreferencesManager"
    }
}
