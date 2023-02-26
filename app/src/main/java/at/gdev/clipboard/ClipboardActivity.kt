package at.gdev.clipboard

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import at.gdev.clipboard.responses.PushUrlResponse
import com.google.android.gms.common.util.Hex
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.nio.charset.StandardCharsets
import java.security.SecureRandom
import java.util.Base64.Decoder
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec


class ClipboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var text = intent.getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT)
        val text2 = intent.getCharSequenceExtra(Constants.INTENT_EXTRA_NOTIFICATION);

        if (text == null && text2 != null) {
            text = text2;
        }

        if (text != null) {
            // Extra supplied (copy to universal clipboard, share)
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

            var encryptedContent = "";


                        val iterations = 100000;
            val keyLength = 64 * 4;

            var salt = Base64.decode(sessionHandler.userDetails.salt,  Base64.NO_WRAP)

            var spec = PBEKeySpec(sessionHandler.userDetails.password.toCharArray(), salt, iterations, keyLength);
            var factory = SecretKeyFactory.getInstance("PBKDF2withHmacSHA256");
            var keyBytes = factory.generateSecret(spec).getEncoded();




            // use passwordBasedKey to decrypt the key
            Log.d("TAG", Base64.encodeToString(keyBytes, Base64.NO_WRAP));

            var cipher = Cipher.getInstance("AES/GCM/NoPadding")
            var key = SecretKeySpec(keyBytes, "AES")
            var iv = Base64.decode(sessionHandler.userDetails.iv, Base64.NO_WRAP);

            cipher.init(Cipher.DECRYPT_MODE, key, GCMParameterSpec(128, iv))
            Log.d("KEY", sessionHandler.userDetails.key)
            cipher.update(Base64.decode(sessionHandler.userDetails.key, Base64.NO_WRAP));
            val encodedSymmetricKey = cipher.doFinal()

            // Decode symmetric key

            val decodedSymmetricKey = java.util.Base64.getDecoder().decode(encodedSymmetricKey)

            // use the decrypted key to encrypt the text

            cipher.init(Cipher.ENCRYPT_MODE, SecretKeySpec(decodedSymmetricKey, "AES"), GCMParameterSpec(128, iv))
            val cipherText = cipher.doFinal(text.toString().toByteArray(StandardCharsets.UTF_8))

            encryptedContent = Base64.encodeToString(cipherText, Base64.NO_WRAP)

            val call = apiService.pushPaste(
                "Bearer " + sessionHandler.userDetails.apiToken,
                lastSavedDeviceId,
                encryptedContent
            )

            call.enqueue(object : Callback<PushUrlResponse> {
                override fun onResponse(call: Call<PushUrlResponse>, response: Response<PushUrlResponse>) {
                    Log.d("TAG", "Response = ${response.body()}")
                }

                override fun onFailure(call: Call<PushUrlResponse>, t: Throwable) {
                    Log.d("TAG", "Response = $t")
                }
            });

            finish()
        }

        // normal app start

    }
}