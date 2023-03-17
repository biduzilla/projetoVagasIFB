package com.toddy.vagasifb.ui.activity.aluno.framents

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.toddy.vagasifb.database.VagaDao
import com.toddy.vagasifb.databinding.FragmentHomeBinding
import com.toddy.vagasifb.ui.activity.CHAVE_USER
import com.toddy.vagasifb.ui.activity.CHAVE_VAGA_ID
import com.toddy.vagasifb.ui.activity.app.DetalhesVagaActivity
import com.toddy.vagasifb.ui.adapter.VagasAdapter


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val adapter = VagasAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configRv()
    }

    override fun onResume() {
        super.onResume()

        VagaDao().recuperarVagas(requireActivity()) { vagas ->
            if (vagas == null) {
                adapter.atualiza(emptyList())
                Log.i("infoteste", "vazio")
            } else {
                Log.i("infoteste", "OK")
                adapter.atualiza(vagas)
            }
        }
    }

    private fun configRv() {
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