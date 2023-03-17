package com.toddy.vagasifb.ui.activity.aluno.framents

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.toddy.vagasifb.database.VagaDao
import com.toddy.vagasifb.databinding.ActivityAlunoMainBinding
import com.toddy.vagasifb.databinding.FragmentHomeBinding
import com.toddy.vagasifb.ui.activity.CHAVE_USER
import com.toddy.vagasifb.ui.activity.CHAVE_VAGA_ID
import com.toddy.vagasifb.ui.activity.app.DetalhesVagaActivity
import com.toddy.vagasifb.ui.adapter.VagasAdapter


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        configClicks()
        configRv()
    }

    override fun onResume() {
        super.onResume()
        val adapter = VagasAdapter(requireContext())
        VagaDao().recuperarVagas(requireActivity()) { vagas ->
            if (vagas.isEmpty()) {
                adapter.atualiza(emptyList())
            } else {
                adapter.atualiza(vagas)
            }
        }
    }

    private fun configRv() {
        val adapter = VagasAdapter(requireContext())
        with(binding) {
            rvVagas.adapter = adapter
            rvVagas.layoutManager = LinearLayoutManager(requireActivity())
            adapter.onClick = {
                Intent(requireContext(), DetalhesVagaActivity::class.java).apply {
                    putExtra(CHAVE_VAGA_ID, it.id)
                    putExtra(CHAVE_USER, true)
                    startActivity(this)
                }
            }
        }
    }
}