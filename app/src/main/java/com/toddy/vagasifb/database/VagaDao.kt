package com.toddy.vagasifb.database

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.toddy.vagasifb.model.Vaga
import java.sql.Timestamp

class VagaDao {

    fun salvarImagemVagaFirebase(
        imagem: String,
        idVaga: String,
        context: Context,
        urlCriado: (url: String?) -> Unit
    ) {
        getIdUser()?.let { idUser ->
            val storage = FirebaseStorage.getInstance().reference
                .child("imagens")
                .child("vagas")
                .child(idUser)
                .child(idVaga)
                .child("vaga.jpeg")

            storage.putFile(Uri.parse(imagem)).apply {
                addOnSuccessListener {
                    storage.downloadUrl.addOnCompleteListener { task ->
                        urlCriado(task.result.toString())
                    }
                }.addOnFailureListener {
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun salvarVagaUser(vaga: Vaga, activity: Activity, isNovo: Boolean) {
        getIdUser()?.let { idUser ->

            val vagaMeuRef = FirebaseDatabase.getInstance().reference
                .child("empregadores")
                .child(idUser)
                .child(vaga.id)

            vagaMeuRef.setValue(vaga)

            if (isNovo) {
                vagaMeuRef.child("dataPostada").setValue(ServerValue.TIMESTAMP)
            }
            activity.finish()
        }


    }

    private fun getIdUser(): String? {
        FirebaseAuth.getInstance().currentUser?.let {
            return it.uid
        }
        return null
    }
}