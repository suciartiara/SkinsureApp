package com.example.skinsure.model

import android.provider.ContactsContract.CommonDataKinds.Email

data class historyModel(
    val email: String = "",
    val link: String = "",
    val name: String = "",
    val result: List<String> = emptyList()
)