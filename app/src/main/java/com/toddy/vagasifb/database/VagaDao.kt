package com.toddy.vagasifb.database

import android.app.Activity
import android.net.Uri
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.toddy.vagasifb.model.Vaga

class VagaDao {

    fun salvarImagemVagaFirebase(
        imagem: String,
        idVaga: String,
        activity: Activity,
        urlCriado: (url: String?) -> Unit
    ) {
        UserDao().getIdUser(activity)?.let { idUser ->
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
                    Toast.makeText(activity.baseContext, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun salvarVagaUser(
        vaga: Vaga,
        activity: Activity,
        isNovo: Boolean,
        isSalvarCv: Boolean,
        idUser: String
    ) {
        val vagaMeuRef = FirebaseDatabase.getInstance().reference
            .child("empregadores")
            .child(idUser)
            .child(vaga.id!!)

        vagaMeuRef.setValue(vaga)

        when {
            !isSalvarCv -> salvarVagaPub(vaga, isNovo)
            isNovo -> vagaMeuRef.child("dataPostada").setValue(ServerValue.TIMESTAMP)
        }

        activity.finish()

    }

    fun salvarVagaPub(vaga: Vaga, isNovo: Boolean) {
        val vagaPubRef = FirebaseDatabase.getInstance().reference
            .child("vagas")
            .child(vaga.id!!)

        vagaPubRef.setValue(vaga)

        if (isNovo) {
            vagaPubRef.child("dataPostada").setValue(ServerValue.TIMESTAMP)
        }
    }

    fun recuperarMinhasVagas(
        activity: Activity,
        progressBar: ProgressBar,
        vagasRecuperadas: (vagas: List<Vaga>) -> Unit
    ) {
        val vagasLst = mutableListOf<Vaga>()

        UserDao().getIdUser(activity)?.let { idUser ->
            val minhasVagasRef = FirebaseDatabase.getInstance().reference
                .child("empregadores")
                .child(idUser)

            minhasVagasRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        snapshot.children.forEach {
                            it.getValue(Vaga::class.java)?.let { vaga ->
                                vagasLst.add(vaga)
                            }
                        }
                        vagasRecuperadas(vagasLst.toList())
                    } else {
                        vagasRecuperadas(emptyList())
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }
        progressBar.visibility = View.GONE

    }

    fun recuperarVaga(
        activity: Activity,
        idVaga: String,
        progressBar: ProgressBar? = null,
        vagasRecuperada: (vaga: Vaga?) -> Unit
    ) {
        UserDao().getIdUser(activity)?.let {
            FirebaseDatabase.getInstance().reference
                .child("vagas")
                .child(idVaga)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            vagasRecuperada(snapshot.getValue(Vaga::class.java))

                            progressBar?.let {
                                it.visibility = View.GONE
                            }

                        } else {
                            Toast.makeText(
                                activity.baseContext,
                                "Vaga não encontrada",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                })
        }
    }

    fun recuperarMinhaVaga(
        activity: Activity,
        idVaga: String,
        idUser: String,
        progressBar: ProgressBar? = null,
        vagasRecuperada: (vaga: Vaga?) -> Unit
    ) {

        FirebaseDatabase.getInstance().reference
            .child("empregadores")
            .child(idUser)
            .child(idVaga)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        vagasRecuperada(snapshot.getValue(Vaga::class.java))
                        progressBar?.let {
                            it.visibility = View.GONE
                        }

                    } else {
                        Toast.makeText(
                            activity.baseContext,
                            "Vaga não encontrada",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }


    fun recuperarVagas(activity: Activity, vagasRecuperada: (vagaLst: List<Vaga>?) -> Unit) {

        val vagas = mutableListOf<Vaga>()
        FirebaseDatabase.getInstance().reference
            .child("vagas")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        snapshot.children.forEach {
                            it.getValue(Vaga::class.java)?.let { vaga ->
                                vagas.add(vaga)
                            }
                        }
                        vagasRecuperada(vagas)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }

    fun apagarVaga(activity: Activity, vagaId: String) {
        UserDao().getIdUser(activity)?.let { idUser ->

            FirebaseDatabase.getInstance().reference
                .child("empregadores")
                .child(idUser)
                .child(vagaId).removeValue()

            FirebaseDatabase.getInstance().reference
                .child("vagas")
                .child(vagaId).removeValue()

            FirebaseStorage.getInstance().reference
                .child("imagens")
                .child("vagas")
                .child(idUser)
                .child(vagaId)
                .child("vaga.jpeg").delete()

            activity.finish()
        }
    }
}