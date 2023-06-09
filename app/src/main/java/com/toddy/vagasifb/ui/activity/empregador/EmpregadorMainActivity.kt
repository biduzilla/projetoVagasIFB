package com.toddy.vagasifb.ui.activity.empregador

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.toddy.vagasifb.database.VagaDao
import com.toddy.vagasifb.databinding.ActivityEmpregadorMainBinding
import com.toddy.vagasifb.extensions.iniciaActivity
import com.toddy.vagasifb.model.Vaga
import com.toddy.vagasifb.ui.activity.CHAVE_VAGA_ID
import com.toddy.vagasifb.ui.activity.app.DetalhesVagaActivity
import com.toddy.vagasifb.ui.activity.app.LoginActivity
import com.toddy.vagasifb.ui.adapter.VagasAdapter

class EmpregadorMainActivity : AppCompatActivity() {

    private val adapter = VagasAdapter()
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
    }

    override fun onResume() {
        super.onResume()
        recuperarVagas()

    }

    private fun recuperarVagas() {
        VagaDao().recuperarMinhasVagas(this, binding.progressBar) {
            if (it.isEmpty()) {
                adapter.atualiza(emptyList())
                binding.llInfo.visibility = View.VISIBLE
                binding.rvVagas.visibility = View.GONE
            } else {
                adapter.atualiza(it)
                binding.rvVagas.visibility = View.VISIBLE
                binding.llInfo.visibility = View.GONE
            }
        }
    }

    private fun configClicks() {
        binding.fabAdd.setOnClickListener {
            iniciaActivity(FormVagaActivity::class.java)
        }
        binding.toolbarSair.btnSair.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            iniciaActivity(LoginActivity::class.java)
            finish()
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