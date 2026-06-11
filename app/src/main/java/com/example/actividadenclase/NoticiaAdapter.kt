package com.example.actividadenclase

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

class NoticiaAdapter(context: Context, private val noticias: List<Noticia>) :
    ArrayAdapter<Noticia>(context, 0, noticias) {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view = convertView ?: LayoutInflater.from(context)
                .inflate(R.layout.item_noticia, parent, false)

            val noticia = noticias[position]

            view.findViewById<TextView>(R.id.tvCategoria).text = noticia.categoria
            view.findViewById<TextView>(R.id.tvTitulo).text = noticia.titulo
            view.findViewById<TextView>(R.id.tvFecha).text = "Publicada el: ${noticia.fechaPublicacion}"
            view.findViewById<TextView>(R.id.tvUrl).text = noticia.urlNoticia

            Glide.with(context)
                .load(noticia.urlPortada)
                .into(view.findViewById(R.id.ivPortada))

            return view
        }
}