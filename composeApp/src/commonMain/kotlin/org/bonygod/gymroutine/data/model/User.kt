package org.bonygod.gymroutine.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User (
    @PrimaryKey
    val id: String,
    val email: String,
    val displayName: String,
    val token: String
)