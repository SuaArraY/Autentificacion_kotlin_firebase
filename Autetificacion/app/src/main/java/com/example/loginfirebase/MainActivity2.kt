package com.example.loginfirebase

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.database.ktx.database



@Suppress("DEPRECATION", "NAME_SHADOWING")
class MainActivity2 : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var textViewUsername: TextView

    private lateinit var databaseRef: DatabaseReference
    val database = FirebaseDatabase.getInstance()
    val ref = database.getReference("datos")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        val textViewHora = findViewById<TextView>(R.id.txtSeleccionarFecha)
        val textViewHoraSeleccionada = findViewById<TextView>(R.id.txtFecha)
        val us = findViewById<TextView>(R.id.textView2)

        textViewHora.setOnClickListener {
            val calendar = Calendar.getInstance()
            val horaActual = calendar.get(Calendar.HOUR_OF_DAY)
            val minutoActual = calendar.get(Calendar.MINUTE)

            val timePickerDialog = TimePickerDialog(
                this,
                { _, horaSeleccionada, minutoSeleccionado ->
                    val horaFormateada = String.format("%02d:%02d", horaSeleccionada, minutoSeleccionado)
                    textViewHoraSeleccionada.text = horaFormateada
                },
                horaActual,
                minutoActual,
                true
            )
            timePickerDialog.show()
        }

        val textViewFecha = findViewById<TextView>(R.id.txtCalendario)
        val textViewFechaSeleccionada = findViewById<TextView>(R.id.txtDia)

        textViewFecha.setOnClickListener {
            val calendar = Calendar.getInstance()
            val anioActual = calendar.get(Calendar.YEAR)
            val mesActual = calendar.get(Calendar.MONTH)
            val diaActual = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                { _, anioSeleccionado, mesSeleccionado, diaSeleccionado ->
                    val fechaFormateada = String.format("%02d/%02d/%04d", diaSeleccionado, mesSeleccionado + 1, anioSeleccionado)
                    textViewFechaSeleccionada.text = fechaFormateada
                },
                anioActual,
                mesActual,
                diaActual
            )
            datePickerDialog.show()
        }
        val editTextOtroDato = findViewById<EditText>(R.id.txtRegistroActividad)
        val botonGuardar = findViewById<Button>(R.id.btnGuardarActividad)
        botonGuardar.setOnClickListener {
            val use = us.text.toString()
            val horaSeleccionada = textViewHoraSeleccionada.text.toString()
            val fechaSeleccionada = textViewFechaSeleccionada.text.toString()
            val otroDato = editTextOtroDato.text.toString()

            val datos = HashMap<String, String>()
            datos["usuario"] = use
            datos["hora"] = horaSeleccionada
            datos["fecha"] = fechaSeleccionada
            datos["otroDato"] = otroDato

            ref.push().setValue(datos)
                .addOnSuccessListener {
                    Toast.makeText(this, "Datos guardados en Firebase", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Error al guardar los datos en Firebase", Toast.LENGTH_SHORT).show()
                }
        }


        //val btnCamara: Button = findViewById(R.id.btnCamara)
        //val btnBaseDatos: Button = findViewById(R.id.btnCrearBaseDatos)
        //val btnCamra2 = findViewById<Button>(R.id.btnCamara2)

        /* btnCamra2.setOnClickListener(){
            val i = Intent(this, camara2::class.java)
            startActivity(i)
        }

        btnBaseDatos.setOnClickListener(){
            crearBaseDeDatos()
        }

        btnCamara.setOnClickListener(){
            val i = Intent(this, tomarFoto::class.java)
            startActivity(i)
        }*/

        firebaseAuth = Firebase.auth
        databaseRef = Firebase.database.reference

        //obtener el usuario actual y mostrar su nombre en un TextView
        val currentUser = firebaseAuth.currentUser
        val userId = currentUser?.uid
        val txtUsername: TextView = findViewById(R.id.textView2)
        databaseRef.child("users").child(userId.toString()).child("name")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val name = snapshot.value.toString()
                    txtUsername.text = name
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("Firebase", "Error al obtener el nombre del usuario")
                }
            })

        //configurar la imagen de perfil del usuario
        //val imgProfile: ImageView = findViewById(R.id.imageView2)
        //val photoUrl = currentUser?.photoUrl
         //Glide.with(this).load(photoUrl).into(imgProfile)

    }


    private  fun crearBaseDeDatos(){

        // Crea una instancia de Firebase Database
        val database = Firebase.database

        // Crea una referencia a la raíz de la base de datos
        val myRef = database.reference

        // Crea un objeto de tipo Producto con los datos que deseas almacenar
        val producto = Producto("1234", "Descripción del producto", 10)

        // Almacena el objeto en la base de datos bajo la referencia "productos"
        myRef.child("productos").push().setValue(producto)

    }


    data class Producto(val codigo: String, val descripcion: String, val stock: Int)

    //para cuando retrocedamos no salga del menu principal
    //pasos
    //CODE - OVERRIDE METHODS
    //y buscamos  OnBackPressed

    //override fun onBackPressed() {
       // super.onBackPressed()             - el super permite retroceder con el boton retroceder
    //}

    //para agregar el menu creado en la carpeta res - menu -nav_menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu,menu)
        return true
    //super.onCreateOptionsMenu(menu)
    }

    // ´para dar funcionalidad a los elementos del menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId)
        {
            R.id.menu_registrarActividad ->{
                Toast.makeText(baseContext,"Buscar Info", Toast.LENGTH_LONG).show()
            }
            R.id.menu_listarActividad ->{
                Toast.makeText(baseContext,"Buscar Info", Toast.LENGTH_LONG).show()
            }
            R.id.menu_salir ->{
                signOut()
            }
        }


        return super.onOptionsItemSelected(item)
    }


    override fun onBackPressed() {
        return
    }


    private fun signOut(){
        firebaseAuth.signOut()
        Toast.makeText(baseContext,"Se cerro exitosamente la cuenta", Toast.LENGTH_LONG).show()
        val i = Intent(this,MainActivity::class.java)
        startActivity(i)
    }

}