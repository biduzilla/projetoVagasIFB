package com.toddy.vagasifb.ui.activity.aluno

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.toddy.vagasifb.database.AlunoDao
import com.toddy.vagasifb.databinding.ActivityCadastrarCvBinding
import com.toddy.vagasifb.model.Curriculo
import com.toddy.vagasifb.ui.activity.CHAVE_CV_UPDATE
import com.toddy.vagasifb.ui.activity.CHAVE_USER

class CadastrarCvActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityCadastrarCvBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)



        configClicks()
        updateCv()
    }

    private fun updateCv() {
        val isUpdate = intent.getBooleanExtra(CHAVE_CV_UPDATE, false)

        if (isUpdate) {
            binding.toolbarVoltar.tvTitulo.text = "Atualizar Currículo"
            binding.btnLogin.text = "Atualizar"

            AlunoDao().recuperaCv(this) { cv ->
                cv?.let {
                    preencheDados(cv)
                }
            }
        } else {
            binding.toolbarVoltar.tvTitulo.text = "Cadastrar Currículo"
        }
    }

    private fun preencheDados(curriculo: Curriculo) {
        with(binding) {
            edtNome.setText(curriculo.nome)
            edtTelefone.setText(curriculo.telefone)
            edtSobre.setText(curriculo.sobre)
            edtSemestre.setText(curriculo.semestre)
            edtExpeiencias.setText(
                curriculo.experiencias.toString().replace("[", "").replace("]", "")
                    .replace(",", ".")
            )
            edtQualificacoes.setText(
                curriculo.qualificacoes.toString().replace("[", "").replace("]", "")
                    .replace(",", ".")
            )
        }
    }

    private fun configClicks() {
        binding.toolbarVoltar.btnVoltar.setOnClickListener {
            finish()
        }
        binding.btnLogin.setOnClickListener {
            validaDados()
        }
    }

    private fun validaDados() {
        with(binding) {
            val nome: String = edtNome.text.toString().trim()
            val telefone: String = edtTelefone.text.toString().trim()
            val sobre: String = edtSobre.text.toString().trim()
            val semestre: String = edtSemestre.text.toString().trim()
            val experiencia: String = edtExpeiencias.text.toString().trim()
            val qualificacoes: String = edtQualificacoes.text.toString().trim()

            when {
                nome.isEmpty() -> {
                    edtNome.requestFocus()
                    edtNome.error = "Campo Obrigatório"
                }
                telefone.isEmpty() -> {
                    edtTelefone.requestFocus()
                    edtTelefone.error = "Campo Obrigatório"
                }
                sobre.isEmpty() -> {
                    edtSobre.requestFocus()
                    edtSobre.error = "Campo Obrigatório"
                }
                semestre.isEmpty() -> {
                    edtSemestre.requestFocus()
                    edtSemestre.error = "Campo Obrigatório"
                }
                experiencia.isEmpty() -> {
                    edtExpeiencias.requestFocus()
                    edtExpeiencias.error = "Campo Obrigatório"
                }
                !experiencia.contains(".") -> {
                    edtExpeiencias.requestFocus()
                    edtExpeiencias.error = "Coloque '.' no final de cada experiência"
                }
                qualificacoes.isEmpty() -> {
                    edtQualificacoes.requestFocus()
                    edtQualificacoes.error = "Campo Obrigatório"
                }
                !qualificacoes.contains(".") -> {
                    edtQualificacoes.requestFocus()
                    edtQualificacoes.error = "Coloque '.' no final de cada experiência"
                }
                else -> {
                    progressBar.visibility = View.VISIBLE
                    btnLogin.visibility = View.GONE

                    val qualificacoesLst: MutableList<String> =
                        experiencia.split(".").toMutableList()
                    val experienciaLst: MutableList<String> =
                        qualificacoes.split(".").toMutableList()

                    qualificacoesLst.forEachIndexed { index, item ->
                        if (item.isEmpty()) {
                            qualificacoesLst.removeAt(index)
                        }
                    }

                    experienciaLst.forEachIndexed { index, item ->
                        if (item.isEmpty()) {
                            experienciaLst.removeAt(index)
                        }
                    }

                    salvarCv(
                        Curriculo(
                            id = "",
                            nome = nome,
                            telefone = telefone,
                            sobre = sobre,
                            semestre = semestre,
                            experiencias = experienciaLst,
                            qualificacoes = qualificacoesLst,
                            emptyList()
                        )
                    )
                }
            }

        }
    }

    private fun salvarCv(curriculo: Curriculo) {
        AlunoDao().salvarCv(this, curriculo)
    }
}