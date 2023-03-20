package com.toddy.vagasifb.ui.activity.aluno.framents

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.toddy.vagasifb.R
import com.toddy.vagasifb.database.AlunoDao
import com.toddy.vagasifb.database.UserDao
import com.toddy.vagasifb.databinding.FragmentCvBinding
import com.toddy.vagasifb.extensions.iniciaActivity
import com.toddy.vagasifb.model.Curriculo
import com.toddy.vagasifb.ui.activity.aluno.CadastrarCvActivity


class CvFragment : Fragment() {

    private var _binding: FragmentCvBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCvBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configClicks()
        binding.toolbarVoltar.tvTitulo.text = "Meu Currículo"

    }

    override fun onResume() {
        super.onResume()
        limparLLView()
        recuperaCv()
    }

    private fun limparLLView() {
        binding.llExpericencias.removeAllViews()
        binding.llQualificacoes.removeAllViews()
    }

    private fun configClicks() {
        activity?.let { activity ->
            with(binding) {
                btnCadastrarCv.setOnClickListener {
                    activity.iniciaActivity(CadastrarCvActivity::class.java)
                }
            }
        }
    }

    private fun recuperaCv() {
        activity?.let {
            AlunoDao().recuperaCv(it) { cvRecuperado ->
                cvRecuperado?.let { cv ->
                    showView(cv)
                } ?: cvNaoEncontrado()
            }
        }
    }

    private fun cvNaoEncontrado() {
        binding.llInfo.visibility = View.VISIBLE
        binding.scrollView.visibility = View.GONE
    }

    private fun showView(curriculo: Curriculo) {
        with(binding) {

            preencheDados(curriculo)

            llInfo.visibility = View.GONE
            scrollView.visibility = View.VISIBLE


        }
    }


    private fun preencheDados(curriculo: Curriculo) {

        with(binding) {
            UserDao().getEmailUser(requireContext())?.let { email ->
                tvEmail.text = email
            }
            tvNome.text = curriculo.nome
            tvSemestre.text = curriculo.semestre
            tvTelefone.text = curriculo.telefone
            tvSobre.text = curriculo.sobre

            curriculo.qualificacoes?.let {
                it.forEach { qualificacao ->
                    TextView(requireActivity()).apply {
                        text = qualificacao
                        textSize = 18f
                        setPadding(0, 0, 0, 8)
                        setTextColor(Color.parseColor("#388E3C"))
                        llQualificacoes.addView(this)
                    }
                }
            }

            curriculo.experiencias?.let {
                it.forEach { experiencia ->
                    TextView(requireActivity()).apply {
                        text = experiencia
                        textSize = 18f
                        setPadding(0, 0, 0, 8)
                        setTextColor(Color.parseColor("#388E3C"))
                        llExpericencias.addView(this)
                    }
                }
            }
        }
    }
}