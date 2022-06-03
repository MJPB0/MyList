package com.example.shoppinglist.product_info

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.shoppinglist.R
import com.example.shoppinglist.data.api.dto.ProductDto
import com.example.shoppinglist.data.api.services.ProductService
import com.example.shoppinglist.view_models.Communicator
import com.example.shoppinglist.view_models.ProductsViewModel

class ProductInformation : Fragment() {
    private var communicator: Communicator?= null

    private lateinit var nameText: TextView
    private lateinit var unitText: TextView
    private lateinit var compositionText: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        communicator = ViewModelProvider(requireActivity())[Communicator::class.java]

        return inflater.inflate(R.layout.product_information_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.nameText = view.findViewById<TextView>(R.id.product_information_name)
        this.unitText = view.findViewById<TextView>(R.id.product_information_unit)
        this.compositionText = view.findViewById<TextView>(R.id.product_information_description)

        val product: ProductDto = communicator?.Product?.value!!
        this.nameText.text = product.name
        this.unitText.text = product.unit
        this.compositionText.text = product.description
    }
}