package com.toddy.vagasifb.database

import android.app.Activity
import android.content.SharedPreferences
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.toddy.vagasifb.databinding.ActivityCadastrarProfBinding
import com.toddy.vagasifb.extensions.iniciaActivity
import com.toddy.vagasifb.model.User
import com.toddy.vagasifb.ui.activity.admin.ProfCadastradosActivity
import com.toddy.vagasifb.utils.FireBaseHelper
import com.toddy.vagasifb.utils.SharedPref

class AdminDao {
    fun cadastraEmpregador(
        activity: Activity,
        email: String,
        senha: String,
        telefone: String,
        binding: ActivityCadastrarProfBinding
    ) {
        FirebaseAuth.getInstance().signOut()

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, senha)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    task.result.user?.let { FirebaseUser ->
                        val idUser = FirebaseUser.uid
                        val user =
                            User(id = idUser, email = email, telefone = telefone, senha = senha)
                        loginNovamente(activity, user)
                    }
                } else {
                    Toast.makeText(
                        activity.baseContext,
                        FireBaseHelper.validaErros(task.exception.toString()),
                        Toast.LENGTH_SHORT
                    ).show()

                    binding.progressBar.visibility = View.GONE
                    binding.btnLogin.visibility = View.VISIBLE
                }
            }
    }

    private fun loginNovamente(activity: Activity, user:User) {
        val email: String
        val senha: String

        FirebaseAuth.getInstance().signOut()
        SharedPref(activity).getDadosLogin().let {
            email = it[0]
            senha = it[1]
            UserDao().login(activity, email, senha)
            UserDao().salvarEmpregador(user)
        }
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