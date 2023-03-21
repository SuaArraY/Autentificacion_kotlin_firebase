package com.example.loginfirebase

import android.net.wifi.p2p.WifiP2pManager.ActionListener
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    //variable que conecta a Firebase
    private lateinit var firebaseAuth: FirebaseAuth
    private  lateinit var  authStateListener : FirebaseAuth.AuthStateListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //variables conectando a los elementos de la ACTIVIDAD 1
        val btnIngresar : Button = findViewById(R.id.btnRegistrar)
        val txtemail : TextView = findViewById(R.id.txtCorreo)
        val txtpass: TextView = findViewById(R.id.txtContra)
        firebaseAuth = Firebase.auth
        //funcionalidad del boton registrar
        btnIngresar.setOnClickListener()
        {
signIn(txtemail.text.toString(), txtpass.text.toString())
        }

    }
    //funciones
    private  fun signIn(email: String, password: String)
    {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
            if  (task.isSuccessful) {
                val user = firebaseAuth.currentUser
                Toast.makeText(baseContext, user?.uid.toString(), Toast.LENGTH_SHORT).show()
                //aqui debe de abrir el segundo activity

            }
            else
            {
                Toast.makeText(baseContext, "datos incorrectos", Toast.LENGTH_SHORT).show()
            }

        }
    }
}