package com.imagitor.app.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.fondesa.kpermissions.extension.*
import com.imagitor.app.R
import com.imagitor.app.databinding.ActivityHomeBinding
import com.imagitor.app.model.UndoOperation
import com.imagitor.app.utils.*
import com.imagitor.app.viewmodel.HomeViewModel


class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private val viewModel: HomeViewModel by viewModels()

    private val cameraPermission by lazyFast {
        permissionsBuilder(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ).build()
    }

    private val galleryPermission by lazyFast {
        permissionsBuilder(Manifest.permission.READ_EXTERNAL_STORAGE).build()
    }

    private val gallery = registerForActivityResult(ActivityResultContracts.GetContent()) {
        it?.let { navigateToEditScreen(it) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        setContentView(binding.root)
        with(binding) {
            buttonTakeSelfie.setOnClickListener { pickSelfie() }
            buttonGallery.setOnClickListener { pickImageFromGallery() }
        }
    }

    private fun navigateToEditScreen(uri: Uri) {
        editImageActivity.launch(
            Intent(this, EditActivity::class.java).putExtra(
                Constants.IMAGE,
                uri.toString()
            )
        )
    }

    private val captureImageActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val image = it.data?.extras?.getString(Constants.IMAGE)
                navigateToEditScreen(Uri.parse(image))
            }
        }

    private val editImageActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                it.data?.extras?.getParcelable<UndoOperation>(Constants.IMAGE)?.let { image ->
                    viewModel.setImage(image.previousImage)
                    viewModel.setRotation(image.angle)
                }
            }
        }

    private fun pickSelfie() {
        cameraPermission
            .onAccepted {
                captureImageActivity.launch(Intent(this, CaptureSelfieActivity::class.java))
            }
            .onDenied {
                showMessage(this, getString(R.string.message_denied))
            }
            .onPermanentlyDenied {
                DialogUtils.showGeneralDialog(
                    this,
                    getString(R.string.message_camera_permission),
                    getString(R.string.title_settings),
                    getString(R.string.button_cancel)
                ) {
                    openPermissionSettings()
                }
            }
            .onShouldShowRationale { _, permissionNonce ->
                DialogUtils.showGeneralDialog(
                    this,
                    getString(R.string.message_camera_permission),
                    getString(R.string.button_accept),
                    getString(R.string.button_reject)
                ) {
                    permissionNonce.use()
                }
            }
            .send()
    }

    private fun pickImageFromGallery() {
        galleryPermission
            .onAccepted {
                gallery.launch("image/*")
            }.onDenied {
                showMessage(this, getString(R.string.message_denied))
            }.onPermanentlyDenied {
                DialogUtils.showGeneralDialog(
                    this,
                    getString(R.string.message_gallery_permission),
                    getString(R.string.title_settings),
                    getString(R.string.button_cancel)
                ) {
                    openPermissionSettings()
                }
            }
            .onShouldShowRationale { _, permissionNonce ->
                DialogUtils.showGeneralDialog(
                    this,
                    getString(R.string.message_gallery_permission),
                    getString(R.string.button_accept),
                    getString(R.string.button_reject)
                ) {
                    permissionNonce.use()
                }
            }
            .send()
    }
}