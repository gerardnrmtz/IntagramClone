package com.example.kotlininstagramclone

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*

class FeedActivity : AppCompatActivity() {

    var useremailfromFB: ArrayList<String> = ArrayList<String>()
    var userImagesfromFB: ArrayList<String> = ArrayList<String>()
    var userCommentsfromFB: ArrayList<String> = ArrayList<String>()

    var firebaseDatabase: FirebaseDatabase? = null
    var myRef: DatabaseReference? = null
    var adapter: PostClass? = null
    var listView: ListView? = null

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        var menuInflater = menuInflater
        menuInflater.inflate(R.menu.add_post, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item?.itemId) {
            R.id.add_post -> {
                val intent = Intent(applicationContext, UploadActivity::class.java)
                startActivity(intent)


            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)



        listView = findViewById(R.id.listView)
        firebaseDatabase = FirebaseDatabase.getInstance()
        myRef = firebaseDatabase!!.getReference()
        adapter = PostClass(useremailfromFB, userImagesfromFB, userCommentsfromFB, this)

        listView?.adapter = adapter

        getdataFromFirebase()


    }

    fun getdataFromFirebase() {
        val needReference = firebaseDatabase!!.getReference("Posts")

        needReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {

                adapter!!.clear()
                userImagesfromFB.clear()
                userCommentsfromFB.clear()
                useremailfromFB.clear()

                for (snapshot in p0.children) {

                    val hasmap = snapshot.value as HashMap<String, String>
                    if (hasmap.size > 0) {
                        val email = hasmap["useremail"]
                        val comment = hasmap["comment"]
                        val image = hasmap["downloadurl"]

                        if (email != null) {
                            useremailfromFB.add(email)
                        }
                        if (comment != null) {
                            userCommentsfromFB.add(comment)
                        }
                        if (image != null) {
                            userImagesfromFB.add(image)
                        }
                        adapter!!.notifyDataSetChanged()
                    }


                }


            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }


        })


    }
}
