package com.toddy.vagasifb.ui.activity.aluno.framents

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.toddy.vagasifb.R
import com.toddy.vagasifb.databinding.FragmentCvBinding


class CvFragment : Fragment() {

    private var _binding : FragmentCvBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCvBinding.inflate(inflater, container, false)
        return binding.root
    }


}