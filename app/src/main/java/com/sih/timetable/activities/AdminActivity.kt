package com.sih.timetable.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.sih.timetable.databinding.ActivityAdminBinding

class AdminActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminBinding
    private var imagePath: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)
        selectImage()
        uploadImage()
    }

    private fun selectImage() {
        binding.selectContainer.setOnClickListener {

            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            startActivityForResult(intent, 33)
        }
    }

    private fun uploadImage() {
        binding.btnUpload.setOnClickListener {
            if (imagePath == null) {
                Toast.makeText(this, "Image not selected!", Toast.LENGTH_SHORT).show()
            } else {
                val currentTimestamp = System.currentTimeMillis().toString()
                val reference: StorageReference =
                    FirebaseStorage.getInstance().reference.child("mess").child(currentTimestamp)
                reference.putFile(imagePath!!).addOnSuccessListener {

                    reference.downloadUrl.addOnSuccessListener {
                        FirebaseDatabase.getInstance().reference.child("Database").child("Links")
                            .child("messURL").setValue(it.toString()).addOnSuccessListener {
                                Toast.makeText(applicationContext, "URL updated", Toast.LENGTH_SHORT).show()
                            }
                    }.addOnFailureListener {
                        Log.d("DOWN_URL", "failed")
                    }
                    Toast.makeText(applicationContext, "Image uploaded", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Log.e("NOT_UP",it.printStackTrace().toString())
                    Toast.makeText(applicationContext, "Image not uploaded", Toast.LENGTH_SHORT)
                        .show()
                }
            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && data!!.data != null) {
            imagePath = data.data
            binding.zvSelected.setImageURI(imagePath)
        } else {
            Toast.makeText(this, "Picture not selected", Toast.LENGTH_SHORT).show()
        }
    }
}