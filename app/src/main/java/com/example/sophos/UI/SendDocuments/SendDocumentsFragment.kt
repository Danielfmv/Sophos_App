package com.example.sophos.UI.SendDocuments

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.PopupMenu
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.sophos.Data.Model.OfficesItems
import com.example.sophos.Data.Model.SendDocsData
import com.example.sophos.R
import com.example.sophos.ViewModels.LoginViewModel
import com.example.sophos.ViewModels.SendDocsViewModel
import com.example.sophos.databinding.FragmentSendDocumentsBinding
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.util.*


@Suppress("UNREACHABLE_CODE")
class SendDocumentsFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private val sendDocumentsViewModel : SendDocsViewModel by viewModels()
    private val loginViewModel : LoginViewModel by activityViewModels()

    private var cityList : MutableList<OfficesItems> = mutableListOf()
    private lateinit var arrayAdapterCities : ArrayAdapter<String>
    private lateinit var arrayAdapterDocs : ArrayAdapter<String>

    private var base64String : String = ""
    //private var selectedItemsDocs : String = ""


    // VARIABLE PARA FECHA DE ENVIO //

    private var currentDate = Calendar.getInstance()
    private val dateFormat = "dd-MM-yyyy"
    @RequiresApi(Build.VERSION_CODES.N)
    private var sendingDate = SimpleDateFormat(dateFormat).format(currentDate.time)


    private var _bindging: FragmentSendDocumentsBinding? = null
    private val binding: FragmentSendDocumentsBinding
        get() = _bindging!!

    private val filesAccessRequestCode = 1001
    private val requestCameraCode = 1002
    private var cameraP??c : Uri? = null



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _bindging = FragmentSendDocumentsBinding.inflate(inflater, container, false)
        return binding.root
    }


    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("ResourceType")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ----------- MUESTRA EL MEN?? POPUP PARA TOMAR O CARGAR FOTOS --------------------------//

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

    // -------------------- CARGAR FOTO DESDE BOT??N ------------------------- //

        binding.uploadDocBttn.setOnClickListener {
            galleryPermission()
        }


    // ------------------------------- POST en API ---------------------------------- //

        sendDocumentsViewModel.sendDocsLiveData.observe(viewLifecycleOwner) {
            sendDocumentsViewModel.sendDocuments(infoToPost())
        }

        binding.sednDocsBttn.setOnClickListener {
            if (textChecked()) {
                val infoToSent = infoToPost()
                sendDocumentsViewModel.sendDocuments(infoToSent)
                Toast.makeText(requireContext(), "Tus datos fueron enviados", Toast.LENGTH_SHORT).show()
                reloadFragment()
            }
        }

        binding.burguerMenuSendDocs.setOnClickListener {
            val popupMenu = PopupMenu(requireContext(), binding.burguerMenuSendDocs)
            popupMenu.menuInflater.inflate(R.menu.ontop_menu, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.topMenuSendDocs ->
                        Toast.makeText(requireContext(), "Ya estas en la vista de enviar documentos", Toast.LENGTH_SHORT).show()
                    R.id.topMenuViewDocs ->
                        findNavController().navigate(R.id.action_sendDocumentsFragment_to_getDocumentsFragment)
                    R.id.topMenuOffices ->
                        findNavController().navigate(R.id.action_sendDocumentsFragment_to_officesFragment)
                    R.id.topMenuLogout ->
                        findNavController().navigate(R.id.loginFragment)
                }
                true
            }
            popupMenu.show()
        }

        binding.backBttn.setOnClickListener {
            findNavController().navigate(R.id.action_sendDocumentsFragment_to_menu_Screen)
        }

    }

    // ----------------  FUNCI??N PARA EVALUAR QUE CAMPOS DE TEXTO Y SPINNERS NO EST??N VACIOS ------------------ //

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
        return true

        if (binding.docUserEmail.toString() != loginViewModel.userEmail) {
            Toast.makeText(requireContext(), "El correo ingresado no corresponde con nuestra base de datos", Toast.LENGTH_SHORT).show()
        } else {
            println(loginViewModel.userEmail)
        }
    }

    // ----------------  FUNCI??N DE ASIGNACI??N DE VALORES PARA REALIZAR EL POST ------------------ //

    @RequiresApi(Build.VERSION_CODES.N)
    private fun infoToPost () : SendDocsData {
        return SendDocsData(
            sendingDate,
            idType = binding.spinnerDocs.selectedItem.toString(),
            idNum = binding.docUserNumber.text.toString(),
            name = binding.docUserName.text.toString(),
            lastName = binding.docUserLastName.text.toString(),
            city = binding.spinnerCities.selectedItem.toString(),
            email = binding.docUserEmail.text.toString(),
            attachedType = binding.tipoAdjunto.text.toString(),
            attached = base64String
        )
    }

    // ----------------  PERMISOS Y ACC??SO A C??MARA DEL TEL??FONO ------------------ //

    //1
    private fun openCamara () {
//        val value = ContentValues() //crea un espacio de memoria vacio
//        value.put(MediaStore.Images.Media.TITLE, "Nueva Imagen")
//        cameraP??c =
//            context?.contentResolver?.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, value)
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        //cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraP??c)
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

    // NOTA Paso 3: paso 3 sigue en funci??n "onRequestPermissionsResult" abajo
    // NOTA Paso 4 : paso 4 sigue en funci??n "onActivityResult" abajo


    // -----------------------  PERMISOS Y ACC??SO A GALER??A DEL TEL??FONO ----------------------- //

    //1.
    private fun openGallery () {
        val intentGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        // intentGallery.type = "image/*"
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
            // Para Galer??a
            filesAccessRequestCode -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    openGallery()
                else {
                    Toast.makeText(
                        requireContext(),
                        "Permiso de acceso a la galer??a denegado",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            // 3. Para C??mara
            requestCameraCode -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    openCamara()
                else {
                    Toast.makeText(
                        requireContext(),
                        "Permiso de acceso a la c??mara denegado",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    //4 SE CARGA LA IMAGEN EN EL IMAGEVIEW CREADO (EL ??CONO DE C??MARA)
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Para Galer??a
        if (resultCode == Activity.RESULT_OK && requestCode == filesAccessRequestCode) {
            binding.imgViewClick.setImageURI(data?.data)

        // --------------- LLAMADO FUNCIONES PARA CONVERSI??N DE IMAGENES A BASE64 ---------------//
            val selectedImageUri = data?.data
            val inputStream: InputStream? = this.context?.contentResolver?.openInputStream(selectedImageUri!!)
            val imageBitmap = BitmapFactory.decodeStream(inputStream)
            base64String = convertBitmapToBase64(imageBitmap)

        }
        // 4. Para C??mara
        if (resultCode == Activity.RESULT_OK && requestCode == requestCameraCode) {
            binding.imgViewClick.setImageURI(cameraP??c)

        // --------------- LLAMADO FUNCIONES PARA CONVERSI??N DE IMAGENES A BASE64 ---------------//
            val imageBitmap = data?.extras?.get("data") as Bitmap
            binding.imgViewClick.setImageBitmap(imageBitmap)
            base64String = convertBitmapToBase64(imageBitmap)

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
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream)
        val image = stream.toByteArray()
        return android.util.Base64.encodeToString(image, android.util.Base64.DEFAULT)
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        TODO("Not yet implemented")
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    // ----------------- RECARGAR FRAGMENTO AL ENV??O DE INFORMACI??N --------------------- //

    private fun reloadFragment() {
        findNavController().navigate(R.id.sendDocumentsFragment)
    }
}




