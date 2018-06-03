package br.com.infinitytechnology.madmarvel.entities

data class CreatorList(val available: Int, val returned: Int, val collectionURI: String,
                       val items: List<CreatorSummary>)