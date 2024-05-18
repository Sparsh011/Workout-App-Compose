package com.sparshchadha.workout_app.ui.activity.components

import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.sparshchadha.workout_app.features.shared.viewmodels.SharedViewModel
import com.sparshchadha.workout_app.ui.activity.GoogleAuthClient

@Composable
fun GoogleSignInLauncher(
    sharedViewModel: SharedViewModel
) {
    val context = LocalContext.current
    val startLogin = sharedViewModel.startGoogleSignIn.collectAsStateWithLifecycle().value
    val googleAuthClient = GoogleAuthClient(context)
    val isConnectedToInternet = sharedViewModel.connectedToInternet.collectAsStateWithLifecycle().value ?: false
    var showNoInternetToast by rememberSaveable {
        mutableStateOf(false)
    }

    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            handleSignInResult(
                completedTask = task,
                getId = { idToken ->
                    if (idToken != null) {
                        sharedViewModel.updateLoginResult(result = true, token = idToken)
                    } else {
                        sharedViewModel.updateLoginResult(result = false)
                    }
                },
                getAccessToken = {
                    // send access token to backend to verify.
                }
            )
        }
    )

    observeLoginState(
        startLogin = startLogin,
        googleAuthClient = googleAuthClient,
        signInLauncher = googleSignInLauncher,
        isConnectedToInternet = isConnectedToInternet,
        showNoInternetToast = {
            showNoInternetToast = true
        }
    )

    if (showNoInternetToast) {
        Toast.makeText(context, "Please Check Your Internet Connection and Try Again!", Toast.LENGTH_SHORT).show()
        showNoInternetToast = false
    }
}

private fun observeLoginState(
    startLogin: Boolean,
    googleAuthClient: GoogleAuthClient,
    signInLauncher: ManagedActivityResultLauncher<Intent, ActivityResult>,
    isConnectedToInternet: Boolean,
    showNoInternetToast: () -> Unit
) {
    when (startLogin) {
        true -> {
            if (isConnectedToInternet) {
                val signInIntent = googleAuthClient.getSignInIntent()
                signInLauncher.launch(signInIntent)
            } else {
                showNoInternetToast()
            }
        }

        false -> {

        }
    }
}


private fun handleSignInResult(
    completedTask: Task<GoogleSignInAccount>,
    getId: (String?) -> Unit,
    getAccessToken: (String?) -> Unit
) {
    try {
        val account: GoogleSignInAccount = completedTask.getResult(ApiException::class.java)
        getId(
            account.id
        )
        getAccessToken(
            account.idToken
        )
    } catch (e: ApiException) {
        getId(null)
        getAccessToken(null)
    }
}