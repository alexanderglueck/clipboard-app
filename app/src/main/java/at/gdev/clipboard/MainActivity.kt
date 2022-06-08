package at.gdev.clipboard

import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val text = intent.getCharSequenceExtra(Constants.INTENT_EXTRA_NOTIFICATION);

        if (text != null) {
            val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("copied text", text)
            clipboard.setPrimaryClip(clip)

            Toast.makeText(
                this,
                """Copied "$text" to the universal clipboard""",
                Toast.LENGTH_SHORT
            ).show()

            finish()
        }

    }
}