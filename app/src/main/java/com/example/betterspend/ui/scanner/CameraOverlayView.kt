package com.example.betterspend.ui.scanner

import android.content.Context
import android.graphics.Canvas
import android.graphics.Camera
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.View

class CameraOverlayView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs), SensorEventListener {

    private val clearPaint = Paint().apply {
        xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
    }

    private val blurPaint = Paint().apply {
        isAntiAlias = true
        color = Color.BLACK
        alpha = 100
    }

    private val rotationMatrix = FloatArray(9) { 0f }
    private val orientationAngles = FloatArray(3) { 0f }

    private val sensorManager: SensorManager =
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val rotationVectorSensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)

    private val handler = Handler(Looper.getMainLooper())
    private val updateRunnable = object : Runnable {
        override fun run() {
            invalidate()
            handler.postDelayed(this, 16) // ~60 FPS
        }
    }

    private val camera = Camera()

    init {
        if (rotationVectorSensor != null) {
            sensorManager.registerListener(this, rotationVectorSensor, SensorManager.SENSOR_DELAY_UI)
        } else {
            throw IllegalStateException("Rotation vector sensor not available on this device.")
        }
        handler.post(updateRunnable)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null) return

        if (event.sensor.type == Sensor.TYPE_ROTATION_VECTOR) {
            SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values)
            SensorManager.getOrientation(rotationMatrix, orientationAngles)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        sensorManager.unregisterListener(this)
        handler.removeCallbacks(updateRunnable)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Draw a semi-transparent overlay
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), blurPaint)

        // Define the rectangle for the barcode area
        val rectWidth = width * 0.8f
        val rectHeight = rectWidth / 2.0f
        val left = -rectWidth / 2
        val top = -rectHeight / 2
        val right = rectWidth / 2
        val bottom = rectHeight / 2

        // Save the canvas state
        canvas.save()

        // Translate canvas to center
        canvas.translate(width / 2f, height / 2f)

        // Apply 3D rotation using Camera
        val pitchDegrees = Math.toDegrees(orientationAngles[1].toDouble()).toFloat() // Pitch

        camera.save()
        camera.rotateX(-pitchDegrees/2) // Invert pitch for correct orientation
//        camera.rotateY(-yawDegrees/10)
//        camera.rotateZ(-rollDegrees/10)
        camera.applyToCanvas(canvas)
        camera.restore()

        // Draw the clear rectangle
        canvas.drawRect(left, top, right, bottom, clearPaint)

        // Optionally, draw a border around the rectangle
        val borderPaint = Paint().apply {
            color = Color.WHITE
            strokeWidth = 5f
            style = Paint.Style.STROKE
        }
        canvas.drawRect(left, top, right, bottom, borderPaint)

        // Restore the canvas state
        canvas.restore()
    }
}
