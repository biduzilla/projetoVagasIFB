package com.toddy.vagasifb.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.toddy.vagasifb.databinding.ItemCurriculoVagaBinding
import com.toddy.vagasifb.databinding.ItemProfessorBinding
import com.toddy.vagasifb.model.Curriculo
import com.toddy.vagasifb.model.User

class UserAdapter(usuarios: List<User> = emptyList()) :
    RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    private val usuarios = usuarios.toMutableList()

    inner class ViewHolder(private val binding: ItemProfessorBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private lateinit var usuario: User


        fun vincula(user: User) {
            this.usuario = user

            with(binding) {
                tvNome.text = user.email
                tvTelefone.text = user.telefone
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemProfessorBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = usuarios[position]
        holder.vincula(user)
    }

    override fun getItemCount(): Int = usuarios.size

    fun atualiza(usuarios: List<User>) {
        this.usuarios.clear()
        this.usuarios.addAll(usuarios)
        notifyDataSetChanged()
    }
}