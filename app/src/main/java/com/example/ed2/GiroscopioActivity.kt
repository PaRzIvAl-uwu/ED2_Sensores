package com.example.ed2

import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class GiroscopioActivity : AppCompatActivity(), SensorEventListener {
    private lateinit var tvGiro : TextView
    private lateinit var tvValor : TextView
    private lateinit var layout : View
    private lateinit var sensorManager : SensorManager

    private var sensorGiro : Sensor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_giroscopio)

        tvGiro = findViewById(R.id.tvGiro)
        tvValor = findViewById(R.id.tvValor)
        layout = findViewById(R.id.main)

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

        sensorGiro = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)

        if (sensorGiro == null) {
            tvGiro.text = "No tienes giroscopio :("
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }


    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_GYROSCOPE) {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]

            tvValor.text = "X: %.2f, Y: %.2f, Z: %.2f,".format(x,y,z)

            if (Math.abs(x) >= 2.0f) {
                tvGiro.setTextColor(Color.WHITE)
                tvValor.setTextColor(Color.WHITE)
                tvGiro.text = "Giro fuerte del eje x"
                layout.setBackgroundColor(Color.GREEN)
            }
            if (Math.abs(y) >= 2.0f) {
                tvGiro.text = "Giro fuerte del eje y"
                tvGiro.setTextColor(Color.BLACK)
                tvValor.setTextColor(Color.BLACK)
                layout.setBackgroundColor(Color.YELLOW)
            }
            if (Math.abs(z) >= 2.0f) {
                tvGiro.setTextColor(Color.WHITE)
                tvValor.setTextColor(Color.WHITE)
                tvGiro.text = "Giro fuerte del eje z"
                layout.setBackgroundColor(Color.BLUE)
            }
        }
    }


    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    override fun onResume() {
        super.onResume()

        sensorGiro?.let {
            sensorManager.registerListener(
                this,
                it,
                SensorManager.SENSOR_DELAY_UI
            )
        }
    }
}