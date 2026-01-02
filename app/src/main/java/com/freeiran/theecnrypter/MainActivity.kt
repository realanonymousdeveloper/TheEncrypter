package com.freeiran.theecnrypter

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.util.Base64
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

class MainActivity : AppCompatActivity() {

    private lateinit var inputText: TextInputEditText
    private lateinit var passwordText: TextInputEditText
    private lateinit var outputText: TextInputEditText
    private lateinit var encryptButton: MaterialButton
    private lateinit var decryptButton: MaterialButton
    private lateinit var copyButton: MaterialButton
    private lateinit var clearButton: MaterialButton

    private val defaultPassword = "TheEncrypterDefaultKey2024"
    private val salt = "TheEncrypterSalt".toByteArray()
    private val ivBytes = ByteArray(16) { it.toByte() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initViews()
        setupListeners()
    }

    private fun initViews() {
        inputText = findViewById(R.id.inputText)
        passwordText = findViewById(R.id.passwordText)
        outputText = findViewById(R.id.outputText)
        encryptButton = findViewById(R.id.encryptButton)
        decryptButton = findViewById(R.id.decryptButton)
        copyButton = findViewById(R.id.copyButton)
        clearButton = findViewById(R.id.clearButton)
    }

    private fun setupListeners() {
        encryptButton.setOnClickListener {
            val input = inputText.text.toString()
            if (input.isEmpty()) {
                showToast("Please enter a message to encrypt")
                return@setOnClickListener
            }
            val password = getPassword()
            try {
                val encrypted = encrypt(input, password)
                outputText.setText(encrypted)
                showToast("Encrypted successfully")
            } catch (e: Exception) {
                showToast("Encryption failed: ${e.message}")
            }
        }

        decryptButton.setOnClickListener {
            val input = inputText.text.toString()
            if (input.isEmpty()) {
                showToast("Please enter a message to decrypt")
                return@setOnClickListener
            }
            val password = getPassword()
            try {
                val decrypted = decrypt(input, password)
                outputText.setText(decrypted)
                showToast("Decrypted successfully")
            } catch (e: Exception) {
                showToast("Decryption failed: Wrong password or invalid data")
            }
        }

        copyButton.setOnClickListener {
            val output = outputText.text.toString()
            if (output.isEmpty()) {
                showToast("Nothing to copy")
                return@setOnClickListener
            }
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Encrypted Text", output)
            clipboard.setPrimaryClip(clip)
            showToast("Copied to clipboard")
        }

        clearButton.setOnClickListener {
            inputText.setText("")
            passwordText.setText("")
            outputText.setText("")
            showToast("Cleared")
        }
    }

    private fun getPassword(): String {
        val password = passwordText.text.toString()
        return if (password.isEmpty()) defaultPassword else password
    }

    private fun generateKey(password: String): SecretKeySpec {
        val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
        val spec = PBEKeySpec(password.toCharArray(), salt, 65536, 256)
        val secretKey = factory.generateSecret(spec)
        return SecretKeySpec(secretKey.encoded, "AES")
    }

    private fun encrypt(plainText: String, password: String): String {
        val key = generateKey(password)
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        val ivSpec = IvParameterSpec(ivBytes)
        cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec)
        val encryptedBytes = cipher.doFinal(plainText.toByteArray(Charsets.UTF_8))
        return Base64.encodeToString(encryptedBytes, Base64.NO_WRAP)
    }

    private fun decrypt(encryptedText: String, password: String): String {
        val key = generateKey(password)
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        val ivSpec = IvParameterSpec(ivBytes)
        cipher.init(Cipher.DECRYPT_MODE, key, ivSpec)
        val encryptedBytes = Base64.decode(encryptedText, Base64.NO_WRAP)
        val decryptedBytes = cipher.doFinal(encryptedBytes)
        return String(decryptedBytes, Charsets.UTF_8)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
