package com.example.jomeco.ui.theme

import android.content.Context
import org.tensorflow.lite.Interpreter
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

fun loadModelFile(context: Context): MappedByteBuffer {
    val fileDescriptor = context.assets.openFd("model.tflite")
    val inputStream = fileDescriptor.createInputStream()
    val fileChannel = inputStream.channel
    val startOffset = fileDescriptor.startOffset
    val declaredLength = fileDescriptor.declaredLength
    return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
}


