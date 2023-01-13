package com.example.sophos.UI.GetDocuments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    private fun onRecyclerViewItemClick(regID: String){
        println(regID.toString())
        //binding.documentPhoto.setImageBitmap()
    }

}

