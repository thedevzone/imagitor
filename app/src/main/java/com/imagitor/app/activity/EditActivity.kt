package com.imagitor.app.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.imagitor.app.R
import com.imagitor.app.databinding.ActivityEditBinding
import com.imagitor.app.utils.Constants
import com.imagitor.app.utils.ImageUtils
import com.imagitor.app.utils.showMessage
import com.imagitor.app.utils.toUri
import com.imagitor.app.viewmodel.EditImageViewModel


class EditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditBinding
    private val viewModel: EditImageViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        setContentView(binding.root)
        getDataFromIntent()
        addToastObserver()
    }

    private fun addToastObserver() {
        viewModel.toastMessage.observe(this, {
            it?.let { msg -> showMessage(this, msg) }
        })
    }

    private fun getDataFromIntent() {
        val picUri = Uri.parse(intent.getStringExtra(Constants.IMAGE))
        if (picUri != null) {
            viewModel.setImage(picUri)
            setListeners()
        } else
            showMessage(this, getString(R.string.something_went_wrong))
    }

    private fun setListeners() {
        with(binding) {
            buttonCrop.setOnClickListener { crop() }
            buttonSave.setOnClickListener { save() }
        }
    }

    private fun save() {
        setResult(
            Activity.RESULT_OK, Intent().putExtra(Constants.IMAGE, viewModel.getFinalImage())
        )
        finish()
    }

    private fun crop() {
        val rotatedUri = ImageUtils.rotateImage(this, viewModel.getImage(), viewModel.getRotation())
        cropIntentResult.launch(ImageUtils.getCropIntent(rotatedUri))
    }

    private val cropIntentResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                viewModel.addUndoOperation()
                val bitMap = it.data?.extras?.getParcelable<Bitmap>("data")
                viewModel.resetAngle()
                viewModel.setImage(bitMap?.toUri(this) ?: Uri.EMPTY)
            }
        }
}