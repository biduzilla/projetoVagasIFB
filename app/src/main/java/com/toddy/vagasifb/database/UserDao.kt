package com.toddy.vagasifb.database

import android.app.Activity
import android.content.Context
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.toddy.vagasifb.model.User
import com.toddy.vagasifb.extensions.iniciaActivity
import com.toddy.vagasifb.ui.activity.MainActivity
import com.toddy.vagasifb.ui.activity.app.LoginActivity
import com.toddy.vagasifb.ui.activity.empregador.EmpregadorMainActivity
import com.toddy.vagasifb.utils.FireBaseHelper

class UserDao {
    fun criarConta(
        context: Context,
        email: String,
        senha: String,
        userCriado: (userCriado: User?) -> Unit
    ) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, senha)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    task.result.user?.let { FirebaseUser ->
                        val idUser = FirebaseUser.uid
                        userCriado(
                            User(
                                id = idUser,
                                email = email,
                                senha = senha
                            )
                        )
                        context.iniciaActivity(LoginActivity::class.java)
                    }
                } else {
                    Toast.makeText(
                        context,
                        FireBaseHelper.validaErros(task.exception.toString()),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    fun salvarUser(user: User) {
        FirebaseDatabase.getInstance().reference
            .child("usuarios")
            .child("empregador")
            .child(user.id)
            .setValue(user)
    }

    fun recuperarConta(context: Context, email: String) {
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        context,
                        "E-mail para trocar de senha enviado",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        context,
                        FireBaseHelper.validaErros(task.exception.toString()),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    fun login(context: Context, email: String, senha: String) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, senha)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    context.iniciaActivity(EmpregadorMainActivity::class.java)
                } else {
                    Toast.makeText(
                        context,
                        FireBaseHelper.validaErros(task.exception.toString()),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}