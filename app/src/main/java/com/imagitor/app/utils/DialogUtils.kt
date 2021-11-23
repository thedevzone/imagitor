package com.imagitor.app.utils

import android.app.AlertDialog
import android.content.Context
import com.imagitor.app.R

object DialogUtils {

    fun showGeneralDialog(
        context: Context,
        message: String,
        positiveText: String,
        negativeText: String,
        onClick: () -> Unit
    ) {
        val dialog = AlertDialog.Builder(context)
            .setTitle(context.getString(R.string.app_name))
            .setMessage(message)
            .setCancelable(false)
            .setPositiveButton(positiveText) { dialog, _ ->
                onClick.invoke()
                dialog.dismiss()
            }
            .setNegativeButton(negativeText) { dialog, _ ->
                dialog.dismiss()
            }.create()

        dialog.show()
    }

}