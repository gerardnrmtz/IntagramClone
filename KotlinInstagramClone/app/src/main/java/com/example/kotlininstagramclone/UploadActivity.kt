package com.example.kotlininstagramclone

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_upload.*
import java.lang.Exception
import java.util.*

class UploadActivity : AppCompatActivity() {

    var selected: Uri? = null
    var mAuth: FirebaseAuth? = null
    var mAuthListener: FirebaseAuth.AuthStateListener? = null
    var firebaseDatabase: FirebaseDatabase? = null
    var myRef: DatabaseReference? = null
    var mStoreRef: StorageReference? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload)


        mAuth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        myRef = firebaseDatabase!!.reference
        mStoreRef = FirebaseStorage.getInstance().reference


    }

    fun upload(view: View) {

        val uuid = UUID.randomUUID()
        val imageName = "images/$uuid.jpg"
        val storagerefeces = mStoreRef!!.child(imageName)
        var downloadUrl = ""

        storagerefeces.putFile(selected!!).addOnSuccessListener { taskSnapshot ->

            storagerefeces.downloadUrl.addOnCompleteListener() { taskSnapshot ->

                var url = taskSnapshot.result
                downloadUrl = url.toString()

                val user = mAuth!!.currentUser
                val userEmail = user!!.email
                val userComment = commentText.text.toString()

                val uuid = UUID.randomUUID()
                val uuidString = uuid.toString()

                myRef!!.child("Posts").child(uuidString).child("useremail").setValue(userEmail)
                myRef!!.child("Posts").child(uuidString).child("comment").setValue(userComment)
                myRef!!.child("Posts").child(uuidString).child("downloadurl").setValue(downloadUrl)
            }.addOnFailureListener { exception ->

                if (exception != null) {
                    Toast.makeText(applicationContext, exception.localizedMessage, Toast.LENGTH_LONG).show()
                }
            }
            //val downloadUrl = taskSnapshot.storage.downloadUrl.uri


        }.addOnFailureListener { exception ->
            if (exception != null) {
                Toast.makeText(applicationContext, exception.localizedMessage, Toast.LENGTH_LONG).show()
            }
        }.addOnCompleteListener { task ->
            if (task.isComplete) {
                Toast.makeText(applicationContext, "Post Shared", Toast.LENGTH_LONG).show()

                //intent feed activity
                val intent = Intent(applicationContext, FeedActivity::class.java)
                startActivity(intent)
            }
        }
    }

    fun selectedImage(view: View) {

        if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1)
        } else {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, 2)
        }


    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        if (requestCode == 1) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(intent, 2)
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 2 && resultCode == Activity.RESULT_OK && data != null) {
            selected = data.data
        }
        try {
            val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, selected)
            imageView.setImageBitmap(bitmap)


        } catch (e: Exception) {

        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}
