package com.kostlin.timeplateactivity.data.repository

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ProfileDao {
    @Query("SELECT * FROM profile")
    fun getAll(): List<Profile>

    @Query("SELECT * FROM profile WHERE uid IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<Profile>



    @Insert
    fun insertAll(vararg users: Profile)

    @Delete
    fun delete(user: Profile)
}