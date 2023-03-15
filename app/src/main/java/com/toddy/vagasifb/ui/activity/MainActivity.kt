package com.toddy.vagasifb.ui.activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.toddy.vagasifb.R
import com.toddy.vagasifb.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}