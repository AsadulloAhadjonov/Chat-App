package com.asadullo.goofgleregistratsiya.Models

import java.io.Serializable

class User:Serializable {
    var displayName:String? = null
    var imgLink:String? = null
    var email:String? = null
    var uid:String? = null


    constructor()
    constructor(displayName: String?, imgLink: String?, email: String?, uid: String?) {
        this.displayName = displayName
        this.imgLink = imgLink
        this.email = email
        this.uid = uid
    }

    override fun toString(): String {
        return "User(displayName=$displayName, imgLink=$imgLink, email=$email)"
    }
}