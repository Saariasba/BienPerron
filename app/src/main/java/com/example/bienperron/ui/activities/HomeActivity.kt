package com.example.bienperron.ui.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bienperron.R
import com.example.bienperron.adapters.TiendasAdapterController
import com.example.bienperron.listeners.TiendasListener
import com.example.bienperron.model.Tienda
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class HomeActivity : AppCompatActivity() {

    val db = Firebase.firestore

    private val recyclerView by lazy<RecyclerView> { findViewById(R.id.list_of_tiendas) }

    private val adapterController by lazy {
        TiendasAdapterController(
            object : TiendasListener {
                override fun goToTienda(tienda: Tienda) {
                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("geo:<${tienda.latitud}>,<${tienda.longitud}>?q=<${tienda.latitud}>,<${tienda.longitud}>(Label+Name)")
                    )
                    startActivity(intent)
                }
            },
            this
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        init()

        db.collection("Tiendas")
            .get()
            .addOnSuccessListener { documents ->
                val listTiendas = mutableListOf<Tienda>()
                for (document in documents) {
                    listTiendas.add(
                        Tienda(
                            id = document.id,
                            nombre = document.data["nombre"] as String,
                            image = document.data["image"] as String,
                            latitud = document.data["latitud"] as Double,
                            longitud = document.data["longitud"] as Double,
                            telefono = document.data["telefono"] as String
                        )
                    )
                    Log.d("DEBUG_PRUEBA", "${document.id} => ${document.data}")
                }
                adapterController.setData(listTiendas)
            }
            .addOnFailureListener { exception ->
                Log.w("DEBUG_PRUEBA", "Error getting documents: ", exception)
            }


    }

    private fun init() {
        recyclerView.layoutManager = GridLayoutManager(applicationContext, 1)
        recyclerView.adapter = adapterController.adapter
    }
}