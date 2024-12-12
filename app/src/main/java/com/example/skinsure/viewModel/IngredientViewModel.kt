package com.example.skinsure.viewModel

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skinsure.model.IngredientsModel
import com.example.skinsure.model.historyModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class IngredientViewModel : ViewModel() {

    var _extractedText = mutableStateOf<List<String>>(emptyList())
    val extractedText get() = _extractedText

    var _ingredientDetails = MutableStateFlow<List<IngredientsModel>>(emptyList())
    val ingredientDetails: StateFlow<List<IngredientsModel>> get() = _ingredientDetails

    private var _searchResults = MutableStateFlow<List<IngredientsModel>>(emptyList())
    val searchResults: StateFlow<List<IngredientsModel>> = _searchResults

    private var _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    private val _imageBitmap = MutableStateFlow<Bitmap?>(null)
    val imageBitmap: StateFlow<Bitmap?> get() = _imageBitmap

    var _historyData = MutableStateFlow<List<historyModel>>(emptyList())
    val historyData: MutableStateFlow<List<historyModel>> get() = _historyData

    var _historyDetails = MutableStateFlow<historyModel?>(null)
    val historyDetails: MutableStateFlow<historyModel?> get() = _historyDetails

    var _isSavedResultSuccess = MutableStateFlow<Boolean>(false)
    val isSavedResultSuccess: MutableStateFlow<Boolean> get() = _isSavedResultSuccess

    val storage = FirebaseStorage.getInstance().reference

    private val db = FirebaseFirestore.getInstance()

    val tempName = mutableStateOf("")

    fun setImage(image: Bitmap) {
        _imageBitmap.value = image
    }


    fun setIngredientDetailsBackToEmpty() {
        _ingredientDetails.value = emptyList()
    }

    fun extractTextFromImage(bitmap: Bitmap) {
        _isLoading.value = true
        val textRecognizer = com.google.mlkit.vision.text.TextRecognition.getClient(
            TextRecognizerOptions.DEFAULT_OPTIONS
        )
        val inputImage = com.google.mlkit.vision.common.InputImage.fromBitmap(bitmap, 0)

        textRecognizer.process(inputImage)
            .addOnSuccessListener { visionText ->
                val ingredientsText = StringBuilder()
                for (block in visionText.textBlocks) {
                    for (line in block.lines) {
                        ingredientsText.append(line.text).append(" ")
                    }
                }

                val cleanedIngredients = cleanAndFormatIngredients(ingredientsText.toString())

                _extractedText.value = cleanedIngredients
                Log.d("ExtractedText", cleanedIngredients.joinToString(", "))
                viewModelScope.launch {
                    searchIngredientsInFirebase(cleanedIngredients)
                }
            }
            .addOnFailureListener { e ->
                _isLoading.value = false
            }
    }

    private fun cleanAndFormatIngredients(ingredientsText: String): List<String> {
        // Replace newline characters with spaces and normalize spaces
        val formattedText = ingredientsText.replace("\n", " ").trim()

        // Improved regex to capture both uppercase and lowercase words, and ingredients with slashes
        val regex =
            Regex("""([A-Za-z0-9/-]+(?:\s[A-Za-z0-9/-]+)*(?:\s\([\w\s]+\))?(?:\s[A-Za-z0-9/-]+)*)""")


        Log.d("RawTextBeforeCleaning", "Raw Text: ${ingredientsText}")
        val ingredients = regex.findAll(formattedText)
            .map { it.value.trim() }
            .filter { it.isNotEmpty() }
            .filterNot {
                it.contains(
                    "Ingredients:",
                    ignoreCase = true
                )
            }
            .distinct()
            .toList()

        Log.d("CleanedIngredients", "Cleaned Ingredients: ${ingredients.joinToString(", ")}")

        return ingredients
    }

    suspend fun searchIngredientsInFirebase(ingredientsList: List<String>) {
        viewModelScope.launch {
            _isLoading.value = true
            val results = mutableListOf<IngredientsModel>()
            val normalizedIngredientsList = ingredientsList.map { it.trim().lowercase() }

            Log.d("NormalizedIngredients", normalizedIngredientsList.joinToString(", "))

            for (ingredient in normalizedIngredientsList) {
                try {
                    val snapshot = db.collection("ingredients")
                        .whereEqualTo("IngredientName", ingredient)
                        .get()
                        .await()

                    if (snapshot.isEmpty) {
                        Log.d("FirebaseQueryResult", "No ingredients found for: $ingredient")
                    } else {
                        snapshot.documents.mapNotNull { document ->
                            val data = document.toObject(IngredientsModel::class.java)
                            if (data != null) {
                                results.add(data)
                            }
                        }
                    }
                } catch (e: Exception) {
                    Log.e("FirebaseError", "Error fetching ingredient: $ingredient", e)
                }
            }

            _ingredientDetails.value = results
            Log.d("IngredientDetails", results.toString())
            _isLoading.value = false
        }
    }


    fun search(query: String) {
        viewModelScope.launch {
            if (query.isNotBlank()) {
                val results = try {
                    db.collection("ingredients")
                        .whereGreaterThanOrEqualTo("IngredientName", query)
                        .whereLessThanOrEqualTo("IngredientName", query + "\uf8ff")
                        .get()
                        .await()
                        .documents.mapNotNull { documents ->
                            documents.toObject(IngredientsModel::class.java)
                        }
                } catch (e: Exception) {
                    emptyList()
                }
                _searchResults.value = results
            } else {
                _searchResults.value = emptyList()
            }
        }
    }

    fun saveBitmapToFile(context: Context, bitmap: Bitmap): Uri? {
        val file = File(context.cacheDir, "temp_image.png")
        return try {
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            outputStream.flush()
            outputStream.close()

            Uri.fromFile(file)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    suspend fun saveResult(uri: Uri, email: String, result: List<String>, name: String) {
        _isLoading.value = true
        val imagePath = "history/${name}_${email}"
        val image = storage.child(imagePath)
        val data = hashMapOf(
            "email" to email,
            "name" to name,
            "result" to result,
            "link" to imagePath
        )
        val history = db.collection("history").whereEqualTo("link", imagePath).get().await()

        try {
            if (history.isEmpty) {
                image.putFile(uri).await()
                db.collection("history").add(data).await()
                image.putFile(uri).await()
                Log.d("Storage", "Image uploaded successfully")
                _isLoading.value = false
                _isSavedResultSuccess.value = true
            } else {
                println("file already exists")
                _isLoading.value = false
            }
        } catch (e: Exception) {
            Log.d("Storage", "Image upload failed: ${e.message}")
            _isLoading.value = false
        }
    }

    suspend fun getHistory(email: String) {
        _isLoading.value = true
        try {
            val history = db.collection("history").whereEqualTo("email", email).get().await()

            if (history.isEmpty) {
                println("No data found")
                _historyData.value = emptyList()
            } else {
                val data = history.documents.mapNotNull { document ->
                    document.toObject(historyModel::class.java)
                }
                _historyData.value = data
            }
        } catch (e: Exception) {
            println("Error: ${e.message}")
            _historyData.value = emptyList()
        } finally {
            _isLoading.value = false
        }
    }

    fun filteredData(name: String) {
        _historyDetails.value = _historyData.value.find { it.name == name }
    }

}
