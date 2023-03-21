package com.toddy.vagasifb.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Vaga(
    var id: String? = "",
    var idDono: String? = "",
    var cargo: String? = "",
    var empresa: String? = "",
    var descricao: String? = "",
    var horario: String? = "",
    var dataPostada: Long? = 0L,
    var requisitos: List<String>? = null,
    var candidaturas: MutableList<String> = mutableListOf(),
    var imagem: String? = ""
) : Parcelable

