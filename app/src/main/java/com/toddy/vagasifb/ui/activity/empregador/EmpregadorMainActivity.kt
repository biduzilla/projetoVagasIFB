package com.toddy.vagasifb.ui.activity.empregador

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.toddy.vagasifb.databinding.ActivityEmpregadorMainBinding
import com.toddy.vagasifb.ui.activity.Constants
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

        adapter.atualiza(Constants.vagasList)

    }

    private fun configClicks() {
        binding.fabAdd.setOnClickListener {
            Toast.makeText(this, Constants.vagasList.toString(), Toast.LENGTH_SHORT).show()
        }
    }


    private fun configRv() {
        val rv = binding.rvVagas
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(this)
        adapter.onClick = {
            Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
        }

    }
}