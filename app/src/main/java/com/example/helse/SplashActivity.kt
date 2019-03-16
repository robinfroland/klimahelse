package com.example.helse

internal class SplashActivity : AppCompatActivity() {

    protected fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}

