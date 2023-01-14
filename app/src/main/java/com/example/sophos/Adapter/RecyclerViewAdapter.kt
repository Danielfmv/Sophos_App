package com.example.sophos.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sophos.Data.Model.getDocsItems
import com.example.sophos.Data.Model.getDocsResponse
import com.example.sophos.R
import com.example.sophos.databinding.GetdocsItemlistBinding

class RecyclerViewAdapter (private val onClickListener : (regID: String) -> Unit )
    : RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder>() {

    var documentList = mutableListOf<getDocsItems>()

    class RecyclerViewHolder (view : View) : RecyclerView.ViewHolder(view) {
        var binding = GetdocsItemlistBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.getdocs_itemlist, parent, false)
        return RecyclerViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        val document = documentList[position]
        holder.binding.textViewName.text = "${document.name} ${document.lastName}"
        holder.binding.textViewDate.text = document.docsDate.substring(0, document.docsDate.indexOf("T"))
        holder.binding.textViewAttType.text = document.attachedType

        holder.binding.recyclerItem.setOnClickListener{
            onClickListener(document.regId)
        }
    }

    override fun getItemCount(): Int {
        return this.documentList.size
    }

    fun loadDocuments(documents: MutableList<getDocsItems>){
        this.documentList = documents
        notifyDataSetChanged()
    }
}