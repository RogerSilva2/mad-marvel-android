package br.com.infinitytechnology.madmarvel.entities

data class Event(val id: Int, val title: String, val description: String, val resourceURI: String,
                 val urls: List<Url>, val modified: String, val start: String, val end: String,
                 val thumbnail: Image, val comics: ComicList, val stories: StoryList,
                 val series: SeriesList, val characters: CharacterList, val creators: CreatorList,
                 val next: EventSummary, val previous: EventSummary)