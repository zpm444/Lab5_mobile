package com.example.lab_5

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val buttonGetContacts: Button = findViewById(R.id.get_image)
        buttonGetContacts.setOnClickListener { tryToGetImage() }
    }

    private fun tryToGetImage() {
        val checkResult = ActivityCompat.checkSelfPermission(
            this,
            android.Manifest.permission.READ_MEDIA_IMAGES
        )

        if (checkResult == PackageManager.PERMISSION_GRANTED) {
            getImage()
            return
        }

        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.READ_MEDIA_IMAGES),
            REQUEST_MEDIA_IMAGES_PERMISSION
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_MEDIA_IMAGES_PERMISSION) {
            when {
                grantResults[0] == PackageManager.PERMISSION_GRANTED -> {
                    getImage()
                }
                !shouldShowRequestPermissionRationale(android.Manifest.permission.READ_MEDIA_IMAGES) ->
                {
                    showRationaleDialog()
                }
                else -> {
                }
            }
        }
    }

    private fun getImage(){
        findViewById<TextView>(R.id.textView_errors).setText("Перед созданием intent")

        val intent = Intent()
        intent.setType("image/*")
        intent.setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(
            Intent.createChooser(
                intent,
                "Select Picture"
            ), SELECT_PICTURE
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        findViewById<TextView>(R.id.textView_errors).setText("Обработка выбора")

        if (resultCode == Activity.RESULT_OK) {
            val selectedMediaUri: Uri? = data?.data
            setImage(selectedMediaUri)
        }
    }

    private fun setImage(imageUri : Uri?){
        findViewById<TextView>(R.id.textView_errors).setText("Установка фотки")
        findViewById<ImageView>(R.id.imageView_preview).setImageURI(imageUri)//setImageBitmap(b)
    }

    private fun showRationaleDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Требуется разрешение на просмотр фотографий")
            .setMessage("Это необходимо для загрузки фотографии из памяти в приложение и вывод")
            .show()
    }

    companion object {
        private const val REQUEST_MEDIA_IMAGES_PERMISSION = 0
        private const val SELECT_PICTURE = 1
    }
}