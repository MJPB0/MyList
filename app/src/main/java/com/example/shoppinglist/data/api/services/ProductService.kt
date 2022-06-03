package com.example.shoppinglist.data.api.services

import com.example.shoppinglist.data.api.dto.ProductsListDto
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface ProductService {
    // Uźywane do pobrania listy wszystkich produktów z API.
    @GET("HttpProducts")
    fun getProducts() : Call<ProductsListDto>

    companion object {
        var BASE_URL = "http://10.0.2.2:7071/api/"
        fun create() : ProductService {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(ProductService::class.java)
        }
    }
}