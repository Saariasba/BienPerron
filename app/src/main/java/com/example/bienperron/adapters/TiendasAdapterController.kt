package com.example.bienperron.adapters

import android.content.Context
import com.airbnb.epoxy.AsyncEpoxyController
import com.example.bienperron.listeners.TiendasListener
import com.example.bienperron.model.Tienda
import com.example.bienperron.ui.views.TemaItemViewModel_


class TiendasAdapterController(
    val listener: TiendasListener,
    val context: Context
) : AsyncEpoxyController() {

    var list = listOf<Tienda>()

    fun setData(result: List<Tienda>) {
        cancelPendingModelBuild()
        list = result
        requestModelBuild()
    }

    override fun buildModels() {
        showTopics()
    }

    private fun showTopics() {
        list.forEach {
            TemaItemViewModel_()
                .id(it.nombre)
                .data(it)
                .listener(listener)
                .addTo(this)
        }
    }

}