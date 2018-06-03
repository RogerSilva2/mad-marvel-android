package br.com.infinitytechnology.madmarvel.entities

data class CreatorDataWrapper(val code: Int, val status: String, val copyright: String,
                              val attributionText: String, val attributionHTML: String,
                              val data: CreatorDataContainer, val etag: String)