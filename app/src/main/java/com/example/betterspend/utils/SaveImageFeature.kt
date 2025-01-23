package com.example.betterspend.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.File
import java.io.FileOutputStream
import org.opencv.android.Utils
import org.opencv.core.*
import java.io.InputStream


fun saveImageToInternalStorage(context: Context, uri: Uri): Uri? {
    return try {
        val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
        val file = File(context.filesDir, "profile_picture.jpg") // Save with a fixed name
        val outputStream = FileOutputStream(file)

        inputStream?.use { input ->
            outputStream.use { output ->
                input.copyTo(output)
            }
        }

        Uri.fromFile(file) // Return the saved file's URI
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun processImageRedChannel(context: Context, inputUri: Uri): Uri? {
    return try {
        // Load the image from internal storage as a Mat object
        val inputStream = context.contentResolver.openInputStream(inputUri)
        val bitmap = BitmapFactory.decodeStream(inputStream)
        inputStream?.close()

        val mat = Mat()
        Utils.bitmapToMat(bitmap, mat)

        // Split into channels
        val channels = ArrayList<Mat>(3)
        Core.split(mat, channels)

        // Keep only the red channel (index 2 in OpenCV's BGR format)
        val redChannel = channels[2]

        // Merge the red channel back with zeros for G and B
        val zeroChannel = Mat.zeros(redChannel.size(), CvType.CV_8UC1)
        val redOnlyMat = ArrayList<Mat>(3)
        redOnlyMat.add(zeroChannel) // Blue
        redOnlyMat.add(zeroChannel) // Green
        redOnlyMat.add(redChannel)  // Red

        val outputMat = Mat()
        Core.merge(redOnlyMat, outputMat)

        // Save the processed image back to internal storage
        val processedBitmap = Bitmap.createBitmap(
            outputMat.cols(),
            outputMat.rows(),
            Bitmap.Config.ARGB_8888
        )
        Utils.matToBitmap(outputMat, processedBitmap)

        val processedFile = File(context.filesDir, "processed_image.jpg")
        val outputStream = FileOutputStream(processedFile)
        processedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        outputStream.close()

        // Return the URI of the processed image
        Uri.fromFile(processedFile)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

