package com.toddy.vagasifb.ui.activity.app

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.toddy.vagasifb.database.UserDao
import com.toddy.vagasifb.databinding.ActivityCadastroUserBinding

class CadastroUserActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityCadastroUserBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        configClick()
    }

    private fun configClick() {
        with(binding) {
            toolbarVoltar.btnVoltar.setOnClickListener {
                finish()
            }
            binding.btnLogin.setOnClickListener {
                validaDados()
            }
        }
    }

    private fun validaDados() {
        with(binding) {
            val email: String = edtEmail.text.toString().trim()
            val senha: String = edtSenha.text.toString().trim()

            when {
                email.isEmpty() -> {
                    edtEmail.requestFocus()
                    edtEmail.error = "Campo Obrigatório"
                }
                senha.isEmpty() -> {
                    edtEmail.requestFocus()
                    edtEmail.error = "Campo Obrigatório"
                }
                else -> {
                    progressBar.visibility = View.VISIBLE
                    btnLogin.visibility = View.GONE
                    salvarUser(email, senha)

                }
            }
        }
    }

    private fun salvarUser(email: String, senha: String) {
        val userDao = UserDao()
        userDao.criarConta( this, email, senha) { user ->
            user?.let {
                userDao.salvarUser(user)
                finish()
            }
            binding.progressBar.visibility = View.GONE
            binding.btnLogin.visibility = View.VISIBLE
        }
    }
}