package com.toddy.vagasifb.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.toddy.vagasifb.databinding.ItemCurriculoVagaBinding
import com.toddy.vagasifb.model.Curriculo

class CurriculosAdapter(
    cvs: List<Curriculo> = emptyList(),
    var onClick: (curriculo: Curriculo) -> Unit = {}
) : RecyclerView.Adapter<CurriculosAdapter.ViewHolder>() {
    private val cvs = cvs.toMutableList()

    inner class ViewHolder(private val binding: ItemCurriculoVagaBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private lateinit var cv: Curriculo

        init {
            itemView.setOnClickListener {
                if (::cv.isInitialized) {
                    onClick(cv)
                }
            }
        }

        fun vincula(cv: Curriculo) {
            this.cv = cv

            with(binding) {
                tvNome.text = cv.nome
                tvTelefone.text = cv.telefone
                tvEmail.text = cv.email
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemCurriculoVagaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cv = cvs[position]
        holder.vincula(cv)
    }

    override fun getItemCount(): Int = cvs.size

    fun atualiza(cvs: List<Curriculo>) {
        this.cvs.clear()
        this.cvs.addAll(cvs)
        notifyDataSetChanged()
    }
}