package com.example.loginfirebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class listaActividades : AppCompatActivity() {


    private lateinit var listaDatos: ListView
    private lateinit var datosList: ArrayList<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_actividades)
        listaDatos = findViewById(R.id.listaDatos)
        datosList = ArrayList<String>()

        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("datos")

        val adapter = ArrayAdapter(this@listaActividades, android.R.layout.simple_list_item_1, datosList)
        listaDatos.adapter = adapter

        val btnCargarDatos = findViewById<Button>(R.id.btnCargarDatos)
        btnCargarDatos.setOnClickListener {
            ref.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    datosList.clear()
                    for (data in dataSnapshot.children) {
                        val hora = data.child("hora").value.toString()
                        val fecha = data.child("fecha").value.toString()
                        val otroDato = data.child("otroDato").value.toString()
                        val user = data.child("usuario").value.toString()

                        val datos = "  Usuario: $user     Actividad: $otroDato        Hora: $hora        Fecha: $fecha "
                        datosList.add(datos)
                    }

                    adapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@listaActividades, "Error al obtener los datos de Firebase", Toast.LENGTH_SHORT).show()
                }
            })
        }

    }
}