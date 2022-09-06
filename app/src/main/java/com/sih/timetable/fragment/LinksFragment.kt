package com.sih.timetable.fragment

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sih.timetable.databinding.FragmentLinksBinding
import com.sih.timetable.dialogs.AdminLoginDialog

class LinksFragment : Fragment() {
    private lateinit var binding: FragmentLinksBinding
    private var clicked = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLinksBinding.inflate(layoutInflater)
        adminClicked()
        buttonClicks()
        imageClick()
        downloadClick()
        return binding.root
    }

    private fun downloadClick() {
        binding.ivDownload.setOnClickListener{
            val builder = AlertDialog.Builder(requireActivity())
            builder.setMessage("Download latest version of the app?")
                .setPositiveButton("Yes") { dialog: DialogInterface?, id: Int ->
                    loadLink(binding.ivDownload,"appLink")
                }
                .setNegativeButton("No") { dialog: DialogInterface, id: Int -> dialog.cancel() }
            val alert = builder.create()
            alert.show()
        }

    }

    private fun imageClick() {
        binding.civImage.setOnClickListener {
            if (clicked == 0)
                Toast.makeText(activity, "Kyu click kiya?", Toast.LENGTH_LONG).show()
            else if (clicked == 1)
                Toast.makeText(activity, "Kuch nahi hota click karke :/", Toast.LENGTH_LONG).show()
            else if (clicked == 2)
                Toast.makeText(activity, "Mat maan, sahi me kuch nahi hota", Toast.LENGTH_LONG)
                    .show()
            else if (clicked == 3)
                Toast.makeText(
                    activity,
                    "LOL me nahi likh raha aur ye if else code. bye",
                    Toast.LENGTH_LONG
                ).show()
            else
                Toast.makeText(
                    activity,
                    "\uD83D\uDD2A",
                    Toast.LENGTH_LONG
                ).show()
            clicked++
        }
    }

    private fun buttonClicks() {
        loadLink(binding.clSyllabus, "syllabusURL")
        loadLink(binding.clBooks, "booksURL")
        loadLink(binding.clPrn, "prnURL")
        loadLink(binding.clGithub, "githubURL")
    }

    private fun loadLink(view: View, type: String) {
        view.setOnClickListener {
            FirebaseDatabase.getInstance().reference.child("Database").child("Links")
                .child(type).addValueEventListener(object :
                    ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val link = snapshot.getValue(String::class.java).toString()
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                        startActivity(intent)
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                })


        }
    }

    private fun adminClicked() {
        binding.ivAdmin.setOnClickListener {
            val dialog = AdminLoginDialog(activity)
            dialog.show(parentFragmentManager, "")
        }
    }
}