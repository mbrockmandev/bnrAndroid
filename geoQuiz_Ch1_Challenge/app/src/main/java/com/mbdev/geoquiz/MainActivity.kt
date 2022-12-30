package com.mbdev.geoquiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var layout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        layout = findViewById(R.id.layout)

        var snackbarCorrect = Snackbar.make(layout, R.string.correct_toast, Snackbar.LENGTH_SHORT)
            .setBackgroundTint(resources.getColor(R.color.teal_200))

        var snackbarIncorrect = Snackbar.make(layout, R.string.incorrect_toast, Snackbar.LENGTH_SHORT)
            .setBackgroundTint(resources.getColor(R.color.purple_700))

        trueButton.setOnClickListener {
            snackbarCorrect.show()
        }

        falseButton.setOnClickListener {
            snackbarIncorrect.show()
        }
    }
}