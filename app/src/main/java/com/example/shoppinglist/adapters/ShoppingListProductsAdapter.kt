package com.example.shoppinglist.adapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.R
import com.example.shoppinglist.data.database.entities.ListProduct
import com.example.shoppinglist.view_models.Communicator
import com.example.shoppinglist.view_models.ProductsViewModel

class ShoppingListProductsAdapter (
    private val shoppingListProducts: LiveData<List<ListProduct>>,
    private val viewModel: ProductsViewModel,
    private val communicator: Communicator
) : RecyclerView.Adapter<ShoppingListProductsAdapter.ShoppingListProductsHolder>() {
    inner class ShoppingListProductsHolder(private val view: View): RecyclerView.ViewHolder(view)
    {
        val productName = view.findViewById<TextView>(R.id.one_list_product_row_name)
        val productAmount = view.findViewById<TextView>(R.id.one_list_product_row_amount)
        val productPriceAmount = view.findViewById<TextView>(R.id.one_list_product_row_price)
        val productDeleteButton = view.findViewById<ImageButton>(R.id.one_list_product_row_delete_button)
        val increaseAmountButton = view.findViewById<ImageButton>(R.id.one_list_product_row_increase_amount)
        val decreaseAmountButton = view.findViewById<ImageButton>(R.id.one_list_product_row_reduce_amount)
        val productInfoButton = view.findViewById<ImageButton>(R.id.one_list_product_row_info_button)
        val productBoughtCheckBox = view.findViewById<CheckBox>(R.id.one_list_product_row_checkbox)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingListProductsHolder {
        val view= LayoutInflater.from(parent.context).
        inflate(R.layout.one_list_product_row,parent,false)
        return ShoppingListProductsHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ShoppingListProductsHolder, position: Int) {
        val product = shoppingListProducts.value?.get(position)
        var price = product?.cost!!
        var amount = product.amount

        holder.productName.text = shoppingListProducts.value?.get(position)?.product
        holder.productAmount.text = amount.toString()
        holder.productPriceAmount.text = "${price * amount}zł"
        holder.productBoughtCheckBox.isChecked = shoppingListProducts.value?.get(position)?.wasBought!!

        holder.productDeleteButton.setOnClickListener {
            shoppingListProducts.value?.let{ existingProducts->
                viewModel.deleteListProduct(existingProducts[position].productId)
            }
        }

        holder.productInfoButton.setOnClickListener {
            communicator.setAddedProductId(shoppingListProducts.value?.get(position)!!.productId)
            holder.itemView.findNavController().navigate(R.id.action_listEdit_to_addedProductInformation)
        }

        holder.increaseAmountButton.setOnClickListener {
            shoppingListProducts.value?.let{ existingProducts->
                viewModel.increaseProductAmount(existingProducts[position].productId)

                price = shoppingListProducts.value?.get(position)?.cost!!
                amount = shoppingListProducts.value?.get(position)?.amount!!
                holder.productAmount.text = amount.toString()
                holder.productPriceAmount.text = "${price * amount}zł"
            }
        }

        holder.decreaseAmountButton.setOnClickListener {
            shoppingListProducts.value?.let{ existingProducts->
                viewModel.reduceProductAmount(existingProducts[position].productId)

                price = shoppingListProducts.value?.get(position)?.cost!!
                amount = shoppingListProducts.value?.get(position)?.amount!!
                holder.productAmount.text = amount.toString()
                holder.productPriceAmount.text = "${price * amount}zł"
            }
        }

        holder.productBoughtCheckBox.setOnClickListener {
            if (holder.productBoughtCheckBox.isChecked){
                shoppingListProducts.value?.let{ existingProducts->
                    shoppingListProducts.value?.get(position)!!.wasBought = true
                    viewModel.updateListProduct(shoppingListProducts.value?.get(position)!!)
                    Log.d("SHOPPING LIST PRODUCTS ADAPTER", "Przedmiot został oznaczony jako kupiony")
                }
            }else{
                shoppingListProducts.value?.let{ existingProducts->
                    shoppingListProducts.value?.get(position)!!.wasBought = false
                    viewModel.updateListProduct(shoppingListProducts.value?.get(position)!!)
                    Log.d("SHOPPING LIST PRODUCTS ADAPTER", "Przedmiot nie jest już oznaczony jako kupiony")
                }
            }
        }
    }

    override fun getItemCount()=shoppingListProducts.value?.size?:0

}