package com.grocart.first.ui


import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background

import com.grocart.first.R
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider

@Composable
fun LoginUi(groViewModel: GroViewModel) {
    val context = LocalContext.current
    val otp by groViewModel.otp.collectAsState()
    val verificationId by groViewModel.verificationId.collectAsState()
    val loading by groViewModel.loading.collectAsState()

    // ✅ Callbacks for OTP verification
    val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {

        }

        override fun onVerificationFailed(e: FirebaseException) {

        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken,
        ) {
            groViewModel.setVerificationId(verificationId)
            Toast.makeText(context, "OTP sent", Toast.LENGTH_SHORT).show()
            groViewModel.resetTime()
            groViewModel.runTimer()
            groViewModel.setLoading(false)

        }
    }

    Box {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.otp),
                contentDescription = "App Icon",
                modifier = Modifier
                    .padding(
                        top = 50.dp,
                        bottom = 10.dp
                    )
                    .size(250.dp)
            )
            if (verificationId.isEmpty()) {
                NumberScree(groViewModel = groViewModel, callbacks = callbacks)
            } else {
                OtpScreen(otp = otp, groViewModel = groViewModel, callbacks = callbacks)
            }

        }
        if(verificationId.isNotEmpty()){
            IconButton(onClick={
                groViewModel.setVerificationId("")
                groViewModel.setOtp("")
            },modifier = Modifier
                // Align to top-left
                .padding(16.dp) ){
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
        }
        if (loading) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
                    .background(Color(255,255,255,190))
            ) {
                LinearProgressIndicator()
                Text(text = "Loading")
            }
        }




    }
}
