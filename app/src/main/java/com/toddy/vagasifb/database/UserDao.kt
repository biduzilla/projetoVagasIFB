package com.toddy.vagasifb.database

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.toddy.vagasifb.databinding.ActivityLoginBinding
import com.toddy.vagasifb.databinding.DialogUserLogarBinding
import com.toddy.vagasifb.extensions.iniciaActivity
import com.toddy.vagasifb.model.User
import com.toddy.vagasifb.ui.activity.admin.ProfCadastradosActivity
import com.toddy.vagasifb.ui.activity.aluno.AlunoMainActivity
import com.toddy.vagasifb.ui.activity.app.LoginActivity
import com.toddy.vagasifb.ui.activity.empregador.EmpregadorMainActivity
import com.toddy.vagasifb.utils.FireBaseHelper

class UserDao {
    fun criarConta(
        context: Context,
        email: String,
        senha: String,
        telefone: String,
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
                                telefone = telefone,
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
//        FirebaseDatabase.getInstance().reference
//            .child("usuarios")
//            .child("empregador")
//            .child(user.id)
//            .setValue(user)
        FirebaseDatabase.getInstance().reference
            .child("usuarios")
            .child("alunos")
            .child(user.id!!)
            .setValue(user)

//        FirebaseDatabase.getInstance().reference
//            .child("usuarios")
//            .child("admin")
//            .child(user.id)
//            .setValue(user)
    }

    fun salvarEmpregador(user: User) {
        FirebaseDatabase.getInstance().reference
            .child("usuarios")
            .child("empregador")
            .child(user.id!!)
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

    fun login(
        activity: Activity,
        email: String,
        senha: String,
        binding: ActivityLoginBinding? = null
    ) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, senha)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    redirecionaAcesso(activity)
                } else {
                    binding?.let {
                        it.progressBar.visibility = View.GONE
                        it.btnLogin.visibility = View.VISIBLE
                    }
                    Toast.makeText(
                        activity.baseContext,
                        FireBaseHelper.validaErros(task.exception.toString()),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun redirecionaAcesso(activity: Activity) {
        getIdUser(activity)?.let { userId ->
            FirebaseDatabase.getInstance().reference
                .child("usuarios")
                .child("alunos")
                .child(userId)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            activity.iniciaActivity(AlunoMainActivity::class.java)
                            activity.finish()
                        } else {
                            FirebaseDatabase.getInstance().reference
                                .child("usuarios")
                                .child("admin")
                                .child(userId)
                                .addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        if (snapshot.exists()) {
                                            activity.iniciaActivity(ProfCadastradosActivity::class.java)
                                            activity.finish()
                                        } else {
                                            FirebaseDatabase.getInstance().reference
                                                .child("usuarios")
                                                .child("empregador")
                                                .child(userId)
                                                .addListenerForSingleValueEvent(object :
                                                    ValueEventListener {
                                                    override fun onDataChange(snapshot: DataSnapshot) {
                                                        activity.iniciaActivity(
                                                            EmpregadorMainActivity::class.java
                                                        )
                                                        activity.finish()
                                                    }

                                                    override fun onCancelled(error: DatabaseError) {
                                                        TODO("Not yet implemented")
                                                    }
                                                })
                                        }
                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                        TODO("Not yet implemented")
                                    }
                                })
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                })

        }
    }

    fun getIdUser(activity: Activity): String? {
        FirebaseAuth.getInstance().currentUser?.let {
            return it.uid
        }
        Toast.makeText(activity.baseContext, "Usuario não logado na conta", Toast.LENGTH_SHORT)
            .show()
        dialogLogarNovamente(activity)
        return null
    }

    fun getEmailUser(activity: Activity): String? {
        FirebaseAuth.getInstance().currentUser?.let {
            return it.email
        }
        Toast.makeText(activity.baseContext, "Usuario não logado na conta", Toast.LENGTH_SHORT)
            .show()

        return null
    }

    private fun dialogLogarNovamente(activity: Activity) {
        DialogUserLogarBinding.inflate(activity.layoutInflater).apply {
            val dialog = AlertDialog.Builder(activity)
                .setView(root)
                .create()

            dialog.setCancelable(false)
            dialog.setCanceledOnTouchOutside(false)
            dialog.show()

            btnLogin.setOnClickListener {
                FirebaseAuth.getInstance().signOut()
                activity.iniciaActivity(LoginActivity::class.java)
                activity.finish()
            }
        }
    }

    fun getUser(activity: Activity, userRecuperado: (userRecuperado: User) -> Unit) {
        getIdUser(activity)?.let { idUser ->
            FirebaseDatabase.getInstance().reference
                .child("usuarios")
                .child("alunos")
                .child(idUser)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            snapshot.getValue(User::class.java)?.let {
                                userRecuperado(it)
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                })
        }
    }
}