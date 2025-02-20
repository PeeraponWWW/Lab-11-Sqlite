package com.example.lab_11_sec2version2

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Employee(
    val emp_id: String,
    val emp_name: String,
    val emp_salary: Int,
    val emp_position: String,
):Parcelable
