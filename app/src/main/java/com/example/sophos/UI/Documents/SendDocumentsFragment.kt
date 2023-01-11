package com.example.sophos.UI.Documents

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64.encodeToString
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.PopupMenu
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.collection.ArraySet
import androidx.fragment.app.viewModels
import com.example.sophos.Data.Model.SendDocsData
import com.example.sophos.Data.Model.SendDocsResponse
import com.example.sophos.R
import com.example.sophos.ViewModels.OfficesViewModel
import com.example.sophos.ViewModels.SendDocsViewModel
import com.example.sophos.databinding.FragmentSendDocumentsBinding
import java.util.*
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.OnRequestPermissionsResultCallback
import androidx.core.content.ContextCompat
import androidx.core.content.contentValuesOf
import androidx.lifecycle.viewModelScope
import com.example.sophos.Data.Model.OfficesItems
import com.example.sophos.Data.Network.API.SendDocsAPI
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

class SendDocumentsFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private val sendDocumentsViewModel : SendDocsViewModel by viewModels()

    private var cityList : MutableList<OfficesItems> = mutableListOf()
    private lateinit var arrayAdapterCities : ArrayAdapter<String>
    private lateinit var arrayAdapterDocs : ArrayAdapter<String>

    private var base64String : String = ""
    private var selectedItemsDocs : String = ""

    private var _bindging: FragmentSendDocumentsBinding? = null
    private val binding: FragmentSendDocumentsBinding
        get() = _bindging!!

    private val filesAccessRequestCode = 1001
    private val requestCameraCode = 1002
    var cameraPíc : Uri? = null



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _bindging = FragmentSendDocumentsBinding.inflate(inflater, container, false)
        return binding.root
    }


    @SuppressLint("ResourceType")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ----------- MUESTRA EL MENÚ POPUP PARA TOMAR O CARGAR FOTOS --------------------------//

        binding.imgViewClick.setOnClickListener {
            val popupMenu = PopupMenu(requireContext(), binding.imgViewClick)
            popupMenu.menuInflater.inflate(R.menu.popup_menu, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    //CAMERA
                    R.id.takePic ->
                        cameraPermission()
                    // GALLERY
                    R.id.galleryPic ->
                        galleryPermission()
                }
                true
            }
            popupMenu.show()
        }

    // --------------  SPINNER DE CIUDADES OBTENIDAS DESDE LA API  ------------------- //

        arrayAdapterCities = ArrayAdapter<String>(requireContext(), R.layout.item_dropdown)

        sendDocumentsViewModel.officesCitiesLiveData.observe(viewLifecycleOwner) {
            val city = "Ciudad"
            arrayAdapterCities.addAll(
                listOf(
                    city,
                    sendDocumentsViewModel.officesCitiesLiveData.value?.get(0).toString(),
                    sendDocumentsViewModel.officesCitiesLiveData.value?.get(1).toString(),
                    sendDocumentsViewModel.officesCitiesLiveData.value?.get(2).toString(),
                    sendDocumentsViewModel.officesCitiesLiveData.value?.get(3).toString(),
                    sendDocumentsViewModel.officesCitiesLiveData.value?.get(4).toString(),
                    sendDocumentsViewModel.officesCitiesLiveData.value?.get(5).toString()
                )
            )
        }

        // -------------------- ITEM SELECCIONADO DEL SPINNER DE CIUDADES ----------------------- //

        binding.spinnerCities.adapter = arrayAdapterCities
        binding.spinnerCities.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val selectedItemCities = p0?.getItemAtPosition(p2) as String
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }

        // -------------------- ITEM SELECCIONADO DEL SPINNER DE DOCUMENTOS --------------------- //

//        val spinnerDocsList = arrayOf("Tipo de documento", "CC", "TI", "PA", "CE")
//
//        arrayAdapterDocs = ArrayAdapter<String>(requireContext(), R.layout.item_dropdown, spinnerDocsList)
//
//        arrayAdapterDocs.setDropDownViewResource(R.layout.item_dropdown)
//
//        binding.spinnerDocs.adapter = arrayAdapterDocs
//
//        binding.spinnerDocs.onItemSelectedListener = this



    // -------------------- CARGAR FOTO EN FORMATO BASE64 ------------------------- //

        binding.uploadDocBttn.setOnClickListener {
            val bitmap = convertImgToBitmap()
            binding.imgViewClick.setImageBitmap(bitmap)
            base64String = convertBitmapToBase64(bitmap)
        }


    // ------------------------------- POST en API ---------------------------------- //

        sendDocumentsViewModel.sendDocsLiveData.observe(viewLifecycleOwner) {
            sendDocumentsViewModel.sendDocuments(infoToPost())
        }

        binding.sednDocsBttn.setOnClickListener {
            if (textChecked()) {
                //sendDocumentsViewModel.sendDocuments(infoToPost())
                Toast.makeText(requireContext(), "Tus datos fueron enviados", Toast.LENGTH_SHORT).show()
                println(infoToPost())
                reloadFragment()
            }
        }
    }

    // ----------------  FUNCIÓN PARA EVALUAR QUE CAMPOS DE TEXTO Y SPINNERS NO ESTÉN VACIOS ------------------ //

    private fun textChecked () : Boolean {
        val fields = arrayOf(
            binding.docUserNumber,
            binding.docUserName,
            binding.docUserLastName,
            binding.docUserEmail,
            binding.tipoAdjunto)

        for (i in fields.indices) {
            val field = fields[i]
            if (field.length() == 0) {
                Toast.makeText(requireContext(), "Todos los campos son requeridos", Toast.LENGTH_SHORT).show()
                return false
            }
        }

        if (binding.spinnerDocs.selectedItemPosition == 0){
            Toast.makeText(requireContext(), "Debes seleccionar un tipo de documento", Toast.LENGTH_SHORT).show()
            return false
        } else {
            if (binding.spinnerCities.selectedItemPosition == 0) {
                Toast.makeText(requireContext(), "Debes seleccionar una ciudad", Toast.LENGTH_SHORT).show()
                return false
            }
        }

        if (binding.imgViewClick.drawable == null) {
            Toast.makeText(requireContext(), "Debes seleccionar una imagen", Toast.LENGTH_SHORT).show()
            println(binding.imgViewClick.drawable)
            return false
        }
        return true
    }

    // ----------------  FUNCIÓN DE ASIGNACIÓN DE VALORES PARA REALIZAR EL POST ------------------ //

    private fun infoToPost () : SendDocsData {
        return SendDocsData(
            idType = binding.spinnerDocs.toString(),
            idNum = binding.docUserNumber.toString(),
            name = binding.docUserName.toString(),
            lastName = binding.docUserLastName.toString(),
            city = binding.spinnerCities.selectedItem.toString(),
            email = binding.docUserEmail.toString(),
            attachedType = binding.tipoAdjunto.toString(),
            attached = base64String
        )
    }

    // ----------------  PERMISOS Y ACCÉSO A CÁMARA DEL TELÉFONO ------------------ //

    //1
    private fun openCamara () {
        val value = ContentValues() //crea un espacio de memoria vacio
        value.put(MediaStore.Images.Media.TITLE, "Nueva Imagen")
        cameraPíc =
            context?.contentResolver?.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, value)
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraPíc)
        startActivityForResult(cameraIntent, requestCameraCode)
    }

    //2
    private fun cameraPermission () {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED
            || ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            val cameraPermission = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            requestPermissions(cameraPermission, requestCameraCode)
        } else {
            openCamara()
        }
    }

    // NOTA Paso 3: paso 3 sigue en función "onRequestPermissionsResult" abajo
    // NOTA Paso 4 : paso 4 sigue en función "onActivityResult" abajo


    // -----------------------  PERMISOS Y ACCÉSO A GALERÍA DEL TELÉFONO ----------------------- //

    //1.
    private fun openGallery () {
        val intentGallery = Intent(Intent.ACTION_PICK)
        intentGallery.type = "image/*"
        startActivityForResult(intentGallery, filesAccessRequestCode)
    }

    //2
    private fun galleryPermission () {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            val filesPermission = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
            requestPermissions(filesPermission, filesAccessRequestCode)
        } else {
            openGallery()
        }
    }

    //3
    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            // Para Galería
            filesAccessRequestCode -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    openGallery()
                else {
                    Toast.makeText(
                        requireContext(),
                        "Permiso de acceso a la galería denegado",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            // 3. Para Cámara
            requestCameraCode -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    openCamara()
                else {
                    Toast.makeText(
                        requireContext(),
                        "Permiso de acceso a la cámara denegado",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    //4
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Para Galería
        if (resultCode == Activity.RESULT_OK && requestCode == filesAccessRequestCode) {
            binding.imgViewClick.setImageURI(data?.data)
        }
        // 4. Para Cámara
        if (resultCode == Activity.RESULT_OK && requestCode == requestCameraCode) {
            binding.imgViewClick.setImageURI(cameraPíc)
        }
    }

    // ------------------  CONVERTIR IMAGEN A BASE 64  ------------------------- //

    //5
    private fun convertImgToBitmap () : Bitmap {
        val drawable = binding.imgViewClick.drawable as BitmapDrawable
        return drawable.bitmap
    }

    //6
    private fun convertBitmapToBase64 (bitmap : Bitmap) : String {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        val image = stream.toByteArray()
        return android.util.Base64.encodeToString(image, android.util.Base64.DEFAULT)
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        TODO("Not yet implemented")
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    private fun reloadFragment() {
        val currentFragment = this
        val fragmentManager = currentFragment.parentFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val newFragmentInstance = currentFragment::class.java.newInstance()
        fragmentTransaction.replace(R.id.fragmentContainerView, newFragmentInstance)
        fragmentTransaction.commit()
        }
}

//    val fragment = this
//    val fragmentManager = fragment.parentFragmentManager
//    val fragmentTransaction = fragmentManager.beginTransaction()
//    fragmentTransaction.detach(fragment)
//    fragmentTransaction.attach(fragment)
//    fragmentTransaction.commit()



