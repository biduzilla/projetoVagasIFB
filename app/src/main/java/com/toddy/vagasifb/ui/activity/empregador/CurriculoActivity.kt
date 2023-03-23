package com.toddy.vagasifb.ui.activity.empregador

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.toddy.vagasifb.database.AlunoDao
import com.toddy.vagasifb.databinding.ActivityCurriculoBinding
import com.toddy.vagasifb.model.Curriculo
import com.toddy.vagasifb.ui.activity.CHAVE_CV_ID

class CurriculoActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityCurriculoBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.toolbarVoltar.tvTitulo.visibility = View.GONE
        configClicks()
        tentaRecuperarDados()
    }

    private fun configClicks() {
        binding.toolbarVoltar.btnVoltar.setOnClickListener { finish() }
    }

    private fun tentaRecuperarDados() {
        intent.getStringExtra(CHAVE_CV_ID)?.let { idCv ->
            AlunoDao().recuperaCv(this, idCv) { cvRecuperado ->

                cvRecuperado?.let { cv ->
                    binding.progressBar.visibility = View.GONE
                    preencheDados(cv)
                }
            }
        }
    }

    private fun preencheDados(curriculo: Curriculo) {
        with(binding) {
            tvNome.text = curriculo.nome
            tvTelefone.text = curriculo.telefone
            tvEmail.text = curriculo.email
            tvSobre.text = curriculo.sobre
            tvSemestre.text = curriculo.semestre

            curriculo.experiencias!!.forEach { experiencia ->
                if (experiencia.isNotEmpty()) {
                    TextView(this@CurriculoActivity).apply {
                        text = experiencia
                        textSize = 18f
                        this.setPadding(0, 0, 0, 16)
                        setTextColor(Color.parseColor("#212121"))
                        llExpericencias.addView(this)
                    }
                }
            }

            curriculo.qualificacoes!!.forEach { qualificacao ->
                if (qualificacao.isNotEmpty()) {
                    TextView(this@CurriculoActivity).apply {
                        text = qualificacao
                        textSize = 18f
                        this.setPadding(0, 0, 0, 16)
                        setTextColor(Color.parseColor("#212121"))
                        llQualificacoes .addView(this)
                    }
                }
            }
            scrollView.visibility = View.VISIBLE
        }
    }
}