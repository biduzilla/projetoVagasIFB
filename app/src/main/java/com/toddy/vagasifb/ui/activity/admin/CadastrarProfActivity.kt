package com.toddy.vagasifb.ui.activity.admin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.toddy.vagasifb.databinding.ActivityCadastrarProfBinding

class CadastrarProfActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityCadastrarProfBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    private fun configClicks(){

    }

}