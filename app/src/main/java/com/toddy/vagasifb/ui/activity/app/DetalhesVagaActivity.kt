package com.toddy.vagasifb.ui.activity.app

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.toddy.vagasifb.R
import com.toddy.vagasifb.database.AlunoDao
import com.toddy.vagasifb.database.VagaDao
import com.toddy.vagasifb.databinding.ActivityDetalhesVagaBinding
import com.toddy.vagasifb.databinding.DialogDetalhesVagaDeletarBinding
import com.toddy.vagasifb.databinding.DialogDetalhesVagaParticiparBinding
import com.toddy.vagasifb.extensions.tentaCarregarImagem
import com.toddy.vagasifb.model.Vaga
import com.toddy.vagasifb.ui.activity.CHAVE_USER
import com.toddy.vagasifb.ui.activity.CHAVE_VAGA
import com.toddy.vagasifb.ui.activity.CHAVE_VAGA_ID
import com.toddy.vagasifb.ui.activity.empregador.FormVagaActivity

class DetalhesVagaActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityDetalhesVagaBinding.inflate(layoutInflater)
    }
    private var vaga: Vaga? = null
    private var vagaId: String? = null
    private var isUser: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        desativarMenu()
        changeBtn()
        configClicks()
    }

    override fun onResume() {
        super.onResume()
        binding.llRequisitos.removeAllViews()

        binding.scrollView.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE

        tentaCarregarVaga()
    }

    private fun changeBtn() {
        AlunoDao().recuperaCv(this) { cv ->
            cv?.let {
                if (it.historico.contains(vagaId)) {
                    binding.btnLogin.text = "Inscrição Feita"
                    binding.btnLogin.isEnabled = false
                }
            }
        }
    }


    private fun configClicks() {
        with(binding) {
            toolbarMenu.btnMenu.setOnClickListener {
                criarMenu()
            }
            toolbarMenu.btnVoltar.setOnClickListener {
                finish()
            }
            btnLogin.setOnClickListener {

                showDialogConfirmacao()
            }
        }
    }

    private fun showDialogConfirmacao() {
        DialogDetalhesVagaParticiparBinding.inflate(layoutInflater).apply {
            val dialog = AlertDialog.Builder(this@DetalhesVagaActivity)
                .setView(root)
                .create()
            dialog.show()

            btnCancelar.setOnClickListener {
                dialog.dismiss()
            }

            btnEnviarCv.setOnClickListener {
                if (isUser) {
                    vagaId?.let {
                        AlunoDao().participarVaga(this@DetalhesVagaActivity, it)
                    }
                } else {
                    Toast.makeText(baseContext, "mostrar inscrições", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun tentaCarregarVaga() {
        vagaId = intent.getStringExtra(CHAVE_VAGA_ID)

        vagaId?.let {
            VagaDao().recuperarVaga(this, vagaId!!, binding.progressBar) {
                vaga = it
                vaga?.let { vagaRecuperada ->
                    preencheDados(vagaRecuperada)
                    binding.scrollView.visibility = View.VISIBLE
                } ?: AlunoDao().recuperaCv(this) { cv ->
                    cv?.let {
                        cv.historico.remove(vagaId)
                        finish()
                    }
                }
            }
        }
    }

    private fun desativarMenu() {
        isUser = intent.getBooleanExtra(CHAVE_USER, false)

        if (isUser) {
            binding.toolbarMenu.btnMenu.visibility = View.GONE
        } else {
            binding.btnLogin.text = "Inscrições da Vaga"
        }
    }

    private fun preencheDados(vaga: Vaga) {
        with(binding) {
            imgVaga.tentaCarregarImagem(vaga.imagem!!)
            tvCargo.text = vaga.cargo
            tvDescricao.text = vaga.descricao
            tvHorario.text = "Horário da vaga: ${vaga.horario}"
            toolbarMenu.tvTitulo.text = vaga.empresa

            vaga.requisitos!!.forEach { requisito ->
                if (requisito.isNotEmpty()) {
                    TextView(this@DetalhesVagaActivity).apply {
                        text = requisito
                        textSize = 18f
                        this.setPadding(0, 0, 0, 16)
                        setTextColor(Color.parseColor("#212121"))
                        llRequisitos.addView(this)
                    }
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
                    Intent(this, FormVagaActivity::class.java).apply {
                        putExtra(CHAVE_VAGA, vaga)
                        startActivity(this)
                        vaga = null
                    }
                }
                R.id.menuExcluir -> {
                    dialogExcluirVaga()
                }
            }
            return@setOnMenuItemClickListener true
        }
        popupMenu.show()
    }

    private fun dialogExcluirVaga() {
        DialogDetalhesVagaDeletarBinding.inflate(layoutInflater).apply {
            val dialog = AlertDialog.Builder(this@DetalhesVagaActivity)
                .setView(root)
                .create()
            dialog.show()

            btnCancelar.setOnClickListener {
                dialog.dismiss()
            }

            btnApagar.setOnClickListener {
                VagaDao().apagarVaga(this@DetalhesVagaActivity, vagaId!!)
                finish()
            }

        }
    }
}