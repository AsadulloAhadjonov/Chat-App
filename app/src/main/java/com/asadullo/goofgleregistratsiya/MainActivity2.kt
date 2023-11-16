package com.asadullo.goofgleregistratsiya

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.asadullo.goofgleregistratsiya.databinding.ActivityMain2Binding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso

class MainActivity2 : AppCompatActivity() {
    private val binding by lazy { ActivityMain2Binding.inflate(layoutInflater) }
    private lateinit var auth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        binding.emailName.text = auth.currentUser?.displayName
        binding.emailEmail.text = auth.currentUser?.email
        Picasso.get().load("${auth.currentUser?.photoUrl}").into(binding.img)

        binding.btnUsers.setOnClickListener {
            startActivity(Intent(this, MainActivity3::class.java))
        }
    }
}