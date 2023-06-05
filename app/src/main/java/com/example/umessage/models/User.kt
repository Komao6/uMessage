package com.example.umessage.models


class User {
    var uid: String? = null
    var username: String? = null
    var profileImageUrl: String? = null


    constructor(){}

    constructor(uid:String, username: String?, profileImageUrl: String?){
        this.uid = uid
        this.username = username
        this.profileImageUrl = profileImageUrl

    }

}