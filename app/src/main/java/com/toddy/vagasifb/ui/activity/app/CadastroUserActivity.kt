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
        binding.toolbarVoltar.tvTitulo.text = "Criar Conta"
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
            val telefone: String = edtTelefone.text.toString().trim()

            when {
                email.isEmpty() -> {
                    edtEmail.requestFocus()
                    edtEmail.error = "Campo Obrigatório"
                }
                senha.isEmpty() -> {
                    edtEmail.requestFocus()
                    edtEmail.error = "Campo Obrigatório"
                }
                telefone.isEmpty() -> {
                    edtTelefone.requestFocus()
                    edtTelefone.error = "Campo Obrigatório"
                }
                else -> {
                    progressBar.visibility = View.VISIBLE
                    btnLogin.visibility = View.GONE
                    salvarUser(email, senha, telefone)
                }
            }
        }
    }

    private fun salvarUser(email: String, senha: String, telefone:String) {
        val userDao = UserDao()
        userDao.criarConta( this, email, senha, telefone) { user ->
            user?.let {
                userDao.salvarUser(user)
                finish()
            }
            binding.progressBar.visibility = View.GONE
            binding.btnLogin.visibility = View.VISIBLE
        }
    }
}