package com.toddy.vagasifb.ui.activity.aluno.framents

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.toddy.vagasifb.database.AlunoDao
import com.toddy.vagasifb.database.VagaDao
import com.toddy.vagasifb.databinding.FragmentVagasBinding
import com.toddy.vagasifb.model.Curriculo
import com.toddy.vagasifb.model.Vaga
import com.toddy.vagasifb.ui.activity.CHAVE_USER
import com.toddy.vagasifb.ui.activity.CHAVE_VAGA_ID
import com.toddy.vagasifb.ui.activity.app.DetalhesVagaActivity
import com.toddy.vagasifb.ui.adapter.VagasAdapter

class VagasFragment : Fragment() {

    private var _binding: FragmentVagasBinding? = null
    private val binding get() = _binding!!
    private val adapter = VagasAdapter()
    private var vagas = mutableListOf<Vaga>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVagasBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbarTitulo.tvTitulo.text = "Vagas Inscritas"
        configRv()
    }

    override fun onResume() {
        super.onResume()
        vagas.clear()
        tentaRecuperarVagas()
    }

    private fun configRv() {
        vagas.clear()
        with(binding) {
            rvVagas.adapter = adapter
            rvVagas.layoutManager = LinearLayoutManager(requireActivity())

            adapter.onClick = {
                Intent(requireActivity(), DetalhesVagaActivity::class.java).apply {
                    putExtra(CHAVE_VAGA_ID, it.id)
                    putExtra(CHAVE_USER, true)
                    startActivity(this)
                }
            }
        }
    }

    private fun tentaRecuperarVagas() {
        binding.progressBar.visibility = View.VISIBLE
        AlunoDao().recuperaCv(requireActivity()) { cv ->
            cv?.historico?.forEach { vagaId ->
                VagaDao().recuperarVaga(requireActivity(), vagaId) { vaga ->
                    vaga?.let { vagaRecuperada ->
                        vagas.add(vagaRecuperada)

                        if (vagas.size == cv.historico.size){
                            showView()
                        }

                    } ?: atualizarHistoricoCv(cv, vagaId)
                }
            }
        }
    }

    private fun showView() {
        with(binding) {
            if (vagas.isEmpty()) {
                progressBar.visibility = View.GONE
                llInfo.visibility = View.VISIBLE
                rvVagas.visibility = View.GONE
            } else {
                adapter.atualiza(vagas)
                progressBar.visibility = View.GONE
                llInfo.visibility = View.GONE
                rvVagas.visibility = View.VISIBLE
            }
        }
    }

    private fun atualizarHistoricoCv(curriculo: Curriculo, vagaId: String) {
        curriculo.historico.remove(vagaId)
        AlunoDao().salvarCv(requireActivity(), curriculo)
    }
}