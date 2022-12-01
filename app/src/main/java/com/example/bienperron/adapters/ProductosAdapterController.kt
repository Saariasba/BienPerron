package com.example.bienperron.adapters

import android.content.Context
import com.airbnb.epoxy.AsyncEpoxyController
import com.example.bienperron.listeners.ProductosListener
import com.example.bienperron.model.Producto
import com.example.bienperron.ui.views.ProductoItemViewModel_


class ProductosAdapterController(
    val listener: ProductosListener,
    val context: Context
) : AsyncEpoxyController() {

    var list = listOf<Producto>()

    fun setData(result: List<Producto>) {
        cancelPendingModelBuild()
        list = result
        requestModelBuild()
    }

    override fun buildModels() {
        showTopics()
    }

    private fun showTopics() {
        list.forEach {
            ProductoItemViewModel_()
                .id(it.nombre)
                .data(it)
                .listener(listener)
                .addTo(this)
        }
    }

}