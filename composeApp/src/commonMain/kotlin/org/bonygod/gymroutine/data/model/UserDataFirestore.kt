package org.bonygod.gymroutine.data.model

data class UserDataFirestore (
    var userName: String,
    var email: String,
    var weight: Int,
    var height: Int,
    var age: Int,
    var gender: String
)