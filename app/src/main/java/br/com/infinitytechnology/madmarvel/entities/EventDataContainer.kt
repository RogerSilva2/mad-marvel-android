package br.com.infinitytechnology.madmarvel.entities

data class EventDataContainer(val offset: Int, val limit: Int, val total: Int, val count: Int,
                              val results: List<Event>)