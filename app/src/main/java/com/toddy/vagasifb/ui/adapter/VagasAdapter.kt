package com.toddy.vagasifb.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.toddy.vagasifb.databinding.ItemEmpregadorVagaBinding
import com.toddy.vagasifb.extensions.tentaCarregarImagem
import com.toddy.vagasifb.model.Vaga
import com.toddy.vagasifb.utils.GetMask

class VagasAdapter(
    private val context: Context,
    vagas: List<Vaga> = emptyList(),
    var onClick: (vaga: Vaga) -> Unit = {}
) : RecyclerView.Adapter<VagasAdapter.ViewHolder>() {

    private val vagas = vagas.toMutableList()

    inner class ViewHolder(private val binding: ItemEmpregadorVagaBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private lateinit var vaga: Vaga

        init {
            itemView.setOnClickListener {
                if (::vaga.isInitialized) {
                    onClick(vaga)
                }
            }
        }

        fun vincula(vaga: Vaga) {
            this.vaga = vaga

            with(binding) {
                tvCargo.text = vaga.cargo
                tvEmpresa.text = vaga.empresa
                tvData.text = GetMask.getDate(vaga.dataPostada!!, 1)
                imgVaga.tentaCarregarImagem(vaga.imagem!!)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VagasAdapter.ViewHolder {
        val inflater = LayoutInflater.from(context)
        val binding = ItemEmpregadorVagaBinding.inflate(inflater, parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = vagas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val vaga = vagas[position]
        holder.vincula(vaga)
    }

    fun atualiza(vagas: List<Vaga>) {
        this.vagas.clear()
        this.vagas.addAll(vagas)
        notifyDataSetChanged()
    }
}
