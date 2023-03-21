package com.toddy.vagasifb.ui.activity.admin

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.toddy.vagasifb.database.AdminDao
import com.toddy.vagasifb.databinding.ActivityProfCadastradosBinding
import com.toddy.vagasifb.extensions.iniciaActivity
import com.toddy.vagasifb.ui.activity.app.LoginActivity
import com.toddy.vagasifb.ui.adapter.UserAdapter

class ProfCadastradosActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityProfCadastradosBinding.inflate(layoutInflater)
    }

    private val adapter = UserAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        configClicks()
        configRv()
    }

    private fun configRv() {
        with(binding){
            rvProf.adapter = adapter
            rvProf.layoutManager = LinearLayoutManager(baseContext)
        }
    }

    override fun onResume() {
        super.onResume()
        recuperaUser()
    }

    private fun recuperaUser() {
        AdminDao().recuperaUsers(this) { usuariosRecuperados ->
            with(binding) {
                if (usuariosRecuperados != null){
                    Log.i("infoteste", "ok")
                    adapter.atualiza(usuariosRecuperados)
                    progressBar.visibility = View.GONE
                    binding.llInfo.visibility = View.GONE
                    rvProf.visibility = View.VISIBLE
                }else{
                    Log.i("infoteste", "notOK")
                    progressBar.visibility = View.GONE
                    rvProf.visibility = View.GONE
                    llInfo.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun configClicks() {
        with(binding) {
            toolbarSair.tvTitulo.text = "Empregadores Cadastrados"
            toolbarSair.btnSair.setOnClickListener {
                iniciaActivity(LoginActivity::class.java)
                finish()
            }
            fabAdd.setOnClickListener {
                iniciaActivity(CadastrarProfActivity::class.java)
            }
        }
    }


}