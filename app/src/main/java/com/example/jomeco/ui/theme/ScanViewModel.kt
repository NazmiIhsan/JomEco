package com.example.jomeco.ui.theme

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ScanViewModel : ViewModel() {
    private val _bitmap = MutableStateFlow<Bitmap?>(null)
    val bitmap: StateFlow<Bitmap?> = _bitmap

    fun setBitmap(bmp: Bitmap) {
        _bitmap.value = bmp
    }

    fun clearBitmap() {
        _bitmap.value = null
    }
}
