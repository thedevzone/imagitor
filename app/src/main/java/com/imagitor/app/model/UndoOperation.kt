package com.imagitor.app.model

import android.net.Uri
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UndoOperation(
    val previousImage: Uri,
    val angle: Float
) : Parcelable
