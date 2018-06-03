package br.com.infinitytechnology.madmarvel.entities

import java.util.*

data class Story(val id: Int, val title: String, val description: String, val resourceURI: String,
                 val type: String, val modified: Date, val thumbnail: Image, val comics: ComicList,
                 val series: SeriesList, val events: EventList, val characters: CharacterList,
                 val creators: CreatorList, val originalissue: ComicSummary)