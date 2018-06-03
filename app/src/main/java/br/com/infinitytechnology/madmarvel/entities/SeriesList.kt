package br.com.infinitytechnology.madmarvel.entities

data class SeriesList(val available: Int, val returned: Int, val collectionURI: String,
                      val items: List<SeriesSummary>)