package com.toddy.vagasifb.ui.activity.empregador

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.toddy.vagasifb.databinding.ActivityCandidaturasBinding

class CandidaturasActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityCandidaturasBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        configClicks()
    }

    private fun configClicks() {
        with(binding){

        }
    }
}