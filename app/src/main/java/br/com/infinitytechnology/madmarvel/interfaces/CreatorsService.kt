package br.com.infinitytechnology.madmarvel.interfaces

import br.com.infinitytechnology.madmarvel.entities.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.*

interface CreatorsService {

    @GET("/v1/public/creators")
    fun creators(@Query("ts") ts: String, @Query("apikey") apikey: String,
                 @Query("hash") hash: String, @Query("firstName") firstName: String?,
                 @Query("middleName") middleName: String?,
                 @Query("lastName") lastName: String?,
                 @Query("suffix") suffix: String?,
                 @Query("nameStartsWith") nameStartsWith: String?,
                 @Query("firstNameStartsWith") firstNameStartsWith: String?,
                 @Query("middleNameStartsWith") middleNameStartsWith: String?,
                 @Query("lastNameStartsWith") lastNameStartsWith: String?,
                 @Query("modifiedSince") modifiedSince: Date?,
                 @Query("comics") comics: Int?, @Query("series") series: Int?,
                 @Query("events") events: Int?, @Query("stories") stories: Int?,
                 @Query("orderBy") orderBy: String?, @Query("limit") limit: Int?,
                 @Query("offset") offset: Int?): Call<CreatorDataWrapper>

    @GET("/v1/public/creators/{creatorId}")
    fun creator(@Query("ts") ts: String, @Query("apikey") apikey: String,
                @Query("hash") hash: String,
                @Path("creatorId") creatorId: Int): Call<CreatorDataWrapper>

    @GET("/v1/public/creators/{creatorId}/comics")
    fun comics(@Query("ts") ts: String, @Query("apikey") apikey: String,
               @Query("hash") hash: String, @Path("creatorId") creatorId: Int,
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
               @Query("characters") characters: Int?, @Query("series") series: Int?,
               @Query("events") events: Int?, @Query("stories") stories: Int?,
               @Query("sharedAppearances") sharedAppearances: Int?,
               @Query("collaborators") collaborators: Int?,
               @Query("orderBy") orderBy: String?, @Query("limit") limit: Int?,
               @Query("offset") offset: Int?): Call<ComicDataWrapper>

    @GET("/v1/public/creators/{creatorId}/events")
    fun events(@Query("ts") ts: String, @Query("apikey") apikey: String,
               @Query("hash") hash: String, @Path("creatorId") creatorId: Int,
               @Query("name") name: String?,
               @Query("nameStartsWith") nameStartsWith: String?,
               @Query("modifiedSince") modifiedSince: Date?,
               @Query("characters") characters: Int?, @Query("series") series: Int?,
               @Query("comics") comics: Int?, @Query("stories") stories: Int?,
               @Query("orderBy") orderBy: String?, @Query("limit") limit: Int?,
               @Query("offset") offset: Int?): Call<EventDataWrapper>

    @GET("/v1/public/creators/{creatorId}/series")
    fun series(@Query("ts") ts: String, @Query("apikey") apikey: String,
               @Query("hash") hash: String, @Path("creatorId") creatorId: Int,
               @Query("title") title: String?,
               @Query("titleStartsWith") titleStartsWith: String?,
               @Query("startYear") startYear: Int?,
               @Query("modifiedSince") modifiedSince: Date?,
               @Query("comics") comics: Int?, @Query("stories") stories: Int?,
               @Query("events") events: Int?, @Query("characters") characters: Int?,
               @Query("seriesType") seriesType: String?,
               @Query("contains") contains: String?,
               @Query("orderBy") orderBy: String?, @Query("limit") limit: Int?,
               @Query("offset") offset: Int?): Call<SeriesDataWrapper>

    @GET("/v1/public/creators/{creatorId}/stories")
    fun stories(@Query("ts") ts: String, @Query("apikey") apikey: String,
                @Query("hash") hash: String, @Path("creatorId") creatorId: Int,
                @Query("modifiedSince") modifiedSince: Date?,
                @Query("comics") comics: Int?, @Query("series") series: Int?,
                @Query("events") events: Int?, @Query("characters") characters: Int?,
                @Query("orderBy") orderBy: String?, @Query("limit") limit: Int?,
                @Query("offset") offset: Int?): Call<StoryDataWrapper>

}