package com.levi.hermes_trading.receiver

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.json.JSONException
import android.content.Context
import org.json.JSONObject
//for Open file Output, Write and Use
import kotlin.io.use
import java.io.FileNotFoundException
import java.io.IOException
import java.lang.Exception
import android.util.Log


class ReceiverJson : FirebaseMessagingService() {

    private var lastMessageJson: JSONObject? = null

    //Sets what should be run, when a msg is received -> Process Json
    // TODO: Update function to handle the full json
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // This will be called when a message is received
        val data = remoteMessage.data["message"] ?: return
        try {
            val json = JSONObject(data)


            saveJsonToFile(json)
            //Variable for saving the latest json
            lastMessageJson = json
        }
        catch (e: JSONException){
            Log.e("ReceiverJson", "Invalid JSON: ${e.message}")
        }
    }

    //Sets what should be run if the token has changed -> Send token back to server
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("ReceiverJson", "New FCM Token: $token")

        // TODO: Handle FCM token updates
        // 1. Override onNewToken(token: String) to catch new tokens.
        // 2. Write the token to Firebase Realtime Database or Firestore, e.g.:
        //      val database = FirebaseDatabase.getInstance().reference
        //      database.child("users").child(userId).child("fcmToken").setValue(token)
        // 3. Ensure your backend listens to Firebase DB changes to get updated tokens.
        // 4. Backend saves tokens and uses them to send FCM messages.
        // 5. (Optional) Handle token cleanup when tokens become invalid.
    }


    private fun saveJsonToFile(json: JSONObject) {
        openFileOutput("last_message.json", Context.MODE_PRIVATE).use {
            it.write(json.toString().toByteArray())
        }
    }

    private fun readJsonFromFile(): JSONObject? {
        return try {
            val jsonString = openFileInput("last_message.json").bufferedReader().use { it.readText() }
            JSONObject(jsonString)
        } catch (e: Exception) {
            null
        }
    }

    //Public function so other modules can get the json file too
    public fun getJson(): JSONObject? {
        return try {
            readJsonFromFile()
        } catch (e: Exception) {
            Log.e("ReceiverJson", "Failed to read JSON: ${e.message}")
            null
        }
    }


}