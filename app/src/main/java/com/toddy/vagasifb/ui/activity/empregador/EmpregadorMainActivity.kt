package com.toddy.vagasifb.ui.activity.empregador

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.toddy.vagasifb.databinding.ActivityEmpregadorMainBinding
import com.toddy.vagasifb.extensions.iniciaActivity
import com.toddy.vagasifb.ui.activity.CHAVE_VAGA_ID
import com.toddy.vagasifb.ui.activity.Constants
import com.toddy.vagasifb.ui.activity.app.DetalhesVagaActivity
import com.toddy.vagasifb.ui.adapter.VagasAdapter

class EmpregadorMainActivity : AppCompatActivity() {

    private val adapter = VagasAdapter(this)

    private val binding by lazy {
        ActivityEmpregadorMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        configRv()
        configClicks()
        binding.toolbarSair.tvTitulo.text = "Minhas Vagas"
        adapter.atualiza(Constants.vagasList)

    }

    private fun configClicks() {
        binding.fabAdd.setOnClickListener {
            iniciaActivity(FormVagaActivity::class.java)
        }
    }


    private fun configRv() {
        val rv = binding.rvVagas
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(this)
        adapter.onClick = {
            Intent(this, DetalhesVagaActivity::class.java).apply {
                putExtra(CHAVE_VAGA_ID, it)
                startActivity(this)
            }
        }
    }
}