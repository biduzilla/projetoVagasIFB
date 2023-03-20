package com.toddy.vagasifb.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Curriculo(
    var id: String? = null,
    var nome: String? = null,
    var telefone: String? = null,
    var sobre: String? = null,
    var semestre: String? = null,
    var experiencias: List<String>? = null,
    var qualificacoes: List<String>? = null,
    var historico: List<String>? = null,
) : Parcelable
