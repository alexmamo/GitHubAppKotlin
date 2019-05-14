package ro.alexmamo.githubapp.models

import java.io.Serializable

class Repository(
    var id: String,
    var name: String,
    var fullName: String,
    var url: String) : Serializable