package com.toddy.vagasifb.model

import com.google.firebase.database.Exclude

data class User(val id: String, val email: String, @get:Exclude val senha: String)
