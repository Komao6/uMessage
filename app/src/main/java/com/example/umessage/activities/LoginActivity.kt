package com.example.umessage.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.umessage.R
import com.google.firebase.auth.FirebaseAuth

class LoginActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportActionBar?.hide()

        val email = findViewById<EditText>(R.id.email_login_edt)
        val password = findViewById<EditText>(R.id.password_login_edt)
        val loginButton = findViewById<Button>(R.id.login_bt_log_screen)

        loginButton.setOnClickListener {
            val getEmail = email.text.toString()
            val getPassword = password.text.toString()

            if(getEmail.isEmpty() || getPassword.isEmpty()) {
                Toast.makeText(this,"Please enter your email/password",Toast.LENGTH_SHORT).show()
            }else {
                // Firebase
                FirebaseAuth.getInstance().signInWithEmailAndPassword(getEmail,getPassword)
                    .addOnCompleteListener(this) { task ->
                        if(task.isSuccessful) {
                            val intent = Intent(this, AllMessagesActivity::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this,"User does not exist",Toast.LENGTH_SHORT).show()
                        }
                    }
            }


        }


        val signUpTv = findViewById<TextView>(R.id.sign_up_tv)
        signUpTv.setOnClickListener {
            finish()
        }
    }

}