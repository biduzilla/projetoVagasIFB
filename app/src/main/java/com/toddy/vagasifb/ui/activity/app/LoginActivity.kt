package com.toddy.vagasifb.ui.activity.app

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.toddy.vagasifb.database.UserDao
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
            btnLogin.setOnClickListener {
                validaDados()
            }
        }
    }

    private fun validaDados() {
        with(binding) {
            val email = edtEmail.text.toString().trim()
            val senha = edtSenha.text.toString().trim()

            when {
                email.isEmpty() -> {
                    edtEmail.requestFocus()
                    edtSenha.error = "Campo Obrigatório"
                }
                senha.isEmpty() -> {
                    edtEmail.requestFocus()
                    edtSenha.error = "Campo Obrigatório"
                }
                else -> {
                    progressBar.visibility = View.VISIBLE
                    btnLogin.visibility = View.GONE

                    login(email, senha)
                }
            }
        }
    }

    private fun login(email: String, senha: String) {
        UserDao().login(this, email, senha)
    }
}