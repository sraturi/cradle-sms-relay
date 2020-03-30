package com.example.cradle_vsa_sms_relay.broad_castrecivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsMessage
import android.util.Log
import com.example.cradle_vsa_sms_relay.MultiMessageListener

/**
 * detects messages receives
 */
class MessageReciever : BroadcastReceiver() {

    companion object {
        private var meListener: MultiMessageListener? = null;

        fun bindListener(messageListener: MultiMessageListener){
            meListener = messageListener
        }
        fun unbindListener(){
            meListener = null
        }
    }

    override fun onReceive(p0: Context?, p1: Intent?) {
        val data = p1?.extras
        val pdus = data?.get("pdus") as Array<Any>

        // you may recieve multiple messages at the same time from different numbers so
        // we keep track of all the messages from different numbers
        val messages = HashMap<String?, String?>();


        for (element in pdus){
            // if smsMessage is null, we continue to the next one
            val smsMessage = SmsMessage.createFromPdu(element as ByteArray?) ?: continue
            //one message has length of 153 chars, 7 other chars for user data header

            Log.d("bugg", smsMessage.messageBody+ " len: "+ smsMessage.messageBody.length);

            //We are assuming that no one phone can send multiple long messages at ones.
            // since there is some user delay in either typing or copy/pasting the message
            //or typing 1 char at  a time
            if (messages.containsKey(smsMessage.originatingAddress)){
                //concatenating messages
                val newMsg:String = smsMessage.messageBody;
                val oldMsg: String? = messages[smsMessage.originatingAddress]
                messages[smsMessage.originatingAddress] = oldMsg + newMsg
            } else {
                messages[smsMessage.originatingAddress] = smsMessage.messageBody
            }
        }
        // send it to the service to send to the server
        meListener?.messageMapRecieved(
            messages
            );

    }

}