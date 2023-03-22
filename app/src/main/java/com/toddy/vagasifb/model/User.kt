package com.toddy.vagasifb.model

import com.google.firebase.database.Exclude

data class User(
    var id: String? = null,
    var email: String? = null,
    var telefone: String? = null,
    @get:Exclude var senha: String = ""
) {
}
