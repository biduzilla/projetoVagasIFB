package com.toddy.vagasifb.ui.activity.admin

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.toddy.vagasifb.database.AdminDao
import com.toddy.vagasifb.databinding.ActivityCadastrarProfBinding

class CadastrarProfActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityCadastrarProfBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        configClicks()

        Toast.makeText(
            baseContext,
            FirebaseAuth.getInstance().currentUser!!.uid,
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun configClicks() {
        with(binding) {
            toolbarVoltar.tvTitulo.text = "Cadastrar Empregador"
            toolbarVoltar.btnVoltar.setOnClickListener {
                finish()
            }
            btnLogin.setOnClickListener {
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
                    edtSenha.requestFocus()
                    edtSenha.error = "Campo Obrigatório"
                }
                telefone.isEmpty() -> {
                    edtTelefone.requestFocus()
                    edtTelefone.error = "Campo Obrigatório"
                }
                else -> {
                    btnLogin.visibility = View.GONE
                    progressBar.visibility = View.VISIBLE

                    AdminDao().cadastraEmpregador(this@CadastrarProfActivity, email, senha,telefone, this)
                }
            }
        }
    }

}