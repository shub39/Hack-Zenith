package shub39.hackzenith.findin.domain

import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Multipart
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.Part
import io.ktor.http.content.PartData

interface ServerApi {
    @GET("posts/get_all")
    suspend fun getAllPosts(): String

    @Multipart
    @POST("posts/create")
    suspend fun createPost(
        @Part("types") types: String,              // "lost" | "found"
        @Part("title") title: String,
        @Part("description") description: String,
        @Part("tags") tags: String,                // JSON string
        @Part("place") place: String,
        @Part("area") area: String,
        @Part("user_uid") userUid: String,
        @Part("user_email") userEmail: String,
        @Part("user_name") userName: String,
        @Part("user_photo") photoURL: String,
        @Part images: List<PartData.FileItem>
    ): String
}