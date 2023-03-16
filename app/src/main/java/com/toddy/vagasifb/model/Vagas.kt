package com.toddy.vagasifb.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Vaga(
    val id: String,
    val cargo: String,
    val empresa: String,
    val descricao: String,
    val horario: String,
    val dataPostada: Long,
    val requisitos: List<String>,
    val imagem: String
) : Parcelable
