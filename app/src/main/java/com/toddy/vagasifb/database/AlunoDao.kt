package com.toddy.vagasifb.database

import android.app.Activity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AlunoDao {

    fun recuperaCv(activity: Activity): Boolean {
        var cvExist = false
        UserDao().getIdUser(activity.baseContext)?.let {
            FirebaseDatabase.getInstance().reference
                .child("alunos")
                .child(it)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            cvExist = true
                        } else {
                            cvExist = false
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                })
        }
        return cvExist
    }
}