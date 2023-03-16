package com.toddy.vagasifb.extensions

import android.widget.ImageView
import coil.load
import com.toddy.vagasifb.R

fun ImageView.tentaCarregarImagem(url: String) {

    load(url) {
        placeholder(R.drawable.placeholder)
    }
}