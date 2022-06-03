package com.example.shoppinglist.data.api.dto

// Data class listy produktów, które zostaną pobrane z API.
data class ProductsListDto(
    var products: List<ProductDto>
)