package com.example.phoneauthentication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class HomeActivity : AppCompatActivity() {


    private lateinit var auth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)


        auth=FirebaseAuth.getInstance()
        val currentUser=auth.currentUser

        //Reference
        val logout=findViewById<Button>(R.id.idLogout)
        if (currentUser==null){
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }
        logout.setOnClickListener{
            auth.signOut()
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }




    }
}