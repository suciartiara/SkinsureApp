package com.example.skinsure.viewModel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skinsure.model.IngredientsModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class IngredientsDetailViewModel : ViewModel() {
    val db = FirebaseFirestore.getInstance()
    var _IngredientsDetail = MutableStateFlow(IngredientsModel())
    val IngredientsDetail: StateFlow<IngredientsModel> get() = _IngredientsDetail
    val tempName = mutableStateOf("")

    fun getIngredientDetail(name: String) {
        viewModelScope.launch {
            val ref = db.collection("ingredients")
                .whereEqualTo("IngredientName", name)
                .get()
                .await()

            if (!ref.isEmpty) {
                val ingredient = ref.documents.firstOrNull()?.toObject(IngredientsModel::class.java)
                ingredient?.let {
                    _IngredientsDetail.value = it
                }
            }
        }
    }
}