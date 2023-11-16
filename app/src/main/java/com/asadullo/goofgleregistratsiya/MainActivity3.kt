package com.asadullo.goofgleregistratsiya

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.asadullo.goofgleregistratsiya.Adapter.AdapterGroups
import com.asadullo.goofgleregistratsiya.Adapter.AdapterRv
import com.asadullo.goofgleregistratsiya.Models.Groups
import com.asadullo.goofgleregistratsiya.Models.User
import com.asadullo.goofgleregistratsiya.databinding.ActivityMain3Binding
import com.asadullo.goofgleregistratsiya.databinding.ItemDialogBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

private const val TAG = "MainActivity3"
class MainActivity3 : AppCompatActivity() {
    private val binding by lazy { ActivityMain3Binding.inflate(layoutInflater) }
    private lateinit var auth:FirebaseAuth
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var reference: DatabaseReference
    private lateinit var adapter:AdapterRv
    private lateinit var list:ArrayList<User>
    private lateinit var listGroups:ArrayList<Groups>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()

        referensFun()

        binding.btnChats.setOnClickListener {
            referensFun()
        }

        binding.btnGroups.setOnClickListener {
            groups()
        }

    }

    fun referensFun(){
        reference = firebaseDatabase.getReference("users")
        reference.addValueEventListener(object  : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                list = ArrayList()
                val  children = snapshot.children
                for (child in children){
                    val user = child.getValue(User::class.java)
                    if (user != null) {
                        list.add(user)
                    }
                }
                Log.d(TAG, "onDataChange: $list")
                adapter = AdapterRv(list,object : AdapterRv.RvClick{
                    override fun click(list: ArrayList<User>, position: Int, user: User) {
                        var intent = Intent(this@MainActivity3, MainActivity4::class.java)
                        intent.putExtra("imgLink", list[position].imgLink)
                        intent.putExtra("user_name", list[position].displayName)
                        intent.putExtra("user", user)
                        startActivity(intent)
                    }
                })
                binding.rv.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity3, "Error", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun groups(){
        binding.addGroups.setOnClickListener {
            val dialog = AlertDialog.Builder(this).create()
            val item = ItemDialogBinding.inflate(layoutInflater)
            dialog.setView(item.root)

            item.btnAdd.setOnClickListener {
                add(item.edtGroupName.text.toString())
                dialog.cancel()
            }

            dialog.show()
        }
        add("a")
    }

    fun add(name:String){
        reference = firebaseDatabase.getReference("groups")
        reference.push().setValue(reference.key)
        reference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                listGroups = ArrayList()
                val children  = snapshot.children
                for (child in children) {
                    val groups = child.getValue(Groups::class.java)
                    if (groups!=null){
                        listGroups.add(groups)
                    }
                }
                binding.rv.adapter = AdapterGroups(listGroups)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity3, "Error groups", Toast.LENGTH_SHORT).show()
            }
        })
    }
}