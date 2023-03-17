package com.toddy.vagasifb.ui.activity.empregador

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.toddy.vagasifb.database.VagaDao
import com.toddy.vagasifb.databinding.ActivityEmpregadorMainBinding
import com.toddy.vagasifb.extensions.iniciaActivity
import com.toddy.vagasifb.model.Vaga
import com.toddy.vagasifb.ui.activity.CHAVE_VAGA_ID
import com.toddy.vagasifb.ui.activity.Constants
import com.toddy.vagasifb.ui.activity.app.DetalhesVagaActivity
import com.toddy.vagasifb.ui.adapter.VagasAdapter

class EmpregadorMainActivity : AppCompatActivity() {

    private val adapter = VagasAdapter(this)
    private var vagas = mutableListOf<Vaga>()

    private val binding by lazy {
        ActivityEmpregadorMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        configRv()
        configClicks()
        binding.toolbarSair.tvTitulo.text = "Minhas Vagas"

        recuperarVagas()

    }

    override fun onResume() {
        super.onResume()
        recuperarVagas()
    }

    private fun recuperarVagas() {
        VagaDao().recuperarMinhasVagas(this, binding.progressBar) {
            it?.let {
                adapter.atualiza(it)
                if (it.isEmpty()) {
                    Toast.makeText(
                        this,
                        "Você ainda não cadastrou nenhuma vaga",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }
        }
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
                putExtra(CHAVE_VAGA_ID, it.id)
                startActivity(this)
            }
        }
    }
}