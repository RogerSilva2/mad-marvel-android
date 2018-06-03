package br.com.infinitytechnology.madmarvel.entities

import java.util.*

data class Creator(val id: Int, val firstName: String, val middleName: String, val lastName: String,
                   val suffix: String, val fullName: String, val modified: Date,
                   val resourceURI: String, val urls: List<Url>, val thumbnail: Image,
                   val series: SeriesList, val stories: StoryList, val comics: ComicList,
                   val events: EventList)