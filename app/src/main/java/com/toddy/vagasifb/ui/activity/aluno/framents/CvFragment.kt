package com.toddy.vagasifb.ui.activity.aluno.framents

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.toddy.vagasifb.R
import com.toddy.vagasifb.database.AlunoDao
import com.toddy.vagasifb.databinding.FragmentCvBinding
import com.toddy.vagasifb.extensions.iniciaActivity
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
        recuperaCv()
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
            AlunoDao().recuperaCv(it).let { cvExist ->
                with(binding) {
                    if (!cvExist) {
                        llInfo.visibility = View.VISIBLE
                    } else {
                        scrollView.visibility = View.VISIBLE
                        llInfo.visibility = View.GONE
                    }
                }

            }
        }
    }

}