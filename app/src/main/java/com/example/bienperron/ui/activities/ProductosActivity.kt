package com.example.bienperron.ui.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bienperron.R
import com.example.bienperron.adapters.ProductosAdapterController
import com.example.bienperron.listeners.ProductosListener
import com.example.bienperron.model.Producto
import com.example.bienperron.model.Tienda
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ProductosActivity : AppCompatActivity() {

    lateinit var tienda: Tienda

    private val recyclerView by lazy<RecyclerView> { findViewById(R.id.list_of_productos) }
    private val tiendaNombre by lazy<TextView> { findViewById(R.id.tittle_tienda) }
    private val maps by lazy<Button> { findViewById(R.id.maps) }

    lateinit var shimmerFrameLayout: ShimmerFrameLayout

    val db = Firebase.firestore

    private val adapterController by lazy {
        ProductosAdapterController(
            object : ProductosListener {
                override fun goToProducto(producto: Producto) {
                    val msj =
                        "Hola ${tienda.nombre}, me envía BienPerrón ya que es estoy interesado en ${producto.nombre},¿me puedes ayudar?"
                    val intent = Intent(Intent.ACTION_VIEW)
                    val uri = "whatsapp://send?phone=${tienda.telefono}&text=$msj"
                    intent.data = Uri.parse(uri)
                    startActivity(intent)
                }
            },
            this
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_productos)
        tienda = Tienda(
            id = intent.getStringExtra("Tienda_Id").toString(),
            nombre = intent.getStringExtra("Tienda_Nombre"),
            telefono = intent.getStringExtra("Tienda_Telefono"),
            latitud = intent.getDoubleExtra("Tienda_Latitud", 0.0),
            longitud = intent.getDoubleExtra("Tienda_Longitud", 0.0),
        )
        tiendaNombre.text = tienda.nombre
        init()
        busqueda()
    }

    private fun busqueda() {
        db.collection("Tiendas")
            .document(tienda.id)
            .collection("productos")
            .get()
            .addOnSuccessListener { documents ->
                val listProductos = mutableListOf<Producto>()
                for (document in documents) {
                    listProductos.add(
                        Producto(
                            id = document.id,
                            nombre = document.data["nombre"] as String,
                            image = document.data["image"] as String,
                            descripcion = document.data["descripcion"] as String,
                            precio = document.data["precio"] as Long
                        )
                    )
                    Log.d("DEBUG_PRUEBA", "${document.id} => ${document.data}")
                }
                shimmerFrameLayout.visibility = View.GONE
                adapterController.setData(listProductos)
            }
            .addOnFailureListener { exception ->
                Log.w("DEBUG_PRUEBA", "Error getting documents: ", exception)
            }
    }

    private fun init() {
        shimmerFrameLayout = findViewById(R.id.shimmerLayout);
        recyclerView.layoutManager = GridLayoutManager(applicationContext, 1)
        recyclerView.adapter = adapterController.adapter
        maps.setOnClickListener {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("geo:<${tienda.latitud}>,<${tienda.longitud}>?q=<${tienda.latitud}>,<${tienda.longitud}>(Label+Name)")
            )
            startActivity(intent)
        }
    }
}