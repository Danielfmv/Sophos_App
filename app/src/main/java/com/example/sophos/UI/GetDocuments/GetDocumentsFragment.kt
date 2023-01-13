package com.example.sophos.UI.GetDocuments

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sophos.Adapter.RecyclerViewAdapter
import com.example.sophos.Data.Model.getDocsItems
import com.example.sophos.Data.Model.getDocsResponse
import com.example.sophos.R
import com.example.sophos.ViewModels.LoginViewModel
import com.example.sophos.ViewModels.getDocsViewModel
import com.example.sophos.databinding.FragmentGetDocumentsBinding
import java.io.ByteArrayInputStream
import java.io.FileOutputStream
import java.util.Base64
import java.util.Base64.getDecoder


class GetDocumentsFragment : Fragment() {

    private val getDocumentsViewModel : getDocsViewModel by viewModels()
    private val loginViewModel : LoginViewModel by activityViewModels()

    private lateinit var recyclerView : RecyclerView
//    private lateinit var recyclerViewAdapter : RecyclerViewAdapter

    private var _binding: FragmentGetDocumentsBinding? = null
    private val binding: FragmentGetDocumentsBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentGetDocumentsBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getDocumentsViewModel.getDocs(loginViewModel.userEmail)

        recyclerView = binding.getDocsRecycler

        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val recyclerAdapter = RecyclerViewAdapter(this::onRecyclerViewItemClick)

        recyclerView.adapter = recyclerAdapter

        getDocumentsViewModel.getDocsLiveData.observe(viewLifecycleOwner) {
            if (it != null) {
                recyclerAdapter.loadDocuments(it.Items as MutableList<getDocsItems>)
            }
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun onRecyclerViewItemClick(regID: String){
        getDocumentsViewModel.getImg(regID)
        getDocumentsViewModel.getDocsLiveData.observe(viewLifecycleOwner) {
            if (it != null) {
                val base64URLsafe = getDocumentsViewModel.base64String.replace("data:image/jpg;base64,", "")
                println(base64URLsafe)
                decodeBase64ToImg(base64URLsafe, binding.documentPhoto)
            }
        }
        //binding.documentPhoto.setImageBitmap()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun decodeBase64ToImg (base64 : String, image : ImageView) {
        val imageBytes = android.util.Base64.decode(base64, android.util.Base64.DEFAULT)
        val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        image.setImageBitmap(decodedImage)
    }

}

