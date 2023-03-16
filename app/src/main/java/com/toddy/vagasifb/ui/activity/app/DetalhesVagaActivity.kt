package com.toddy.vagasifb.ui.activity.app

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.marginBottom
import com.toddy.vagasifb.R
import com.toddy.vagasifb.databinding.ActivityDetalhesVagaBinding
import com.toddy.vagasifb.databinding.ToolbarVoltarMenuBinding
import com.toddy.vagasifb.extensions.iniciaActivity
import com.toddy.vagasifb.extensions.tentaCarregarImagem
import com.toddy.vagasifb.model.Vaga
import com.toddy.vagasifb.ui.activity.CHAVE_VAGA_ID
import com.toddy.vagasifb.ui.activity.empregador.EmpregadorMainActivity

class DetalhesVagaActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityDetalhesVagaBinding.inflate(layoutInflater)
    }
    private var vaga: Vaga? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        tentaCarregarVaga()
        configClicks()
    }

    private fun configClicks() {
        with(binding) {
            toolbarMenu.btnMenu.setOnClickListener {
                criarMenu()
            }
            toolbarMenu.btnVoltar.setOnClickListener {
                iniciaActivity(EmpregadorMainActivity::class.java)
                finish()
            }
        }
    }

    private fun tentaCarregarVaga() {
        intent.getParcelableExtra<Vaga>(CHAVE_VAGA_ID)?.let { vagaCarregada ->
            vaga = vagaCarregada
            vaga?.let { preencheDados(it) }
        }
    }

    private fun preencheDados(vaga: Vaga) {
        with(binding) {
            imgVaga.tentaCarregarImagem(vaga.imagem)
            tvCargo.text = vaga.cargo
            tvDescricao.text = vaga.descricao
            tvHorario.text = "Horário da vaga: ${vaga.horario}"
            toolbarMenu.tvTitulo.text = vaga.empresa

            vaga.requisitos.forEach {requisito ->
                TextView(this@DetalhesVagaActivity).apply {
                    text = requisito
                    Log.i("infoteste", requisito)
                    textSize = 18f
                    setPadding(0,0,0,8)
                    setTextColor(Color.parseColor("#212121"))
                    llRequisitos.addView(this)
                }

            }
        }
    }

    private fun criarMenu() {
        val popupMenu = PopupMenu(this, binding.toolbarMenu.btnMenu)
        popupMenu.menuInflater.inflate(R.menu.menu_detalhe_vaga, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menuEditar -> {
                    Toast.makeText(this, "editar", Toast.LENGTH_SHORT).show()
                }
                R.id.menuExcluir -> {
                    Toast.makeText(this, "excluir", Toast.LENGTH_SHORT).show()
                }
            }
            return@setOnMenuItemClickListener true
        }
        popupMenu.show()
    }
}