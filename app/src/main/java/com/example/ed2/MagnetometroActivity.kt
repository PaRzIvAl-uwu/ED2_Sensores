package com.example.ed2

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.graphics.Color
import android.widget.ImageView

class MagnetometroActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var layout : View

    private lateinit var tvTitulo : TextView
    private lateinit var tvValores : TextView
    private lateinit var tvEstado : TextView

    private lateinit var ivIman: ImageView

    //crear un atributo que nos permita manejar el sensor
    private lateinit var sensorManager : SensorManager

    //atributon que permite usar el sensor
    private var sensorMagnetico : Sensor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_magnetometro)

        layout = findViewById(R.id.main)
        tvTitulo = findViewById(R.id.tvTitulo)
        tvValores = findViewById(R.id.tvValores)
        tvEstado = findViewById(R.id.tvEstado)
        ivIman = findViewById(R.id.ivIman)

        //indicar que el sensor ocupará los servicios del sistema
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

        //hacer uso del sensor de luz
        sensorMagnetico = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

        //verificar si el sensor de luz esta disponible
        if(sensorMagnetico == null){
            tvEstado.text = "No hay sensor magnetico :("
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    //MÉTODOS OBLIGATORIOS DE LA CLASE

    //método para ver los cambios en el sensor

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_MAGNETIC_FIELD) {

            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]

            tvValores.text = "X: $x\nY: $y\nZ: $z"

            val valorMayor = maxOf(
                kotlin.math.abs(x),
                kotlin.math.abs(y),
                kotlin.math.abs(z)
            )


            if (valorMayor > 100) {
                tvEstado.text = "Campo magnético fuerte detectado"
                layout.setBackgroundColor(Color.BLACK)
                tvEstado.setTextColor(Color.WHITE)
                tvValores.setTextColor(Color.WHITE)
                tvTitulo.setTextColor(Color.WHITE)
                ivIman.setImageResource(R.drawable.iman_normal)

            } else if (valorMayor >= 50) {
                tvEstado.text = "Campo magnético débil"
                layout.setBackgroundColor(Color.BLUE)
                tvEstado.setTextColor(Color.WHITE)
                tvValores.setTextColor(Color.WHITE)
                tvTitulo.setTextColor(Color.WHITE)
                ivIman.setImageResource(R.drawable.iman_normal)

            } else {
                tvEstado.text = "Campo magnético normal"
                layout.setBackgroundColor(Color.GREEN)
                tvEstado.setTextColor(Color.BLACK)
                tvValores.setTextColor(Color.BLACK)
                tvTitulo.setTextColor(Color.BLACK)
                ivIman.setImageResource(R.drawable.iman_uwu)
            }
        }
    }


    override fun onResume() {
        super.onResume()
        sensorMagnetico?.let {
            sensorManager.registerListener(
                this,
                it,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }


    //método para hacer cambios en los valores matemáticos
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        //MÉTODO PARA APAGAR EL COMPONENTE (SENSOR)
        super.onResume()
    }
}