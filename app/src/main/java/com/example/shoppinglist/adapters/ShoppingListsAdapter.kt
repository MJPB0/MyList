package com.example.shoppinglist.adapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.R
import com.example.shoppinglist.data.database.entities.ShoppingList
import com.example.shoppinglist.view_models.Communicator
import com.example.shoppinglist.view_models.ShoppingListsViewModel
import java.util.*

class ShoppingListsAdapter (
    private val shoppingLists: LiveData<List<ShoppingList>>,
    private val viewModel: ShoppingListsViewModel,
    private val communicator: Communicator
) : RecyclerView.Adapter<ShoppingListsAdapter.ShoppingListsHolder>() {
    inner class ShoppingListsHolder(private val view: View): RecyclerView.ViewHolder(view)
    {
        val listName = view.findViewById<TextView>(R.id.tv_list_name)
        val listDate = view.findViewById<TextView>(R.id.tv_list_date)
        val listTotalPrice = view.findViewById<TextView>(R.id.tv_list_prices)
        val listEditButton = view.findViewById<ImageButton>(R.id.iv_item_image)
        val listDeleteButton = view.findViewById<ImageButton>(R.id.ib_delete_product)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingListsHolder {
        val view= LayoutInflater.from(parent.context).
        inflate(R.layout.one_lists_row,parent,false)
        return ShoppingListsHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ShoppingListsHolder, position: Int) {
        holder.listName.text = shoppingLists.value?.get(position)?.name

        holder.listDate.text = parseDate(shoppingLists.value?.get(position)?.date!!)
        val total = viewModel.getShoppingListTotalSumPrice(shoppingLists.value?.get(position)?.shoppingListId!!).toString()
        val payed = viewModel.getShoppingListPayedSumPrice(shoppingLists.value?.get(position)?.shoppingListId!!).toString()
        holder.listTotalPrice.text = "${payed}/${total} zł"

        holder.listDeleteButton.setOnClickListener {
            shoppingLists.value?.let{ existingLists->
                viewModel.deleteShoppingList(existingLists[position])
            }
        }

        holder.listEditButton.setOnClickListener {
            communicator.setShoppingListId(shoppingLists.value?.get(position)?.shoppingListId!!)
            holder.itemView.findNavController().navigate(R.id.action_mainFragment_to_listEdit)
        }
    }

    override fun getItemCount()=shoppingLists.value?.size?:0

    private fun parseDate(date: Date): String {
        val dateValue = Calendar.getInstance(TimeZone.getTimeZone("Europe/Poland"))
        dateValue.time = date
        val day: String = if(dateValue.get(Calendar.DAY_OF_MONTH) < 10) "0${dateValue.get(Calendar.DAY_OF_MONTH)}" else dateValue.get(Calendar.DAY_OF_MONTH).toString()
        val month: String = if(dateValue.get(Calendar.MONTH) + 1 < 10) "0${dateValue.get(Calendar.MONTH) + 1}" else (dateValue.get(Calendar.MONTH) + 1).toString()
        return "${day}.${month}.${dateValue.get(Calendar.YEAR)}"
    }

}