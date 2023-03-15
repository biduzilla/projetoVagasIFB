package com.toddy.vagasifb.ui.activity.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.toddy.vagasifb.database.UserDao
import com.toddy.vagasifb.databinding.ActivityRecuperarSenhaBinding
import com.toddy.vagasifb.extensions.iniciaActivity

class RecuperarSenhaActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityRecuperarSenhaBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        configClicks()
    }

    private fun configClicks() {
        with(binding) {
            include.btnVoltar.setOnClickListener {
                finish()
            }
            btnLogin.setOnClickListener {
                validaDados()
            }
        }
    }

    private fun validaDados() {
        with(binding) {

            val email = edtEmail.text.toString().trim()
            if (email.isEmpty()) {
                edtEmail.requestFocus()
                edtEmail.error = "Campo Obrigatório"
            } else {
                progressBar.visibility = View.VISIBLE
                btnLogin.visibility = View.GONE
                recuperarConta(email)
            }
        }
    }

    private fun recuperarConta(email: String) {
        UserDao().recuperarConta(this, email)
        binding.progressBar.visibility = View.GONE
        binding.btnLogin.visibility = View.VISIBLE
    }
}