package com.example.crudfirebase

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase

class MahasiswaAdapter(val mCtx : Context, val layoutResId: Int, val mhsList: List<Mahasiswa> ): ArrayAdapter<Mahasiswa> (mCtx, layoutResId, mhsList){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater : LayoutInflater = LayoutInflater.from(mCtx)

        val view : View = layoutInflater.inflate(layoutResId, null)
        val tvName : TextView = view.findViewById(R.id.tv_name)
        val tvAlamat : TextView = view.findViewById(R.id.tv_alamat)
        val tvEdit : TextView = view.findViewById(R.id.tv_edit)
        val mahasiswa = mhsList[position]

        tvEdit.setOnClickListener {
            showUpdateDialog(mahasiswa)
        }

        tvName.text = mahasiswa.name
        tvAlamat.text = mahasiswa.alamat

        return view
    }

    private fun showUpdateDialog(mahasiswa: Mahasiswa) {
        val builder = AlertDialog.Builder(mCtx)
        builder.setTitle("Edit Data")

        val inflater = LayoutInflater.from(mCtx)
        val view = inflater.inflate(R.layout.update_dialog, null)

        val etName = view.findViewById<EditText>(R.id.et_name)
        val etAlamat = view.findViewById<EditText>(R.id.et_alamat)

        etName.setText(mahasiswa.name)
        etAlamat.setText(mahasiswa.alamat)

        builder.setView(view)

        builder.setPositiveButton("Update"){p0,p1 ->
            val dbMhs = FirebaseDatabase.getInstance().getReference("mahasiswa")

            val name = etName.text.toString().trim()
            val alamat = etAlamat.text.toString().trim()
            if (name.isEmpty()){
                etName.error = "Mohon Nama di isi"
                etName.requestFocus()
                return@setPositiveButton
            }

            if (alamat.isEmpty()){
                etAlamat.error = "Mohon Alamat di isi"
                etAlamat.requestFocus()
                return@setPositiveButton
            }

            val mahasiswa = Mahasiswa(mahasiswa.id, name, alamat)

            dbMhs.child(mahasiswa.id!!).setValue(mahasiswa)

            Toast.makeText(mCtx, "Data berhasil di update", Toast.LENGTH_SHORT).show()

        }

        builder.setNegativeButton("No"){p0,p1 ->

        }

        val alert = builder.create()
        alert.show()

    }

}