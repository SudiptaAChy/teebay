package com.teebay.appname.features.myProduct.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "categories")
data class Category(
    @PrimaryKey val label: String?,
    val value: String?,
    var isSelected: Boolean = false
)
