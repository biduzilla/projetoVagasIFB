package com.toddy.vagasifb.ui.activity.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.toddy.vagasifb.databinding.ActivitySplashBinding
import com.toddy.vagasifb.extensions.iniciaActivity

class SplashActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivitySplashBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        Handler(Looper.getMainLooper()).postDelayed({
            iniciaActivity(LoginActivity::class.java)
            finish()
        }, 3000)
    }
}