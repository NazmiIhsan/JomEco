package com.example.jomeco.ui.theme

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.Image
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.jomeco.ml.TrashClassifier
import java.nio.ByteBuffer

@OptIn(ExperimentalGetImage::class)
@Composable
fun Scan(navController: NavController, scanViewModel: ScanViewModel) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val previewView = remember { PreviewView(context) }

    val cameraPermission = Manifest.permission.CAMERA
    val permissionGranted = remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                cameraPermission
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        permissionGranted.value = isGranted
    }

    LaunchedEffect(Unit) {
        if (!permissionGranted.value) {
            permissionLauncher.launch(cameraPermission)
        }
    }

    if (permissionGranted.value) {
        var latestBitmap by remember { mutableStateOf<Bitmap?>(null) }

        Box(modifier = Modifier.fillMaxSize()) {

            AndroidView(factory = { previewView }, modifier = Modifier.fillMaxSize())



            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close Scanner",
                    tint = Color.White
                )
            }

            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
                    .padding(bottom = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
//                Button(onClick = {
////                    latestBitmap?.let { bitmap ->
////                        val classifier = TrashClassifier(context)
////                        val result = classifier.classify(bitmap)
////
////                        val binType = when (result.lowercase()) {
////                            "plastic" -> "Blue Bin"
////                            "box" -> "Brown Bin"
////                            else -> "Unknown"
////                        }
////
////                        val isDangerous = false
////
////                        navController.navigate("scanresult/${result}/${binType}/${isDangerous}")
////                    }
////                }) {
////                    Text("Take Photo")
////                }

                // Inside the Take Photo button:
                Button(onClick = {
                    latestBitmap?.let { bitmap ->

                        val classifier = TrashClassifier(context)
                        val result = classifier.classify(bitmap)

                        val binType = when (result.lowercase()) {
                            "plastic" -> "Orange Bin"
                            "box" -> "Blue Bin"
                            else -> "Unknown"
                        }
                        val isDangerous = false

                        scanViewModel.setBitmap(bitmap)

                        navController.navigate("scanresult/${result}/${binType}/${isDangerous}")
                    }
                }) {
                    Text("Take Photo")
                }



            }

            LaunchedEffect(true) {
                val cameraProvider = ProcessCameraProvider.getInstance(context).get()

                val preview = Preview.Builder().build().also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }

                val analysis = ImageAnalysis.Builder().build().also {
                    it.setAnalyzer(ContextCompat.getMainExecutor(context)) { imageProxy ->
                        val mediaImage = imageProxy.image
                        if (mediaImage != null) {
                            val bitmap = imageProxyToBitmap(imageProxy)
                            latestBitmap = bitmap
                            imageProxy.close()
                        } else {
                            imageProxy.close()
                        }
                    }
                }

                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    CameraSelector.DEFAULT_BACK_CAMERA,
                    preview,
                    analysis
                )
            }
        }
    } else {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Camera permission is required to scan.")
        }
    }
}

/**
 * Convert ImageProxy (YUV_420_888) to Bitmap, with rotation.
 * This is a simple implementation using ByteBuffer conversion.
 */
@OptIn(ExperimentalGetImage::class)
fun imageProxyToBitmap(imageProxy: ImageProxy): Bitmap {
    val image = imageProxy.image ?: throw IllegalStateException("Image is null")

    // Get YUV planes
    val yBuffer = image.planes[0].buffer // Y
    val uBuffer = image.planes[1].buffer // U
    val vBuffer = image.planes[2].buffer // V

    val ySize = yBuffer.remaining()
    val uSize = uBuffer.remaining()
    val vSize = vBuffer.remaining()

    val nv21 = ByteArray(ySize + uSize + vSize)

    yBuffer.get(nv21, 0, ySize)
    vBuffer.get(nv21, ySize, vSize) // V before U
    uBuffer.get(nv21, ySize + vSize, uSize)

    // Convert NV21 byte array to Bitmap
    val yuvImage = android.graphics.YuvImage(
        nv21,
        android.graphics.ImageFormat.NV21,
        image.width,
        image.height,
        null
    )

    val out = java.io.ByteArrayOutputStream()
    yuvImage.compressToJpeg(android.graphics.Rect(0, 0, image.width, image.height), 100, out)
    val imageBytes = out.toByteArray()

    var bitmap = android.graphics.BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)

    // Rotate bitmap if needed
    val rotationDegrees = imageProxy.imageInfo.rotationDegrees
    if (rotationDegrees != 0) {
        val matrix = Matrix()
        matrix.postRotate(rotationDegrees.toFloat())
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }
    return bitmap
}
