package com.example.crudfirebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import com.google.firebase.database.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var etName : EditText
    private lateinit var etAlamat : EditText
    private lateinit var btnSave : Button

    private lateinit var listMhs : ListView
    private lateinit var ref : DatabaseReference
    private lateinit var mhsList: MutableList<Mahasiswa>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ref = FirebaseDatabase.getInstance().getReference("Mahasiswa")

        etName = findViewById(R.id.et_name)
        etAlamat = findViewById(R.id.et_alamat)
        btnSave = findViewById(R.id.btn_save)
        listMhs = findViewById(R.id.lv_mhs)
        btnSave.setOnClickListener(this)

        mhsList = mutableListOf()

        ref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()){
                    mhsList.clear()
                    for (h in p0.children){
                        val mahasiswa = h.getValue(Mahasiswa::class.java)
                        if (mahasiswa !=null){
                            mhsList.add(mahasiswa)
                        }
                    }

                    val adapter = MahasiswaAdapter(this@MainActivity, R.layout.item_mhs, mhsList)
                    listMhs.adapter = adapter
                }
            }

            override fun onCancelled(p0: DatabaseError) {
            }

        })
    }

    override fun onClick(v: View?) {
        saveData()
    }

    private fun saveData() {
        val name = etName.text.toString().trim()
        val alamat = etAlamat.text.toString().trim()

        if (name.isEmpty()){
            etName.error = "Isi Nama!"
            return
        }

        if (alamat.isEmpty()){
            etAlamat.error = "Isi Alamat!"
            return
        }

        val mhsId = ref.push().key

        val mhs = Mahasiswa(mhsId!!,name, alamat)

        if (mhsId !=null){
            ref.child(mhsId).setValue(mhs).addOnCompleteListener {
                Toast.makeText(applicationContext, "Data berhasil ditambahkan", Toast.LENGTH_SHORT).show()
            }
        }
    }


}