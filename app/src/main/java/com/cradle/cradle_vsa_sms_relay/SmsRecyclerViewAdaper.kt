package com.cradle.cradle_vsa_sms_relay

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.cradle.cradle_vsa_sms_relay.activities.MainActivity
import com.cradle.cradle_vsa_sms_relay.database.SmsReferralEntitiy
import com.cradle.cradle_vsa_sms_relay.utilities.DateTimeUtil

class SmsRecyclerViewAdaper(private val context: Context) :
    RecyclerView.Adapter<SmsRecyclerViewAdaper.SMSViewHolder>() {

    private var sms: List<SmsReferralEntitiy> = ArrayList()

    val onCLickList:ArrayList<MainActivity.AdapterClicker> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SMSViewHolder {
        val v: View =
            LayoutInflater.from(parent.context).inflate(R.layout.sms_recycler_item_layout, parent, false)
        return SMSViewHolder(v)

    }


    override fun getItemCount(): Int {
        return sms.size
    }
    fun setReferralList(smsReferralEntities: List<SmsReferralEntitiy>){
        this.sms = smsReferralEntities
        notifyDataSetChanged()
    }
    override fun onBindViewHolder(holder: SMSViewHolder, position: Int) {
        val smsReferralEntitiy: SmsReferralEntitiy = sms[position
        ]
        holder.smsText.text = smsReferralEntitiy.jsonData
        if (smsReferralEntitiy.isUploaded) {
            holder.statusImg.setBackgroundResource(R.drawable.ic_check_circle_24dp)
            holder.statusTxt.text = context.getString(R.string.sucess)
            holder.statusTxt.setTextColor(context.resources.getColor(R.color.green))
            holder.errorTxt.visibility = GONE
        } else if (!smsReferralEntitiy.isUploaded && smsReferralEntitiy.numberOfTriesUploaded ==0){
            holder.statusImg.setBackgroundResource(R.drawable.ic_error_24dp)
            holder.statusTxt.text = context.getString(R.string.progress)
            holder.statusTxt.setTextColor(context.resources.getColor(R.color.yellowDown))
            holder.errorTxt.visibility = VISIBLE
            holder.errorTxt.setTextColor(context.resources.getColor(R.color.yellowDown))
            holder.errorTxt.text = context.getString(R.string.inProgessMessage)
        } else{
            holder.statusImg.setBackgroundResource(R.drawable.ic_error_24dp)
            holder.statusTxt.text = context.getString(R.string.error)
            holder.statusTxt.setTextColor(context.resources.getColor(R.color.redDown))
            holder.errorTxt.setTextColor(context.resources.getColor(R.color.redDown))
            holder.errorTxt.visibility = VISIBLE
            holder.errorTxt.text = smsReferralEntitiy.errorMessage
        }
        holder.receivedTimeTxt.text =
            DateTimeUtil.convertUnixToTimeString(smsReferralEntitiy.timeRecieved)
        holder.layout.setOnClickListener {
            onCLickList.forEach { f -> f.onClick(smsReferralEntitiy) }
        }
    }

    class SMSViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var smsText: TextView = itemView.findViewById<TextView>(R.id.txtBody)
        var statusImg: ImageView = itemView.findViewById(R.id.msgStatus)
        var receivedTimeTxt: TextView = itemView.findViewById(R.id.timeReceivedTxt)
        var errorTxt: TextView = itemView.findViewById(R.id.errorMsgTxt)
        var layout:ConstraintLayout = itemView.findViewById(R.id.referralLayout)
        var statusTxt:TextView = itemView.findViewById(R.id.statusTxt)
    }
}