package br.com.infinitytechnology.madmarvel.entities

data class CharacterList(val available: Int, val returned: Int, val collectionURI: String,
                         val items: List<CharacterSummary>)