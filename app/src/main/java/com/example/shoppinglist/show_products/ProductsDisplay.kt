package com.example.shoppinglist.show_products

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.R
import com.example.shoppinglist.adapters.ProductsListAdapter
import com.example.shoppinglist.adapters.ShoppingListsAdapter
import com.example.shoppinglist.data.api.dto.ProductDto
import com.example.shoppinglist.data.api.dto.ProductsListDto
import com.example.shoppinglist.data.api.services.ProductService
import com.example.shoppinglist.data.database.entities.ListProduct
import com.example.shoppinglist.data.database.entities.ShoppingList
import com.example.shoppinglist.view_models.Communicator
import com.example.shoppinglist.view_models.ProductsViewModel
import com.example.shoppinglist.view_models.ShoppingListsViewModel
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.await

class ProductsDisplay : Fragment() {
    private lateinit var service: ProductService
    private var communicator: Communicator?= null

    private var productsList: List<ProductDto> = listOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        service = ProductService.create()
        communicator = ViewModelProvider(requireActivity())[Communicator::class.java]

        return inflater.inflate(R.layout.products_display_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        service.getProducts().enqueue( object : Callback<ProductsListDto> {
            override fun onResponse(call: Call<ProductsListDto>?, response: Response<ProductsListDto>?) {
                if (response?.body() != null){
                    productsList = response.body()!!.products

                    val shoppingListsAdapter= ProductsListAdapter(productsList, communicator!!)

                    val layoutManager= LinearLayoutManager(view.context)
                    view.findViewById<RecyclerView>(R.id.products_display_list).let {
                        it!!.adapter=shoppingListsAdapter
                        it.layoutManager=layoutManager
                    }
                }
            }

            override fun onFailure(call: Call<ProductsListDto>?, t: Throwable?) {
                if (t != null) {
                    throw Error("Pobranie produktów z API nie powiodło się: ${t.message}")
                }
            }
        })
    }
}