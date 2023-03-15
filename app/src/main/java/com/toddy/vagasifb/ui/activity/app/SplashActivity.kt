package com.toddy.vagasifb.ui.activity.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.toddy.vagasifb.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivitySplashBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}