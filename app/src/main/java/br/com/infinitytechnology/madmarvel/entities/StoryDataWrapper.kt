package br.com.infinitytechnology.madmarvel.entities

data class StoryDataWrapper(val code: Int, val status: String, val copyright: String,
                            val attributionText: String, val attributionHTML: String,
                            val data: StoryDataContainer, val etag: String)