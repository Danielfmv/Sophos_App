package com.example.sophos.UI.Documents

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.sophos.R
import com.example.sophos.databinding.FragmentSendDocumentsBinding
import java.util.*

class SendDocumentsFragment : Fragment() {

    private var _bindging: FragmentSendDocumentsBinding? = null
    private val binding: FragmentSendDocumentsBinding
        get() = _bindging!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _bindging = FragmentSendDocumentsBinding.inflate(inflater, container, false)

        val typeOf_doc = resources.getStringArray(R.array.typeOf_doc)

        val spinnerAdapter1 =
            object : ArrayAdapter<String>(requireContext(), R.layout.item_dropdown, typeOf_doc) {

                override fun isEnabled(position: Int): Boolean {
                    // Disable the first item from Spinner
                    // First item will be used for hint
                    return position != 0
                }

                override fun getDropDownView(
                    position: Int,
                    convertView: View?,
                    parent: ViewGroup
                ): View {
                    val view: TextView =
                        super.getDropDownView(position, convertView, parent) as TextView
                    //set the color of first item in the drop down list to gray
                    if (position == 0) {
                        view.setTextColor(Color.RED)
                    }
                    return view
                }

            }

        return binding.root
    }
}
