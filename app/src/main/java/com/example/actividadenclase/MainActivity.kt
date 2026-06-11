package com.example.actividadenclase

import android.os.Bundle
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

class MainActivity : AppCompatActivity() {

    private val URL = "https://apiws.uteq.edu.ec/h6RPoSoRaah0Y4Bah28eew/functions/information/entity/1"
    private val TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJfeDF1c2VyZGV2IiwiaWF0IjoxNzgxMjA2NDgwLCJleHAiOjE3ODEyOTI4ODB9.ut9t7jNdM2ubQhp0EZCCytNYR2IQQPmlyoO51V2laGE"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        obtenerNoticias()
    }

    private fun obtenerNoticias() {
        val queue = Volley.newRequestQueue(this)

        val request = object : JsonObjectRequest(
            Request.Method.GET, URL, null,
            { response ->
                try {
                    // Verificamos si existe la clave "data" antes de intentar leerla
                    if (response.has("data") && !response.isNull("data")) {
                        val noticias = mutableListOf<Noticia>()
                        val array = response.getJSONArray("data")

                        for (i in 0 until minOf(10, array.length())) {
                            val obj = array.getJSONObject(i)
                            val categoria = obj.optString("ntTipoNoticia", "Sin categoría")
                            val titulo = obj.optString("ntTitulo", "Sin título")
                            val fecha = obj.optString("ntFechaPublicacion", "").take(10)
                            val urlNoticia = "https://uteq.edu.ec/es/comunicacion/noticia/" +
                                    obj.optString("ntUrlNoticia", "")
                            val urlPortada = "https://uteq.edu.ec/assets/images/news/pagina/" +
                                    obj.optString("ntUrlPortada", "")

                            noticias.add(Noticia(categoria, titulo, urlPortada, fecha, urlNoticia))
                        }

                        val listView = findViewById<ListView>(R.id.lvNoticias)
                        listView.adapter = NoticiaAdapter(this, noticias)
                    } else {
                        // Si no hay "data", mostramos el error que mande el servidor
                        val mensaje = response.optString("message", "Error desconocido en el formato")
                        Toast.makeText(this, "Respuesta inesperada: $mensaje", Toast.LENGTH_LONG).show()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(this, "Error procesando datos: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            },
            { error ->
                val statusCode = error.networkResponse?.statusCode
                Toast.makeText(this, "Error de red ($statusCode): ${error.message}", Toast.LENGTH_LONG).show()
            }
        ) {
            override fun getHeaders(): MutableMap<String, String> {
                return mutableMapOf("Authorization" to "Bearer $TOKEN")
            }
        }

        queue.add(request)
    }
}