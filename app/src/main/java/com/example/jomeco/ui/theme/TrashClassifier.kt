package com.example.jomeco.ml

import com.example.jomeco.ui.theme.loadModelFile


import android.content.Context
import android.graphics.Bitmap
import org.tensorflow.lite.Interpreter
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.math.min

class TrashClassifier(context: Context) {

    private var interpreter: Interpreter
    private val inputImageWidth: Int
    private val inputImageHeight: Int
    private val modelInputSize: Int

    private val labels = listOf("plastic", "box")

    init {
        val model = loadModelFile(context)
        interpreter = Interpreter(model)

        // Assuming model input is [1, height, width, 3]
        val inputShape = interpreter.getInputTensor(0).shape()
        inputImageHeight = inputShape[1]
        inputImageWidth = inputShape[2]

        // Assuming float32 input, 4 bytes per float, 3 channels RGB
        modelInputSize = inputImageWidth * inputImageHeight * 3 * 4
    }

    fun classify(bitmap: Bitmap): String {
        val resizedBitmap = Bitmap.createScaledBitmap(bitmap, inputImageWidth, inputImageHeight, true)

        val byteBuffer = convertBitmapToByteBuffer(resizedBitmap)

        val output = Array(1) { FloatArray(labels.size) } // Output shape [1, numLabels]

        interpreter.run(byteBuffer, output)

        val confidences = output[0]
        val maxIdx = confidences.indices.maxByOrNull { confidences[it] } ?: -1

        return if (maxIdx != -1) labels[maxIdx] else "unknown"
    }

    private fun convertBitmapToByteBuffer(bitmap: Bitmap): ByteBuffer {
        val byteBuffer = ByteBuffer.allocateDirect(modelInputSize)
        byteBuffer.order(ByteOrder.nativeOrder())

        val intValues = IntArray(inputImageWidth * inputImageHeight)
        bitmap.getPixels(intValues, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)

        // Normalize input to [0,1] by dividing by 255f
        for (pixelValue in intValues) {
            val r = ((pixelValue shr 16) and 0xFF) / 255.0f
            val g = ((pixelValue shr 8) and 0xFF) / 255.0f
            val b = (pixelValue and 0xFF) / 255.0f

            byteBuffer.putFloat(r)
            byteBuffer.putFloat(g)
            byteBuffer.putFloat(b)
        }

        return byteBuffer
    }
}
