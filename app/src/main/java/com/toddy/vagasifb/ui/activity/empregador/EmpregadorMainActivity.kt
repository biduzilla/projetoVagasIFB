package com.toddy.vagasifb.ui.activity.empregador

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.toddy.vagasifb.databinding.ActivityEmpregadorMainBinding

class EmpregadorMainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityEmpregadorMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}