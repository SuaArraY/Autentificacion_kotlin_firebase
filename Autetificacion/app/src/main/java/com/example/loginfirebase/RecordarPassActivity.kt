package com.example.loginfirebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RecordarPassActivity : AppCompatActivity() {

    private  lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recordar_pass)
        val txtemail : TextView =findViewById(R.id.txtCambiarContra)
        val btnCambiar : Button = findViewById(R.id.btnCambiar)

        btnCambiar.setOnClickListener(){
            sendPasswordReset(txtemail.text.toString())
        }

        firebaseAuth = Firebase.auth

    }

    private  fun sendPasswordReset(email: String){
        firebaseAuth.sendPasswordResetEmail(email)
            .addOnCompleteListener(){task ->
                if (task.isSuccessful)
                {
                    Toast.makeText(baseContext,"Correo de cambio de contraseña enviado", Toast.LENGTH_SHORT).show()
                }
                else
                {
                    Toast.makeText(baseContext,"ERROR, NO SE PUDO COMPLETAR EL PROCESO", Toast.LENGTH_LONG).show()
                }

            }
    }

}