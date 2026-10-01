package com.example.extendedhelloworld

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.extendedhelloworld.ui.theme.ExtendedHelloWorldTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val client= RESTClient("http://192.168.0.20:8080")
        setContent {
            ExtendedHelloWorldTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background //Color(0xffC0C0C0)

                ) {
                    // Calling of composable function
                   ButtonAndLabelScreen(client)
                }
            }
        }
    }
}
// Composable function defining activity screen
@Composable
fun ButtonAndLabelScreen(client: RESTClient) {
    // State to hold the text that will change when the button is clicked
    var buttonClickedMessage by remember { mutableStateOf("Hello World") }

    // Required scope to launch coroutine
    val scope = rememberCoroutineScope()

    // Column layout
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center, // Centers children vertically
        horizontalAlignment = Alignment.CenterHorizontally // Centers children horizontally
    ) {
        // Display the current status message
        Text(text = buttonClickedMessage, textAlign = TextAlign.Center, fontSize = 20.sp,  fontWeight = FontWeight.Bold  )

        // Add some vertical space between the text and the button
        Spacer(modifier = Modifier.height(24.dp))

        // The button composable
        Button(onClick = {
            // Update the state when the button is clicked, triggering a recomposition

            val name="Alejandro"
            val pass="1234"


            // Launch coroutine perform HTTP requests
            scope.launch(Dispatchers.IO) {
                // POST request to login
                client.httpPostAsync("/login","""{"userName": "$name","password": "$pass"}""")
                var response = client.wait()

                // GET request to retrieve doctors list
                client.httpGetAsync("/getlocks?userName=$name")

                response = client.wait()
                // Change context to Main thread to modify UI
                withContext(Dispatchers.Main) {
                    // This triggers re-composition
                    buttonClickedMessage = response
                }

                // POST request to logout
                client.httpPostAsync("/logout", """ "userName": "$name """)
                response = client.wait()
            }

        }) {
            // The label (text) inside the button
            Text(text = "Get locks list")
        }
    }
}





