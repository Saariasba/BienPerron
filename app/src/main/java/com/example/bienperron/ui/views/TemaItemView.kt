package com.example.bienperron.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.airbnb.epoxy.*
import com.example.bienperron.R
import com.example.bienperron.listeners.TiendasListener
import com.example.bienperron.model.Tienda
import com.squareup.picasso.Picasso


@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class TemaItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0

) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val tienda by lazy<TextView> { findViewById(R.id.tienda) }
    private val image by lazy<ImageView> { findViewById(R.id.image) }
    val animationScale = AnimationUtils.loadAnimation(context, R.anim.button_choice_small)
    private var listener: TiendasListener? = null

    private lateinit var data: Tienda

    init {
        View.inflate(context, R.layout.tema_list_item, this)
        listeners()
    }

    private fun listeners() {
        rootView.setOnClickListener {
            it.startAnimation(animationScale)
            listener?.goToTienda(data)
        }
    }

    @ModelProp
    fun setData(data: Tienda) {
        this.data = data
    }

    @CallbackProp
    fun setListener(listener: TiendasListener?) {
        this.listener = listener
    }

    @AfterPropsSet
    fun setText() {
        tienda.text = data.nombre
        Picasso
            .with(context)
            .load(data.image)
            .into(image)
    }
}