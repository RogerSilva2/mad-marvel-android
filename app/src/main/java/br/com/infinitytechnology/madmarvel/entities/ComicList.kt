package br.com.infinitytechnology.madmarvel.entities

data class ComicList(val available: Int, val returned: Int, val collectionURI: String,
                     val items: List<ComicSummary>)