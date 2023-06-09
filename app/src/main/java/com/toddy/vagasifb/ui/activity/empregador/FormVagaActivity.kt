package com.toddy.vagasifb.ui.activity.empregador

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.database.FirebaseDatabase
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.toddy.vagasifb.R
import com.toddy.vagasifb.database.UserDao
import com.toddy.vagasifb.database.VagaDao
import com.toddy.vagasifb.databinding.ActivityFormVagaBinding
import com.toddy.vagasifb.databinding.BottomSheetFormVagaBinding
import com.toddy.vagasifb.extensions.tentaCarregarImagem
import com.toddy.vagasifb.model.Vaga
import com.toddy.vagasifb.ui.activity.ABRIR_CAMERA
import com.toddy.vagasifb.ui.activity.ABRIR_GALERIA
import com.toddy.vagasifb.ui.activity.CHAVE_VAGA
import com.toddy.vagasifb.ui.activity.CHAVE_VAGA_ID
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class FormVagaActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityFormVagaBinding.inflate(layoutInflater)
    }

    private lateinit var currentPhotoPath: String
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private var resultCode: String = ""
    private var vaga: Vaga? = null
    private var caminhoImagem: String? = null
    private var isUpdate: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.toolbarVoltar.tvTitulo.text = "Cadastrar Vaga"
        startResult()
        configClicks()
        updateVaga()
    }

    private fun updateVaga() {
        intent.getStringExtra(CHAVE_VAGA_ID)?.let { idVaga ->
            isUpdate = true
            binding.toolbarVoltar.tvTitulo.text = "Atualizar Vaga"
            binding.btnSalvar.text = "Atualizar"

            UserDao().getIdUser(this)?.let { idUser ->
                VagaDao().recuperarMinhaVaga(this, idVaga, idUser) { vagaRecuperada ->
                    vagaRecuperada?.let {
                        vaga = it
                        preencheDados(it)
                    }
                }
            }

        }
        vaga?.let {
            isUpdate = true
        }
    }

    private fun preencheDados(vaga: Vaga) {
        with(binding) {
            edtCargo.setText(vaga.cargo)
            edtEmpresa.setText(vaga.empresa)
            edtDescricao.setText(vaga.descricao)
            edtHorario.setText(vaga.horario)
            edtRequisitos.setText(vaga.requisitos.toString().replace("[", "").replace("]", ""))
            imgAdd.visibility = View.GONE
            imgVagaImg.tentaCarregarImagem(vaga.imagem!!)
        }
    }

    private fun configClicks() {
        with(binding) {
            toolbarVoltar.btnVoltar.setOnClickListener {
                finish()
            }
            btnSalvar.setOnClickListener {
                validaDados()
            }

            btnAddImg.setOnClickListener {
                showBottonSheet()
            }
        }
    }

    private fun validaDados() {
        Log.i("infoteste", isUpdate.toString())
        with(binding) {
            val cargo = edtCargo.text.toString().trim()
            val empresa = edtEmpresa.text.toString().trim()
            val descricao = edtDescricao.text.toString().trim()
            val horario = edtHorario.text.toString().trim()
            val requisitos = edtRequisitos.text.toString().trim()

            when {
                cargo.isEmpty() -> {
                    edtCargo.requestFocus()
                    edtCargo.error = "Campo Obrigatório"
                }
                empresa.isEmpty() -> {
                    edtEmpresa.requestFocus()
                    edtEmpresa.error = "Campo Obrigatório"
                }
                descricao.isEmpty() -> {
                    edtDescricao.requestFocus()
                    edtDescricao.error = "Campo Obrigatório"
                }
                horario.isEmpty() -> {
                    edtHorario.requestFocus()
                    edtHorario.error = "Campo Obrigatório"
                }
                requisitos.isEmpty() -> {
                    edtRequisitos.requestFocus()
                    edtRequisitos.error = "Campo Obrigatório"
                }
                !requisitos.contains(".") -> {
                    edtRequisitos.requestFocus()
                    edtRequisitos.error = "Coloque '.' no final de cada requisito"
                }

                else -> {
                    btnSalvar.visibility = View.GONE
                    progressBar.visibility = View.VISIBLE

                    ocultarTeclado()

                    val requisitosLst = requisitos.split(".").toMutableList()

                    requisitosLst.forEach {
                        if (it.isEmpty()) {
                            requisitosLst.remove(it)
                        }
                    }

                    if (!isUpdate) {

                        vaga = Vaga(
                            "",
                            UserDao().getIdUser(this@FormVagaActivity),
                            cargo,
                            empresa,
                            descricao,
                            horario,
                            0L,
                            requisitosLst.toList(),
                            emptyList<String>().toMutableList(),
                            ""
                        )
                    } else {
                        Log.i("infoteste", "ok")
                        vaga!!.cargo = cargo
                        vaga!!.empresa = empresa
                        vaga!!.descricao = descricao
                        vaga!!.horario = horario
                        vaga!!.requisitos = requisitosLst.toList()
                    }
                    Log.i("infoteste", "vaga = $vaga")
                    if (vaga!!.id!!.isNotEmpty()) {
                        if (caminhoImagem != null) {
                            salvarImagemFirebase(vaga!!.id!!)
                        } else {
                            UserDao().getIdUser(this@FormVagaActivity)?.let { idUserRecuperado ->
                                VagaDao().salvarVagaUser(
                                    vaga!!,
                                    this@FormVagaActivity,
                                    !isUpdate, false, idUserRecuperado
                                )
                            }
                        }
                    } else {
                        if (caminhoImagem != null) {
                            salvarVaga(vaga!!)
                        } else {
                            btnSalvar.visibility = View.VISIBLE
                            progressBar.visibility = View.GONE

                            Toast.makeText(
                                this@FormVagaActivity,
                                "Imagem da vaga obrigatória",
                                Toast.LENGTH_SHORT
                            ).show()

                        }
                    }

                }
            }
        }
    }

    private fun ocultarTeclado() {
        val imm =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.edtCargo.windowToken, 0)
    }

    private fun salvarVaga(vaga: Vaga) {
        FirebaseDatabase.getInstance().reference.apply {
            push().key?.let { id ->
                vaga.id = id
                salvarImagemFirebase(vaga.id!!)
            }
        }
    }

    private fun salvarImagemFirebase(idVaga: String) {
        caminhoImagem?.let {
            VagaDao().salvarImagemVagaFirebase(it, idVaga, this) { url ->
                url?.let {
                    vaga!!.imagem = url

                    UserDao().getIdUser(this@FormVagaActivity)?.let { idUserRecuperado ->
                        VagaDao().salvarVagaUser(
                            vaga!!,
                            this@FormVagaActivity,
                            isNovo = !isUpdate,
                            isSalvarCv = false,
                            idUserRecuperado
                        )
                    }
                }
            }
        }
    }

    private fun showBottonSheet() {
        val sheetBinding = BottomSheetFormVagaBinding.inflate(layoutInflater)
        BottomSheetDialog(this, R.style.BottomSheetDialog).apply {
            setContentView(sheetBinding.root)
            show()

            sheetBinding.btnCamera.setOnClickListener {
                resultCode = ABRIR_CAMERA
                verificarPermissaoCamera()
                dismiss()
            }
            sheetBinding.btnGaleria.setOnClickListener {
                resultCode = ABRIR_GALERIA
                verificarPermissaoGaleria()
                dismiss()
            }
            sheetBinding.btnCancelar.setOnClickListener {
                dismiss()
            }

        }
    }

    private fun verificarPermissaoGaleria() {
        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply {
            resultLauncher.launch(this)
        }
    }

    private fun verificarPermissaoCamera() {
        val permissionListener: PermissionListener = object : PermissionListener {
            override fun onPermissionGranted() {
                abrirCamera()
            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                Toast.makeText(baseContext, "Permissão Negada", Toast.LENGTH_SHORT).show()
            }
        }
        showDialogPermissao(
            permissionListener,
            "Se você não aceitar a permissão não poderá acessar a camera do dispositivo, deseja aceitar a permissão?",
            listOf(android.Manifest.permission.CAMERA)
        )
    }

    private fun showDialogPermissao(
        permissionListener: PermissionListener,
        msg: String,
        perm: List<String>
    ) {
        TedPermission.create()
            .setPermissionListener(permissionListener)
            .setDeniedTitle("Permissão negada")
            .setDeniedMessage(msg)
            .setDeniedCloseButtonText("Não")
            .setGotoSettingButtonText("Sim")
            .setPermissions(*perm.toTypedArray())
            .check()
    }

    private fun abrirCamera() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        "com.toddy.vagasifb.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    resultLauncher.launch(takePictureIntent)
                }
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    private fun startResult() {
        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == Activity.RESULT_OK) {

                    when (resultCode) {
                        ABRIR_CAMERA -> {
                            val file = File(currentPhotoPath)
                            binding.imgAdd.visibility = View.GONE
                            binding.imgVagaImg.setImageURI(Uri.fromFile(file))
                            caminhoImagem = file.toURI().toString()
                        }

                        ABRIR_GALERIA -> {

                            val imagemSelecionada: Uri = it.data!!.data!!
                            caminhoImagem = imagemSelecionada.toString()

                            val bitmap: Bitmap = if (Build.VERSION.SDK_INT < 28) {
                                MediaStore.Images.Media.getBitmap(
                                    contentResolver,
                                    imagemSelecionada
                                )
                            } else {
                                val source: ImageDecoder.Source =
                                    ImageDecoder.createSource(
                                        contentResolver,
                                        imagemSelecionada
                                    )
                                ImageDecoder.decodeBitmap(source)
                            }
                            binding.imgAdd.visibility = View.GONE
                            binding.imgVagaImg.setImageBitmap(bitmap)
                        }

                        else -> Toast.makeText(
                            this,
                            "Error carregar imagem selecionada",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
    }
}