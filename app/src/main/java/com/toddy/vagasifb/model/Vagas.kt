package com.toddy.vagasifb.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Vaga(
    var id: String,
    var cargo: String,
    var empresa: String,
    var descricao: String,
    var horario: String,
    var dataPostada: Long,
    var requisitos: List<String>,
    var imagem: String
) : Parcelable

