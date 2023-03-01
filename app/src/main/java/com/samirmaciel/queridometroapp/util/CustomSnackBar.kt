package com.samirmaciel.queridometroapp.util

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.samirmaciel.queridometroapp.R

object CustomSnackBar {


    fun getNotification(view: View, context: Context,  message: String, isPositiveMessage: Boolean){
        val snackbar = Snackbar.make(view, "", Snackbar.LENGTH_LONG)
        val params = snackbar.view.layoutParams as FrameLayout.LayoutParams
        params.gravity = Gravity.TOP
        params.height = 200
        snackbar.view.layoutParams = params

        val layout = snackbar.view as Snackbar.SnackbarLayout
        snackbar.view.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent))
        val customLayout = LayoutInflater.from(context).inflate(R.layout.notification_snackbar_layout, null)
        val imageView = customLayout.findViewById<ImageView>(R.id.snackbar_image)

        if(isPositiveMessage){
            imageView.setImageResource(R.drawable.emojihappy)
        }else{
            imageView.setImageResource(R.drawable.emojifurious)
        }

        val textView = customLayout.findViewById<TextView>(R.id.snackbar_text)
        textView.text = message
        layout.setPadding(0,0,0,0);
        layout.addView(customLayout, 0)
        snackbar.show()

    }
}