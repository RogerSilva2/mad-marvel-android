package br.com.infinitytechnology.madmarvel.interfaces

import br.com.infinitytechnology.madmarvel.entities.CharacterDataWrapper
import br.com.infinitytechnology.madmarvel.entities.ComicDataWrapper
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.*

interface CharactersService {

    @GET("/v1/public/characters")
    fun characters(@Query("ts") ts: String, @Query("apikey") apikey: String,
                   @Query("hash") hash: String, @Query("name") name: String?,
                   @Query("nameStartsWith") nameStartsWith: String?,
                   @Query("modifiedSince") modifiedSince: Date?,
                   @Query("comics") comics: Int?, @Query("series") series: Int?,
                   @Query("events") events: Int?, @Query("stories") stories: Int?,
                   @Query("orderBy") orderBy: String?, @Query("limit") limit: Int?,
                   @Query("offset") offset: Int?): Call<CharacterDataWrapper>

    @GET("/v1/public/characters/{characterId}")
    fun character(@Query("ts") ts: String, @Query("apikey") apikey: String,
                  @Query("hash") hash: String,
                  @Path("characterId") characterId: Int): Call<CharacterDataWrapper>

    @GET("/v1/public/characters/{characterId}/comics")
    fun comics(@Query("ts") ts: String, @Query("apikey") apikey: String,
               @Query("hash") hash: String, @Path("characterId") characterId: Int,
               @Query("format") format: String?,
               @Query("formatType") formatType: String?,
               @Query("noVariants") noVariants: Boolean?,
               @Query("dateDescriptor") dateDescriptor: String?,
               @Query("dateRange") dateRange: Int?, @Query("title") title: String?,
               @Query("titleStartsWith") titleStartsWith: String?,
               @Query("startYear") startYear: Int?,
               @Query("issueNumber") issueNumber: Int?,
               @Query("diamondCode") diamondCode: String?,
               @Query("digitalId") digitalId: Int?,
               @Query("upc") upc: String?, @Query("isbn") isbn: String?,
               @Query("ean") ean: String?, @Query("issn") issn: String?,
               @Query("hasDigitalIssue") hasDigitalIssue: Boolean?,
               @Query("modifiedSince") modifiedSince: Date?,
               @Query("creators") creators: Int?,
               @Query("characters") characters: Int?, @Query("series") series: Int?,
               @Query("events") events: Int?, @Query("stories") stories: Int?,
               @Query("sharedAppearances") sharedAppearances: Int?,
               @Query("collaborators") collaborators: Int?,
               @Query("orderBy") orderBy: String?, @Query("limit") limit: Int?,
               @Query("offset") offset: Int?): Call<ComicDataWrapper>

}