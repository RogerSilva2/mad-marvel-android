package br.com.infinitytechnology.madmarvel.entities

data class CreatorDataContainer(val offset: Int, val limit: Int, val total: Int, val count: Int,
                                val results: List<Creator>)