package com.toddy.vagasifb.database

import android.app.Activity
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.toddy.vagasifb.model.Curriculo

class AlunoDao {

    fun recuperaCv(
        activity: Activity,
        idUser: String,
        cvRecuperada: (cv: Curriculo?) -> Unit
    ) {

        FirebaseDatabase.getInstance().reference
            .child("alunos")
            .child(idUser)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        snapshot.getValue(Curriculo::class.java)
                            ?.let { cv ->
                                cvRecuperada(cv)
                            }
                    } else {
                        cvRecuperada(null)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

    }


    fun salvarCv(activity: Activity, curriculo: Curriculo) {
        UserDao().getIdUser(activity)?.let { idUser ->
            curriculo.id = idUser
            FirebaseDatabase.getInstance().reference
                .child("alunos")
                .child(idUser)
                .setValue(curriculo)

            activity.finish()
        }
    }

    fun participarVaga(activity: Activity, idVaga: String) {
        UserDao().getIdUser(activity)?.let { idUser ->
            recuperaCv(activity, idUser) {
                it?.let {
                    val cv = it
                    cv.historico.add(idVaga)
                    VagaDao().recuperarVaga(activity, idVaga) { vagaRecuperada ->

                        vagaRecuperada?.let { vaga ->
                            val userId = UserDao().getIdUser(activity)

                            userId?.let {
                                vaga.candidaturas.add(userId)
                                VagaDao().salvarVagaUser(
                                    vaga, activity,
                                    isNovo = false,
                                    isSalvarCv = true,
                                    idUser = vaga.idDono!!
                                )
                            }
                        }
                    }
                    salvarCv(activity, cv)
                    Toast.makeText(
                        activity.baseContext,
                        "Currículo enviado com sucesso!!",
                        Toast.LENGTH_SHORT
                    ).show()
                } ?: Toast.makeText(
                    activity.baseContext,
                    "Currículo não cadastrado, cadastre um para poder participar da vaga",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }


}