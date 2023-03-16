package com.toddy.vagasifb.ui.activity.empregador

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.toddy.vagasifb.databinding.ActivityFormVagaBinding

class FormVagaActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityFormVagaBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}