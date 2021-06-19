package com.example.phoneauthentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    lateinit var storedVerificationId:String
    lateinit var resendToken:PhoneAuthProvider.ForceResendingToken
    private lateinit var callbacks:PhoneAuthProvider.OnVerificationStateChangedCallbacks

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        auth=FirebaseAuth.getInstance()

        //Reference
        val login=findViewById<Button>(R.id.loginBtn)


        val currentUser=auth.currentUser
        if(currentUser!=null){
            startActivity(Intent(applicationContext,HomeActivity::class.java))
            finish()
        }
        login.setOnClickListener{
            login()
        }

        callbacks=object:PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
            override fun onVerificationCompleted(p0: PhoneAuthCredential) {
               startActivity(Intent(applicationContext,HomeActivity::class.java))
                finish()
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                Toast.makeText(applicationContext,"Failed",Toast.LENGTH_LONG).show()
            }

            override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                Log.d("TAG","onCodeSent:$p0")
                storedVerificationId=p0
                resendToken=p1

                val intent=Intent(applicationContext,VerifyActivity::class.java)
                intent.putExtra("storedVerificationId",storedVerificationId)
                startActivity(intent)
            }

        }

    }

    private fun login() {
        val mobileNumber=findViewById<EditText>(R.id.phoneNumber)
            var number=mobileNumber.text.toString().trim()
        if(!number.isEmpty()){
            number="+91"+number
            sendVerificationCode(number)

        }else{
            Toast.makeText(this,"Enter Mobile Number",Toast.LENGTH_SHORT).show()
        }

    }

    private fun sendVerificationCode(number: String) {
        val options=PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(number)
            .setTimeout(60L,TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(callbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)


    }


}