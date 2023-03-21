package com.toddy.vagasifb.ui.activity.empregador

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.toddy.vagasifb.database.AlunoDao
import com.toddy.vagasifb.database.VagaDao
import com.toddy.vagasifb.databinding.ActivityCandidaturasBinding
import com.toddy.vagasifb.model.Curriculo
import com.toddy.vagasifb.ui.activity.CHAVE_VAGA_ID
import com.toddy.vagasifb.ui.adapter.CurriculosAdapter

class CandidaturasActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityCandidaturasBinding.inflate(layoutInflater)
    }

    private val adapter = CurriculosAdapter()
    private val cvs: MutableList<Curriculo> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        configClicks()
        configRv()
    }

    private fun configRv() {
        with(binding) {
            rvCVs.adapter = adapter
            rvCVs.layoutManager = LinearLayoutManager(this@CandidaturasActivity)
            adapter.onClick = {
                Toast.makeText(this@CandidaturasActivity, it.nome, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun configClicks() {
        with(binding) {
            toolbarVoltar.tvTitulo.text = "Inscrições da Vaga"
            toolbarVoltar.btnVoltar.setOnClickListener {
                finish()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        cvs.clear()
        recuperaDadosVaga()
    }

    private fun recuperaDadosVaga() {
        intent.getStringExtra(CHAVE_VAGA_ID)?.let { vagaId ->
            recuperaInscricoes(vagaId)
        }
    }

    private fun recuperaInscricoes(vagaId: String) {
        VagaDao().recuperarMinhaVaga(this, vagaId) { vagaRecuperada ->
            vagaRecuperada?.let {

                binding.progressBar.visibility = View.GONE

                val candidaturas = it.candidaturas

                if (candidaturas.isNotEmpty()) {

                    recuperaCvParticipantes(candidaturas)
                } else {
                    binding.llInfo.visibility = View.VISIBLE
                    binding.rvCVs.visibility = View.GONE
                }
            }
        }
    }

    private fun recuperaCvParticipantes(candidaturas: MutableList<String>) {
        candidaturas.forEach { idCv ->
            AlunoDao().recuperaCv(this@CandidaturasActivity, idCv) { cvRecuperadao ->
                cvRecuperadao?.let { cv ->

                    cvs.add(cv)

                    if (candidaturas.size == cvs.size) {

                        adapter.atualiza(cvs)
                        binding.llInfo.visibility = View.GONE
                        binding.rvCVs.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

}