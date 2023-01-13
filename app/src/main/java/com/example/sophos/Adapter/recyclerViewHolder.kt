package com.example.sophos.Adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.sophos.Data.Model.getDocsItems
import com.example.sophos.databinding.GetdocsItemlistBinding

class recyclerViewHolder (view : View) : RecyclerView.ViewHolder(view) {

    // PASO 3
    private var binding = GetdocsItemlistBinding.bind(itemView)

    fun render (docs : getDocsItems, onClickListener : (getDocsItems) -> Unit) {
        binding.textViewDate.text = docs.name
        binding.textViewAttType.text = docs.attachedType
        binding.textViewName.text = docs.name

        itemView.setOnClickListener {
            onClickListener(docs)
        }
    }

}