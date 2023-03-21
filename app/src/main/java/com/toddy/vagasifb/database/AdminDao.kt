package com.toddy.vagasifb.database

import android.app.Activity
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.toddy.vagasifb.model.User

class AdminDao {
    fun cadastraEmpregador(activity: Activity, email: String, senha: String) {

    }

    fun recuperaUsers(activity: Activity, usuariosRecuperados: (usuarios: List<User>?) -> Unit) {
        val usuarios = mutableListOf<User>()
        UserDao().getIdUser(activity)?.let {
            FirebaseDatabase.getInstance().reference
                .child("usuarios")
                .child("empregador")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            snapshot.children.forEach {
                                it.getValue(User::class.java)?.let { user ->
                                    usuarios.add(user)
                                }
                            }
                            usuariosRecuperados(usuarios.toList())
                        } else {
                            usuariosRecuperados(null)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })
        }

    }
}