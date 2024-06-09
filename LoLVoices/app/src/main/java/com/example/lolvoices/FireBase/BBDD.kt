package com.example.lolvoices.FireBase

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore

class FireStoreBBDD () {

    val db = Firebase.firestore

    /**
     * Añade una puntuación a la base de datos
     */
    fun addPuntuacion(puntuacion: Int, usuario: String) {
        val puntuacion = hashMapOf(
            "usuario" to usuario,
            "puntuacion" to puntuacion
        )

        db.collection("puntuaciones")
            .add(puntuacion)
            .addOnSuccessListener { documentReference ->
                Log.d("TAG", "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w("TAG", "Error adding document", e)
            }
    }

    /**
     * Obtiene las puntuaciones de la base de datos
     */
    fun getPuntuaciones(callback: (List<Map<String, Any>>) -> Unit) {

        val puntuaciones = db.collection("puntuaciones")

        val query = puntuaciones
            .orderBy("puntuacion", Query.Direction.DESCENDING)  // Ordena en orden descendente
            .limit(25)

        query.get()
            .addOnSuccessListener { result ->
                Log.d("TAG", "Query successful. Documents fetched: ${result.size()}")
                val puntuacionesList = mutableListOf<Map<String, Any>>()
                for (document in result) {
                    Log.d("TAG", "Document: ${document.id} => ${document.data}")
                    puntuacionesList.add(document.data)
                }
                callback(puntuacionesList)
            }
            .addOnFailureListener { exception ->
                Log.w("TAG", "Error getting documents: ", exception)
                callback(emptyList()) // Retorna una lista vacía en caso de error
            }

    }
}