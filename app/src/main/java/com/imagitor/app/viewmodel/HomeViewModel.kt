package com.imagitor.app.viewmodel

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val _imageUri = MutableLiveData<Uri>()
    val imageUri = _imageUri

    private val _angle = MutableLiveData<Float>()
    val angle = _angle

    fun setImage(image: Uri) {
        _imageUri.value = image
    }

    fun setRotation(angle: Float) {
        _angle.value = angle
    }
}