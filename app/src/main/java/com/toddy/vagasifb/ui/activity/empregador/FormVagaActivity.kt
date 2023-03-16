package com.toddy.vagasifb.ui.activity.empregador

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.toddy.vagasifb.databinding.ActivityFormVagaBinding

class FormVagaActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityFormVagaBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.toolbarVoltar.tvTitulo.text = "Cadastrar Vaga"

        configClicks()
    }

    private fun configClicks() {
        with(binding) {
            toolbarVoltar.btnVoltar.setOnClickListener {
                finish()
            }
            btnSalvar.setOnClickListener {
                validaDados()
            }
        }
    }

    private fun validaDados() {
        with(binding) {
            val cargo = edtCargo.text.toString().trim()
            val empresa = edtEmpresa.text.toString().trim()
            val descricao = edtDescricao.text.toString().trim()
            val horario = edtHorario.text.toString().trim()
            val requisitos = edtRequisitos.text.toString().trim()

            when {
                cargo.isEmpty() -> {
                    edtCargo.requestFocus()
                    edtCargo.error = "Campo Obrigatório"
                }
                empresa.isEmpty() -> {
                    edtEmpresa.requestFocus()
                    edtEmpresa.error = "Campo Obrigatório"
                }
                descricao.isEmpty() -> {
                    edtDescricao.requestFocus()
                    edtDescricao.error = "Campo Obrigatório"
                }
                horario.isEmpty() -> {
                    edtHorario.requestFocus()
                    edtHorario.error = "Campo Obrigatório"
                }
                requisitos.isEmpty() -> {
                    edtRequisitos.requestFocus()
                    edtRequisitos.error = "Campo Obrigatório"
                }
                !requisitos.contains(",") -> {
                    edtRequisitos.requestFocus()
                    edtRequisitos.error = "Coloque vírgula no final de cada requisito"
                }
                else -> {
                    Toast.makeText(this@FormVagaActivity, "ok", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


}