package com.toddy.vagasifb.ui.activity.admin

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.toddy.vagasifb.databinding.ActivityProfCadastradosBinding
import com.toddy.vagasifb.extensions.iniciaActivity
import com.toddy.vagasifb.ui.activity.app.LoginActivity

class ProfCadastradosActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityProfCadastradosBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        configClicks()
    }

    private fun configClicks() {
        with(binding) {
            toolbarSair.tvTitulo.text = "Empregadores Cadastrados"
            toolbarSair.btnSair.setOnClickListener {
                iniciaActivity(LoginActivity::class.java)
                finish()
            }
            fabAdd.setOnClickListener {
                iniciaActivity(CadastrarProfActivity::class.java)
            }
        }
    }


}