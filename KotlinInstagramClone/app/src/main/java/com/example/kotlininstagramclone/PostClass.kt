package com.example.kotlininstagramclone

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.custom_view.view.*

class PostClass(
    private val useremail: ArrayList<String>,
    private val user_images: ArrayList<String>,
    private val userComments: ArrayList<String>,
    private val context: Activity
) : ArrayAdapter<String>(context, R.layout.custom_view, useremail) {


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val layoutInflater = context.layoutInflater

        val custom_view = layoutInflater.inflate(R.layout.custom_view, null, true)

        custom_view.customUserName.text = useremail[position]
        custom_view.customCommentText.text = userComments[position]

        //Picasso.get().load("http://i.imgur.com/DvpvklR.png").into(custom_view.customImageView);
        Picasso.get().load(user_images[position]).into(custom_view.customImageView);

        return custom_view
    }
}