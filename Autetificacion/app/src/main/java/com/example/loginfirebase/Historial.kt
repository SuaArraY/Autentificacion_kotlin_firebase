package com.example.loginfirebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Historial : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_historial)

        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("datos")

        val listaDatos = findViewById<ListView>(R.id.listaDatos)
        val datosList = ArrayList<String>()
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                datosList.clear()
                for (data in dataSnapshot.children) {
                    val hora = data.child("hora").value.toString()
                    val fecha = data.child("fecha").value.toString()
                    val otroDato = data.child("otroDato").value.toString()

                    val datos = "Hora: $hora\nFecha: $fecha\nOtro dato: $otroDato"
                    datosList.add(datos)
                }

                val adapter = ArrayAdapter(this@Historial, android.R.layout.simple_list_item_1, datosList)
                listaDatos.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@Historial, "Error al obtener los datos de Firebase", Toast.LENGTH_SHORT).show()
            }
        })

    }

}