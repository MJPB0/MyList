package com.example.shoppinglist.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.R
import com.example.shoppinglist.data.api.dto.ProductDto
import com.example.shoppinglist.view_models.Communicator

class ProductsListAdapter (
    private val productsList: List<ProductDto>,
    private val communicator: Communicator
) : RecyclerView.Adapter<ProductsListAdapter.ProductsListHolder>() {
    inner class ProductsListHolder(private val view: View): RecyclerView.ViewHolder(view)
    {
        val productName = view.findViewById<TextView>(R.id.tv_cart_item_title)
        val productInfoButton = view.findViewById<ImageButton>(R.id.ib_info)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsListHolder {
        val view= LayoutInflater.from(parent.context).
        inflate(R.layout.one_product_row,parent,false)
        return ProductsListHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ProductsListHolder, position: Int) {
        holder.productName.text = productsList[position].name

        holder.productInfoButton.setOnClickListener {
            communicator.setProduct(productsList[position])
            holder.itemView.findNavController().navigate(R.id.action_productsDisplay_to_productInformation)
        }
    }

    override fun getItemCount()=productsList.size?:0

}