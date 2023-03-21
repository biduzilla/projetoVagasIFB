package com.toddy.vagasifb.ui.activity.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.toddy.vagasifb.databinding.ActivityCadastrarProfBinding

class CadastrarProfActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityCadastrarProfBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}