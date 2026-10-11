package com.face.shop.utility

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.widget.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// ==========================================
// 1. หน้ากากเครื่องคิดเลข + ประตูลับ 67
// ==========================================
class CalculatorActivity : Activity() {
    private lateinit var display: TextView
    private var inputBuffer = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(Color.parseColor("#0B0F19"))
            setPadding(40, 40, 40, 40)
            gravity = Gravity.CENTER
        }

        display = TextView(this).apply {
            text = "0"
            textSize = 48f
            setTextColor(Color.WHITE)
            gravity = Gravity.END
            setPadding(20, 50, 20, 50)
        }

        layout.addView(display)

        val buttons = arrayOf(
            arrayOf("7", "8", "9", "/"),
            arrayOf("4", "5", "6", "*"),
            arrayOf("1", "2", "3", "-"),
            arrayOf("C", "0", "=", "+")
        )

        for (row in buttons) {
            val rowLayout = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                gravity = Gravity.CENTER
            }
            for (text in row) {
                val btn = Button(this).apply {
                    this.text = text
                    textSize = 20f
                    setBackgroundColor(Color.parseColor("#1F2937"))
                    setTextColor(Color.WHITE)
                    setOnClickListener { onCalculatorClick(text) }
                }
                val params = LinearLayout.LayoutParams(150, 150).apply { setMargins(10, 10, 10, 10) }
                rowLayout.addView(btn, params)
            }
            layout.addView(rowLayout)
        }
        setContentView(layout)
    }

    private fun onCalculatorClick(value: String) {
        if (value == "C") {
            inputBuffer = ""
            display.text = "0"
            return
        }
        
        inputBuffer += value
        display.text = inputBuffer

        // พิมพ์ 67 เมื่อไหร่ เด้งหน้าต่างใส่ Key ทันที
        if (inputBuffer == "67") {
            inputBuffer = ""
            showKeyDialog()
        }
    }

    private fun showKeyDialog() {
        val dialogLayout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(50, 50, 50, 50)
            setBackgroundColor(Color.parseColor("#111827"))
        }

        val title = TextView(this).apply {
            text = "FACE.SHOP SECURE GATEWAY"
            setTextColor(Color.parseColor("#10B981"))
            textSize = 18f
        }

        val inputKey = EditText(this).apply {
            hint = "ENTER VIP KEY"
            setTextColor(Color.WHITE)
            setHintTextColor(Color.GRAY)
        }

        val submitBtn = Button(this).apply {
            text = "VERIFY & LAUNCH"
            setBackgroundColor(Color.parseColor("#3B82F6"))
            setTextColor(Color.WHITE)
        }

        dialogLayout.addView(title)
        dialogLayout.addView(inputKey)
        dialogLayout.addView(submitBtn)

        val dialog = AlertDialog.Builder(this)
            .setView(dialogLayout)
            .create()

        submitBtn.setOnClickListener {
            val key = inputKey.text.toString().trim()
            if (FaceKeyDatabase.validateKey(key)) {
                dialog.dismiss()
                val intent = Intent(this, FaceShopDashboardActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Invalid or Expired Key!", Toast.LENGTH_SHORT).show()
            }
        }
        dialog.show()
    }
}

// ==========================================
// 2. ฐานข้อมูลคีย์รายวัน + คีย์ถาวรเจ้าของ
// ==========================================
object FaceKeyDatabase {
    private val dateKeyMap = mapOf(
        "01" to "FACESHOP-DAY-001-A",
        "02" to "FACESHOP-DAY-002-B",
        "03" to "FACESHOP-DAY-003-C",
        "04" to "FACESHOP-DAY-004-D",
        "05" to "FACESHOP-DAY-005-E"
    )

    // คีย์ถาวรสำหรับเจ้าของ (Owner Master Key)
    private const val MASTER_OWNER_KEY = "FACESHOP-OWNER-MASTER-999"

    fun validateKey(inputKey: String): Boolean {
        val trimmedKey = inputKey.trim()

        if (trimmedKey == MASTER_OWNER_KEY) {
            return true
        }

        val currentDate = SimpleDateFormat("dd", Locale.getDefault()).format(Date())
        val expectedKey = dateKeyMap[currentDate]

        return trimmedKey == expectedKey
    }
}

// ==========================================
// 3. หน้าต่างเมนูหลัก Face.shop
// ==========================================
class FaceShopDashboardActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(Color.parseColor("#0A0E17"))
            setPadding(40, 40, 40, 40)
        }

        val header = TextView(this).apply {
            text = "FACE.SHOP // DASHBOARD"
            setTextColor(Color.parseColor("#00FF66"))
            textSize = 22f
            setPadding(0, 0, 0, 20)
        }

        val statusText = TextView(this).apply {
            text = "STATUS: STEALTH ENGINE ACTIVE"
            setTextColor(Color.WHITE)
            textSize = 14f
            setPadding(0, 0, 0, 30)
        }

        val toggleBypass = ToggleButton(this).apply {
            textOn = "FACE.SHOP PROTECTION: ON"
            textOff = "FACE.SHOP PROTECTION: OFF"
            isChecked = true
        }

        val toggleAim = ToggleButton(this).apply {
            textOn = "SMART LEGIT AIM: ON"
            textOff = "SMART LEGIT AIM: OFF"
        }

        val launchButton = Button(this).apply {
            text = "START ENGINE"
            setBackgroundColor(Color.parseColor("#E11D48"))
            setTextColor(Color.WHITE)
            setOnClickListener {
                Toast.makeText(this@FaceShopDashboardActivity, "Face.shop Core Activated!", Toast.LENGTH_SHORT).show()
            }
        }

        layout.addView(header)
        layout.addView(statusText)
        layout.addView(toggleBypass)
        layout.addView(toggleAim)

        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply { topMargin = 50 }

        layout.addView(launchButton, params)
        setContentView(layout)
    }
}
