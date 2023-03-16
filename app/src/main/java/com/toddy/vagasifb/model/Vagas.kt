package com.toddy.vagasifb.model

data class Vaga(
    val id: String,
    val cargo: String,
    val empresa:String,
    val descricao: String,
    val horario: String,
    val dataPostada: Long,
    val requisitos: List<String>,
    val imagem:String
)
