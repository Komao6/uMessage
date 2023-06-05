 package com.example.umessage.activities

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.umessage.adapters.MessageAdapter
import com.example.umessage.R
import com.example.umessage.models.Message
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

 class ChatActivity : AppCompatActivity() {

     private lateinit var  messageAdapter: MessageAdapter
     private lateinit var messageList: ArrayList<Message>

     var receiverRoom: String? = null
     var senderRoom: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val chatRecyclerView = findViewById<RecyclerView>(R.id.recyclerview_chat)
        val sendButton = findViewById<ImageView>(R.id.send_imageview)
        val typeText = findViewById<EditText>(R.id.type_a_message_edt)

        val username = intent.getStringExtra("name")
        val receiverUid = intent.getStringExtra("uid")

        val senderUid = FirebaseAuth.getInstance().currentUser?.uid


        senderRoom = receiverUid + senderUid
        receiverRoom = senderUid + receiverUid

        supportActionBar?.title = username

        messageList = ArrayList()
        messageAdapter = MessageAdapter(this@ChatActivity,messageList)


        chatRecyclerView.layoutManager = LinearLayoutManager(this)
        chatRecyclerView.adapter = messageAdapter

        //logic for adding data to recyclerview
        FirebaseDatabase.getInstance().reference.child("chats").child(senderRoom!!).child("messages")
            .addValueEventListener(object: ValueEventListener{
                @SuppressLint("NotifyDataSetChanged")
                override fun onDataChange(snapshot: DataSnapshot) {

                    println("Message must be shown")
                    messageList.clear()

                    for(postSnapshot in snapshot.children){


                        val message = postSnapshot.getValue(Message::class.java)
                        messageList.add(message!!)
                    }
                    messageAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {

                }

            }
            )


        //adding the message to database
        sendButton.setOnClickListener {

            println("Button is working")
            val message = typeText.text.toString()
            val messageObject = Message(message,senderUid.toString())

            FirebaseDatabase.getInstance().reference.child("chats").child(senderRoom!!).child("messages").push()
                .setValue(messageObject).addOnSuccessListener {
                    FirebaseDatabase.getInstance().reference.child("chats").child(receiverRoom!!).child("messages").push()
                        .setValue(messageObject)
                }
            typeText.setText("")
        }

    }
}