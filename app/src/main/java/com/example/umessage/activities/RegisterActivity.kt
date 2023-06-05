package com.example.umessage.activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.umessage.R
import com.example.umessage.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import de.hdodenhof.circleimageview.CircleImageView
import java.util.UUID

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        supportActionBar?.hide()

        val alreadyHaveAccountTextView = findViewById<TextView>(R.id.alredy_have_acc_tV)
        val registrationButton = findViewById<Button>(R.id.regis_bt_regis_screen)
        val selectPhotoButton = findViewById<Button>(R.id.select_photo_bt)

        registrationButton.setOnClickListener {
           performRegister()
        }
        alreadyHaveAccountTextView.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)

        }

        selectPhotoButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent,0)
        }
    }

    var selectedPhotoUri: Uri?=null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode==0 && resultCode==Activity.RESULT_OK && data!=null) {

            val selectPhotoButton = findViewById<Button>(R.id.select_photo_bt)
            val selectImageView = findViewById<CircleImageView>(R.id.select_imageview_register)
            selectedPhotoUri = data.data

            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver,selectedPhotoUri)

            selectImageView.setImageBitmap(bitmap)
            selectPhotoButton.alpha = 0f
        }
    }

    private fun performRegister() {
        val username = findViewById<EditText>(R.id.username_edt_regis_screen)
        val email = findViewById<EditText>(R.id.email_edt_regis_screen)
        val password = findViewById<EditText>(R.id.password_edt_regis_screen)
        val getUsername = username.text.toString()
        val getEmail = email.text.toString()
        val getPassword = password.text.toString()

        if(email.length()==0 || password.length()==0) {
            Toast.makeText(this, "Please enter your email/password", Toast.LENGTH_SHORT).show()
            return
        }
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(getEmail,getPassword)
            .addOnCompleteListener{
                if (!it.isSuccessful) return@addOnCompleteListener

                //else if successful
                println("Successfully created user uid: ${it.result.user}")

                uploadImageToFirebaseStorage()
            }
            .addOnFailureListener {
                println("Failed to create user: ${it.message}")
                Toast.makeText(this, "Failed to create user: ${it.message}", Toast.LENGTH_SHORT).show()

            }
    }

    private fun uploadImageToFirebaseStorage() {
        if(selectedPhotoUri==null) return

        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/image/$filename")

        ref.putFile(selectedPhotoUri!!)
            .addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener {
                    it.toString()
                    println("File location: $it")

                    savaUserToFirebaseDatabase(it.toString())
                }
            }
            .addOnFailureListener {
                //do some logging
            }
    }

    private fun savaUserToFirebaseDatabase(profileImageUrl: String) {
        val username = findViewById<EditText>(R.id.username_edt_regis_screen).text.toString()
        val uid = FirebaseAuth.getInstance().uid ?:""
        val ref = FirebaseDatabase.getInstance().getReference("/user/$uid")

        val user = User(uid,username,profileImageUrl)
        ref.setValue(user)
            .addOnSuccessListener {
                println("Saved the user to Firebase Database")

                val intent = Intent(this, AllMessagesActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
    }

}

//class UserSet(val uid: String, val username: String, val profileImageUrl: String)