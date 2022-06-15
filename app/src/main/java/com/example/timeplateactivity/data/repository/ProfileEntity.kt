package com.example.timeplateactivity.data.repository

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Profile(
    @PrimaryKey(autoGenerate = true)
    var uid: Int = 0,
    @ColumnInfo(name = "name") val profileName: String?,
    @ColumnInfo(name = "roundTime") val roundTime: Long?,
    @ColumnInfo(name = "restTime") val restTime: Long?,
    @ColumnInfo(name = "roundAmount") val roundAmount: Int?,
    @ColumnInfo(name = "isDeletable") val isDeletable: Boolean
)
