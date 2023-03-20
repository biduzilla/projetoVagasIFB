package com.toddy.vagasifb.database

import android.app.Activity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.toddy.vagasifb.model.Curriculo

class AlunoDao {

    fun recuperaCv(
        activity: Activity,
        cvRecuperada: (cv: Curriculo?) -> Unit
    ) {
        UserDao().getIdUser(activity.baseContext)?.let { idUser ->
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
                        }else{
                            cvRecuperada(null)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                })
        }
    }

    fun salvarCv(activity: Activity, curriculo: Curriculo) {

        UserDao().getIdUser(activity.baseContext)?.let { idUser ->
            FirebaseDatabase.getInstance().reference
                .child("alunos")
                .child(idUser)
                .setValue(curriculo)

            activity.finish()
        }
    }


}