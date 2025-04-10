package com.jozefv.whatsnew.core.presentation.util

import android.content.Context
import android.content.Intent

fun Context.shareLink(articleLink: String){
    val sendIntent = Intent().apply {
        action = Intent.ACTION_SEND
        // What we want to share
        putExtra(Intent.EXTRA_TEXT,articleLink)
        type = "text/plain"
    }
    val shareIntent = Intent.createChooser(sendIntent,null)
    // Check if there are apps which can satisfy our intent
    if(shareIntent.resolveActivity(packageManager) != null){
        startActivity(shareIntent)
    }
}