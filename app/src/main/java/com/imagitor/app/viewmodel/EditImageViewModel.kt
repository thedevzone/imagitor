package com.imagitor.app.viewmodel

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.imagitor.app.model.UndoOperation
import com.imagitor.app.utils.ImageUtils


class EditImageViewModel : ViewModel() {

    private val _imageUri = MutableLiveData<Uri>()
    val imageUri = _imageUri

    private val _angle = MutableLiveData<Float>()
    val angle = _angle

    internal val toastMessage = MutableLiveData<String>()

    private var undoOperations = ArrayDeque<UndoOperation>()

    fun getRotation(): Float = _angle.value ?: 0f

    fun resetAngle() {
        _angle.value = 0f
    }

    fun setImage(image: Uri) {
        _imageUri.value = image
    }

    fun getImage(): Uri = _imageUri.value ?: Uri.EMPTY

    fun addUndoOperation() {
        undoOperations.add(UndoOperation(getImage(), getRotation()))
    }

    fun getFinalImage(): UndoOperation = UndoOperation(getImage(), getRotation())

    fun undo() {
        if (undoOperations.isNotEmpty()) {
            val operation = undoOperations.removeLast()
            _angle.value = operation.angle
            _imageUri.value = operation.previousImage
        } else
            toastMessage.value = "No operation performed!"
    }

    fun rotate() {
        addUndoOperation()
        _angle.value = ImageUtils.rotate(getRotation())
    }

}