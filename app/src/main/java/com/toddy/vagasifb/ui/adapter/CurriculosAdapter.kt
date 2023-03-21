package com.toddy.vagasifb.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.toddy.vagasifb.databinding.ItemCurriculoVagaBinding
import com.toddy.vagasifb.databinding.ItemEmpregadorVagaBinding
import com.toddy.vagasifb.model.Curriculo

class CurriculosAdapter(

    curriculos: List<Curriculo> = emptyList(),
    var onClick: (curriculo: Curriculo) -> Unit = {}

) : RecyclerView.Adapter<CurriculosAdapter.ViewHolder>() {
    private val curriculos = curriculos.toMutableList()

    inner class ViewHolder(private val binding: ItemCurriculoVagaBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private lateinit var curriculo: Curriculo

        init {
            itemView.setOnClickListener {
                if (::curriculo.isInitialized) {
                    onClick(curriculo)
                }
            }
        }

        fun vincula(cv: Curriculo) {
            this.curriculo = cv

            with(binding) {
                tvNome.text = cv.nome
                tvTelefone.text = cv.telefone
                tvEmail.text = cv.email
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCurriculoVagaBinding.inflate(inflater, parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cv = curriculos[position]
        holder.vincula(cv)
    }

    override fun getItemCount(): Int = curriculos.size

    fun atualiza(cvs: List<Curriculo>) {
        this.curriculos.clear()
        this.curriculos.addAll(cvs)
        notifyDataSetChanged()
    }
}