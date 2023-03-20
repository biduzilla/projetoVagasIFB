package com.toddy.vagasifb.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Curriculo(
    val nome: String? = null,
    val telefone: String? = null,
    val sobre: String? = null,
    val semestre: String? = null,
    val experiencias: List<String>? = null,
    val qualificacoes: List<String>? = null,
) : Parcelable
