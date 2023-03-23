package com.toddy.vagasifb.ui.activity.app

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.toddy.vagasifb.database.UserDao
import com.toddy.vagasifb.databinding.ActivityLoginBinding
import com.toddy.vagasifb.extensions.iniciaActivity
import com.toddy.vagasifb.utils.SharedPref

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
            progressBar.visibility = View.VISIBLE
            btnLogin.visibility = View.GONE

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
                    ocultarTeclado()

                    SharedPref(this@LoginActivity).salvarDadosLogin(email, senha)

                    UserDao().login(this@LoginActivity, email, senha)
                }
            }
        }
    }

    private fun ocultarTeclado() {
        val imm =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.edtEmail.windowToken, 0)
    }

}