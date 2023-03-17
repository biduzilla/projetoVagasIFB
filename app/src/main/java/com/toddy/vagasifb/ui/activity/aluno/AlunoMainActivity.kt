package com.toddy.vagasifb.ui.activity.aluno

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.toddy.vagasifb.R
import com.toddy.vagasifb.databinding.ActivityAlunoMainBinding

class AlunoMainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityAlunoMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        NavigationUI.setupWithNavController(binding.bottomNavView, navController)
    }
}