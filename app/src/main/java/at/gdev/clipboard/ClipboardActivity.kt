package at.gdev.clipboard

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import at.gdev.clipboard.responses.PushUrlResponse
import at.gdev.clipboard.responses.SessionResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ClipboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val text = intent.getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT)

        if (text != null) {
            val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("copied text", text)
            clipboard.setPrimaryClip(clip)

            Toast.makeText(
                this,
                """Copied "$text" to the universal clipboard""",
                Toast.LENGTH_SHORT
            ).show()

            val sessionHandler = SessionHandler(this)

            val apiService = ApiClient.getClient().create(ApiInterface::class.java)
            val lastSavedDeviceId: Int =
                getSharedPreferences(Constants.SHARED_PREFERENCES_FILE, MODE_PRIVATE).getInt(
                    Constants.LAST_SIGNED_IN_DEVICE_ID,
                    0
                )

            val call = apiService.pushPaste(
                "Bearer " + sessionHandler.userDetails.apiToken,
                lastSavedDeviceId,
                text.toString()
            )

            call.enqueue(object : Callback<PushUrlResponse> {
                override fun onResponse(call: Call<PushUrlResponse>, response: Response<PushUrlResponse>) {
                    Log.d("TAG", "Response = ${response.body()}")
                }

                override fun onFailure(call: Call<PushUrlResponse>, t: Throwable) {
                    Log.d("TAG", "Response = $t")
                }
            });
        }


        finish()
    }
}