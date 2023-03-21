package com.toddy.vagasifb.ui.activity.aluno.framents

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.toddy.vagasifb.database.VagaDao
import com.toddy.vagasifb.databinding.FragmentHomeBinding
import com.toddy.vagasifb.extensions.iniciaActivity
import com.toddy.vagasifb.model.Vaga
import com.toddy.vagasifb.ui.activity.CHAVE_USER
import com.toddy.vagasifb.ui.activity.CHAVE_VAGA_ID
import com.toddy.vagasifb.ui.activity.app.DetalhesVagaActivity
import com.toddy.vagasifb.ui.activity.app.LoginActivity
import com.toddy.vagasifb.ui.adapter.VagasAdapter


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val adapter = VagasAdapter()
    private val vagasRecuperadas = mutableListOf<Vaga>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbarSair.tvTitulo.text = "Vagas Recentes"
        configRv()
        configClicks()
    }

    override fun onResume() {
        super.onResume()
        vagasRecuperadas.clear()
        tentaRecuperarVagas()
    }

    private fun tentaRecuperarVagas() {
        VagaDao().recuperarVagas(requireActivity()) { vagas ->
            with(binding) {
                if (vagas == null) {
                    adapter.atualiza(emptyList())

                    llInfo.visibility = View.VISIBLE
                    rvVagas.visibility = View.GONE

                } else {

                    llInfo.visibility = View.GONE
                    rvVagas.visibility = View.VISIBLE

                    vagasRecuperadas.addAll(vagas)

                    val lstVagas = mutableListOf<Vaga>()
                    vagas.forEachIndexed { index, vaga ->
                        if (index <= 10) {
                            lstVagas.add(vaga)
                        }
                    }
                    adapter.atualiza(lstVagas)
                }
            }
        }
    }

    private fun filtrarVaga(cargo: String) {
        val lstVagas = mutableListOf<Vaga>()
        vagasRecuperadas.forEach {
            if (it.cargo!!.contains(cargo)) {
                lstVagas.add(it)
            }
        }
        adapter.atualiza(lstVagas)

    }

    private fun configClicks() {
        binding.toolbarSair.btnSair.setOnClickListener {
            activity?.let {
                FirebaseAuth.getInstance().signOut()
                it.iniciaActivity(LoginActivity::class.java)
                it.finish()
            }
        }

        binding.edtPesquisar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                filtrarVaga(binding.edtPesquisar.text.toString())
            }

        })

    }

    private fun configRv() {
        with(binding) {
            rvVagas.adapter = adapter
            rvVagas.layoutManager = LinearLayoutManager(requireActivity())
            adapter.onClick = {
                binding.edtPesquisar.text.clear()
                Intent(requireContext(), DetalhesVagaActivity::class.java).apply {
                    putExtra(CHAVE_VAGA_ID, it.id)
                    putExtra(CHAVE_USER, true)
                    startActivity(this)
                }
            }
        }
    }
}