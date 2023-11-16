package com.asadullo.goofgleregistratsiya

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.view.View
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.AnimationUtils
import android.widget.MediaController
import android.widget.Toast
import android.widget.VideoView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.asadullo.goofgleregistratsiya.Adapter.AdapterFromAndTo
import com.asadullo.goofgleregistratsiya.Models.MyMessage
import com.asadullo.goofgleregistratsiya.Models.User
import com.asadullo.goofgleregistratsiya.databinding.ActivityMain3Binding
import com.asadullo.goofgleregistratsiya.databinding.ActivityMain4Binding
import com.asadullo.goofgleregistratsiya.databinding.ItemImgAndVideoBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity4 : AppCompatActivity() {
    private val binding by lazy { ActivityMain4Binding.inflate(layoutInflater) }
    lateinit var toUser: User
    private lateinit var reference: DatabaseReference
    private lateinit var referenceStorage: StorageReference
    private lateinit var database: FirebaseDatabase
    lateinit var auth: FirebaseAuth
    lateinit var dateFormat: SimpleDateFormat
    private lateinit var date:Date
    private lateinit var firebaseStorage: FirebaseStorage
    lateinit var item : ItemImgAndVideoBinding
    lateinit var adapter:AdapterFromAndTo
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        dateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())
        date = Date()
        val img = intent.getStringExtra("imgLink")
        val name = intent.getStringExtra("user_name")
        toUser = intent.getSerializableExtra("user") as User
        Picasso.get().load("$img").into(binding.imgUser)
        binding.userName.text = name
        database = FirebaseDatabase.getInstance()
        reference = database.getReference("messages")
        auth = FirebaseAuth.getInstance()
        firebaseStorage = FirebaseStorage.getInstance()

        binding.btnSend.setOnClickListener {
            val text = binding.edyMessage.text.toString()
            val id = reference.push().key
            val message = MyMessage(id, text , auth.uid, toUser.uid)
            reference.child(id!!).setValue(message)
            binding.edyMessage.text.clear()
        }

        binding.btnImg.setOnClickListener {
            val dialog = AlertDialog.Builder(this).create()
            item = ItemImgAndVideoBinding.inflate(layoutInflater)
            dialog.setView(item.root)

            referenceStorage = firebaseStorage.getReference("my_images")

            item.img.setOnClickListener {
                getImage.launch("image/*")
            }
            item.btnSent.setOnClickListener {
                val imgUrl = referenceStorage.child("${dateFormat.format(date)}rasm")
                Toast.makeText(this, referenceStorage.name, Toast.LENGTH_SHORT).show()
                imgUrl.downloadUrl.addOnSuccessListener {
                    val id = reference.push().key
                    val message = MyMessage(id, it.toString() , auth.uid, toUser.uid)
                    reference.child(id!!).setValue(message)
                    dialog.cancel()
                }

            }

            dialog.show()
        }

        binding.btnVideo.setOnClickListener {
            val dialog = AlertDialog.Builder(this).create()
            item = ItemImgAndVideoBinding.inflate(layoutInflater)
            dialog.setView(item.root)
            referenceStorage = firebaseStorage.getReference("my_videos")
            item.img.visibility = View.GONE
            item.video.visibility = View.VISIBLE
            item.video.setOnClickListener {
                getVideo.launch("video/*")
            }
            item.btnSent.setOnClickListener {
                val videoUrl = referenceStorage.child("${dateFormat.format(date)}video1")
                videoUrl.downloadUrl.addOnSuccessListener {
                    val id = reference.push().key
                    val message = MyMessage(id, it.toString() , auth.uid, toUser.uid)
                    reference.child(id!!).setValue(message)
                    dialog.cancel()
                }
            }

            dialog.show()
        }

        binding.btnSend.setOnLongClickListener {
            val anim = AnimationUtils.loadAnimation(this, R.anim.anim_1)
            val anim2 = AnimationUtils.loadAnimation(this, R.anim.anim_2)
            binding.btnSend.visibility = View.GONE
            binding.btnImg.visibility = View.VISIBLE
            binding.btnImg.startAnimation(anim)
            anim.setAnimationListener(object : AnimationListener{
                override fun onAnimationStart(p0: Animation?) {

                }

                override fun onAnimationEnd(p0: Animation?) {
                    binding.btnImg.startAnimation(anim2)
                }

                override fun onAnimationRepeat(p0: Animation?) {
                }
            })
            true
        }

        binding.btnImg.setOnLongClickListener {
            val anim = AnimationUtils.loadAnimation(this, R.anim.anim_1)
            val anim2 = AnimationUtils.loadAnimation(this, R.anim.anim_2)
            binding.btnImg.visibility = View.GONE
            binding.btnVideo.visibility = View.VISIBLE
            binding.btnVideo.startAnimation(anim)
            anim.setAnimationListener(object : AnimationListener{
                override fun onAnimationStart(p0: Animation?) {

                }

                override fun onAnimationEnd(p0: Animation?) {
                    binding.btnVideo.startAnimation(anim2)
                }

                override fun onAnimationRepeat(p0: Animation?) {
                }
            })
            true
        }

        binding.btnVideo.setOnLongClickListener {
            val anim = AnimationUtils.loadAnimation(this, R.anim.anim_1)
            val anim2 = AnimationUtils.loadAnimation(this, R.anim.anim_2)
            binding.btnVideo.visibility = View.GONE
            binding.btnSend.visibility = View.VISIBLE
            binding.btnSend.startAnimation(anim)
            anim.setAnimationListener(object : AnimationListener{
                override fun onAnimationStart(p0: Animation?) {

                }

                override fun onAnimationEnd(p0: Animation?) {
                    binding.btnSend.startAnimation(anim2)
                }

                override fun onAnimationRepeat(p0: Animation?) {
                }
            })
            true
        }

        binding.back.setOnClickListener {
            finish()
        }
        adapter = AdapterFromAndTo(auth.uid!!, ArrayList())
        binding.rv.adapter = adapter
        reference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = ArrayList<MyMessage>()
                val children = snapshot.children
                for (child in children) {
                    val message = child.getValue(MyMessage::class.java)
                    if (message!=null){
                        if ((message.fromUid == auth.uid && message.toUid==toUser.uid) || (message.fromUid==toUser.uid && message.toUid==auth.uid)) {
                            list.add(message)
                        }
                    }
                }
                adapter.list.clear()
                adapter.list.addAll(list)
                adapter.notifyDataSetChanged()
                binding.rv.scrollToPosition(list.size-1)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity4, "Error", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private var getImage = registerForActivityResult(ActivityResultContracts.GetContent()){uri ->
        uri ?: return@registerForActivityResult
        val putFile = referenceStorage.child("${dateFormat.format(date)}rasm").putFile(uri)
        item.img.visibility = View.GONE
        item.progressCircular.visibility = View.VISIBLE
        putFile.addOnSuccessListener {
            if (it.task.isSuccessful){
                val downloadUrl = it.metadata?.reference?.downloadUrl
                downloadUrl?.addOnSuccessListener {
                    Picasso.get().load(it).into(item.img)
                    item.img.visibility = View.VISIBLE
                    item.progressCircular.visibility = View.GONE
                }
            }
        }
        putFile.addOnFailureListener{
            Toast.makeText(this, "Yuklashda xatolik", Toast.LENGTH_SHORT).show()
        }
    }

    private var getVideo = registerForActivityResult(ActivityResultContracts.GetContent()){
        it ?: return@registerForActivityResult

        val putFile = referenceStorage.child("${dateFormat.format(date)}video1").putFile(it)
        item.video.visibility = View.GONE
        item.progressCircular.visibility = View.VISIBLE
        putFile.addOnSuccessListener{
            if (it.task.isSuccessful){
                val doenloadUrl = it.metadata?.reference?.downloadUrl
                doenloadUrl?.addOnSuccessListener {
                    val videoView: VideoView = item.video
                    val videoUrl = it.toString()
                    val mediaController = MediaController(this)
                    mediaController.setAnchorView(videoView)
                    videoView.setMediaController(mediaController)
                    val videoUri = Uri.parse(videoUrl)
                    videoView.setVideoURI(videoUri)
                    videoView.requestFocus()
                    videoView.start()
                }
                item.progressCircular.visibility = View.GONE
                item.video.visibility = View.VISIBLE
                item.video.background = null
            }
        }
    }
}