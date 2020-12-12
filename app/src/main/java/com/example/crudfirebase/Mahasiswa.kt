package com.example.crudfirebase

data class Mahasiswa (
    val id : String,
    val name : String,
    val alamat : String
    ){
    constructor(): this("","",""){

    }
}