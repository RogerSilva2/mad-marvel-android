package br.com.infinitytechnology.madmarvel.entities

import java.util.*

data class Event(val id: Int, val title: String, val description: String, val resourceURI: String,
                 val urls: List<Url>, val modified: Date, val start: Date, val end: Date,
                 val thumbnail: Image, val comics: ComicList, val stories: StoryList,
                 val series: SeriesList, val characters: CharacterList, val creators: CreatorList,
                 val next: EventSummary, val previous: EventSummary)