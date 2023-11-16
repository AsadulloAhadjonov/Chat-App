package com.asadullo.goofgleregistratsiya.Models

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Date
import java.util.SimpleTimeZone

class MyMessage {
    var id:String? = null
    var text:String? = null
    var fromUid:String? = null
    var toUid:String? = null
    var date:String = SimpleDateFormat("dd.MM.yyyy HH:mm").format(Date())

    constructor(id: String?, text: String?, fromUid: String?, toUid: String?, date: String) {
        this.id = id
        this.text = text
        this.fromUid = fromUid
        this.toUid = toUid
        this.date = date
    }

    constructor()
    constructor(id: String?, text: String?, fromUid: String?, toUid: String?) {
        this.id = id
        this.text = text
        this.fromUid = fromUid
        this.toUid = toUid
    }


}