package com.example.sophos.Data.Model

data class getDocsResponse (
    var Items : List<getDocsItems>,
    var count : Int,
    var ScannedCount : Int
)