package com.example.jomeco.network

import com.example.jomeco.network.Product
import retrofit2.http.GET
import retrofit2.http.Path


data class OpenFoodFactsResponse(
    val status: Int,
    val product: Product?
)

data class Product(
    val product_name: String?,
    val brands: String?,
    val ingredients_text: String?,
    val countries: String?,
    val countries_tags: List<String>?,
    val image_url: String?
)


interface OpenFoodFactsApi {
    @GET("api/v0/product/{barcode}.json")
    suspend fun getProduct(@Path("barcode") barcode: String): OpenFoodFactsResponse
}
