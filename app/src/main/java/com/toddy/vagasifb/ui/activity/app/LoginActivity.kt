package com.toddy.vagasifb.ui.activity.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.toddy.vagasifb.databinding.ActivityLoginBinding
import com.toddy.vagasifb.extensions.iniciaActivity

class LoginActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        configClicks()
    }

    private fun configClicks() {
        with(binding) {
            btnCriarConta.setOnClickListener {
                iniciaActivity(CadastroUserActivity::class.java)
            }
            btnEsqueciSenha.setOnClickListener {
                iniciaActivity(RecuperarSenhaActivity::class.java)
            }
        }
    }
}