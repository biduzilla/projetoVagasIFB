package com.toddy.vagasifb.utils

import android.app.Activity
import android.content.Context
import com.toddy.vagasifb.ui.activity.CHAVE_LOGIN_SHARED_PREF
import com.toddy.vagasifb.ui.activity.CHAVE_USER_EMAIL
import com.toddy.vagasifb.ui.activity.CHAVE_USER_SENHA

class SharedPref(activity: Activity) {
    private val sharedPreferences =
        activity.getSharedPreferences(CHAVE_LOGIN_SHARED_PREF, Context.MODE_PRIVATE)

    fun salvarDadosLogin(email: String, senha: String) {
        sharedPreferences.edit()
            .putString(CHAVE_USER_EMAIL, email)
            .putString(CHAVE_USER_SENHA, senha)
            .apply()
    }

    fun getDadosLogin(): List<String> {
        val dados = mutableListOf<String>()
        with(sharedPreferences) {
            val email = getString(CHAVE_USER_EMAIL, "")
            val senha = getString(CHAVE_USER_SENHA, "")

            dados.add(email!!)
            dados.add(senha!!)
            return dados.toList()
        }
    }
}