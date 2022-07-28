package com.devarm.dentalhistory

//import android.hardware.biometrics.BiometricManager
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.biometric.BiometricManager
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.devarm.dentalhistory.ui.theme.DentalHistoryTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DentalHistoryTheme {

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Auth()
                }
            }
        }

        setupAuth()
    }

    private var canAuth = false
    private lateinit var promptInfo:BiometricPrompt.PromptInfo

    private fun setupAuth(){
        if(BiometricManager.from(this).canAuthenticate(
                BIOMETRIC_STRONG or DEVICE_CREDENTIAL) ==BiometricManager.BIOMETRIC_SUCCESS)
            canAuth=true
            promptInfo=BiometricPrompt.PromptInfo.Builder().setTitle("Authenticacion Biometrica").setAllowedAuthenticators(
                BIOMETRIC_STRONG or DEVICE_CREDENTIAL).build()
    }
    private fun authenticate(auth:(auth:Boolean)->Unit){
        if(canAuth){
            BiometricPrompt(this, ContextCompat.getMainExecutor(this),
            object: BiometricPrompt.AuthenticationCallback(){
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)

                    auth(true)
                }
            }).authenticate(promptInfo)
        }else{
            auth(true)
        }
    }

    @Composable
    fun Auth(){

        var auth by remember { mutableStateOf(false)}

        Column(modifier = Modifier
            .background( if(auth)  Color(0xFF93E2E2)  else Color(0xFFFFFFFF) )
            .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {
            Text(text = if(auth) "Estas Auth" else "Necesitas Auth" ,
                fontSize=22.sp,
                fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = {
                    if(auth) {
                        auth=false
                    }else{
                        authenticate {auth=it}
                    }
                }) {
                Text(text = if(auth) "Cerrar Session" else "Authenticar")
            }
        }
    }

    @Preview(showSystemUi = true)
    @Composable
    fun DefaultPreview() {
        DentalHistoryTheme {
            Auth()
        }
    }
}


