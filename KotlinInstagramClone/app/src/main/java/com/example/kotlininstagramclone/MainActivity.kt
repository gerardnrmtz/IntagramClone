package com.example.kotlininstagramclone

import android.app.Application
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    var mAuth: FirebaseAuth? = null
    var mAuthListener: FirebaseAuth.AuthStateListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuth = FirebaseAuth.getInstance()
        mAuthListener = FirebaseAuth.AuthStateListener { }


    }

    fun signIn(view: View) {
        mAuth!!.signInWithEmailAndPassword(emailText.text.toString(), passwordText.text.toString())
            .addOnCompleteListener {

                    task ->
                if (task.isSuccessful) {
                    val intent = Intent(applicationContext, FeedActivity::class.java)

                    startActivity(intent)

                }

            }.addOnFailureListener { exception ->

                if (exception != null) {
                    Toast.makeText(this, exception.localizedMessage.toString(), Toast.LENGTH_LONG).show()
                }
            }
    }

    fun signup(view: View) {
        mAuth!!.createUserWithEmailAndPassword(emailText.text.toString(), passwordText.text.toString())
            .addOnCompleteListener {

                    task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Success", Toast.LENGTH_LONG).show()
                    val intent = Intent(applicationContext, FeedActivity::class.java)

                    startActivity(intent)
                }

            }.addOnFailureListener { exception ->

                if (exception != null) {
                    Toast.makeText(this, exception.localizedMessage.toString(), Toast.LENGTH_LONG).show()
                }
            }
    }
}
