package com.pd.field_staff.core.api

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.statement.bodyAsText
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import org.json.JSONObject
import timber.log.Timber
import java.io.File


class StaffApi(
    val httpClient: HttpClient,
    val sharedPreferences: SharedPreferences,
    val gson: Gson
    ) {
    // General function for handling requests
    suspend inline fun <reified T> request(
        method: HttpMethod,
        url: String,
        params: Any? = null
    ): Result<T> {
        val jwtToken = sharedPreferences.getString("jwtToken", null)
        return try {
            val response = httpClient.request(url) {
                this.method = method
                when (method) {
                    HttpMethod.Post ->  setBody(params)
                    HttpMethod.Put ->  setBody(params)
                    HttpMethod.Get -> {} // No body needed for GET
                    HttpMethod.Delete -> {} // No body needed for DELETE
                    else -> throw IllegalArgumentException("Unsupported HTTP method: $method")
                }
                jwtToken?.let { bearerAuth(it) }
            }
            if (response.status == HttpStatusCode.OK) {
                val responseBody = gson.fromJson<T>(response.bodyAsText(), object : TypeToken<T>() {}.type)
                Result.success(responseBody)
            } else {
                val _msg = response.bodyAsText()
                val message = JSONObject(_msg).getString("message")
                Timber.d("Error: %s", message)
                Result.failure(Exception(message))
                //Result.failure(Exception("Error: ${response.status}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createPostRequest(url: String, params: HashMap<String, Any>? = null): Result<String> {
        return request(HttpMethod.Post, url, params)
    }

    suspend fun createPutRequest(url: String, params: HashMap<String, Any>? = null): Result<String> {
        return request(HttpMethod.Put, url, params)
    }

    suspend fun createGetRequest(url: String, queryParams: QueryParams? = null): Result<String> {
        val fullUrl = queryParams?.let { buildUrlWithParams(url, it) } ?: url
        return request(HttpMethod.Get, fullUrl)
    }

    private fun buildUrlWithParams(baseUrl: String, queryParams: QueryParams): String {
        return if (queryParams.params.isNotEmpty()) {
            "$baseUrl?${queryParams.toQueryString()}"
        } else {
            baseUrl
        }
    }

    suspend fun createDeleteRequest(url: String): Result<String> {
        return request(HttpMethod.Delete, url)
    }

    suspend fun uploadFile(url: String, file: File): Result<String> {
        return try {
            val response = httpClient.post(url) {
                setBody(MultiPartFormDataContent(
                    formData {
                        append("file", file.readBytes(), Headers.build {
                            append(HttpHeaders.ContentType, "application/octet-stream")
                            append(HttpHeaders.ContentDisposition, "filename=${file.name}")
                        })
                    }
                ))
            }
            if (response.status == HttpStatusCode.OK) {
                Result.success(response.body())
            } else {
                Result.failure(Exception("File upload error: ${response.status}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}

data class QueryParams(
    val params: Map<String, Any>
) {
    fun toQueryString(): String {
        return params.entries.joinToString("&") { (key, value) ->
            "${key}=$value"
        }
    }
}


enum class ApiUrl {

    LOGIN {
        override fun url() = "$pdFieldStaffUrl/login"
    },
    FORGOT_PASSWORD {
        override fun url() = "$pdFieldStaffUrl/forgot-password"
    },
    CHANGE_PASSWORD {
        override fun url() = "$pdFieldStaffUrl/change-password"
    },
    PROFILE {
        override fun url() = "$pdFieldStaffUrl/profile"
    },
    TIME_ENTRY_RECORDS {
        override fun url() = "$pdFieldStaffUrl/clock-records"
    },
    TIME_ENTRY {
        override fun url() = "$pdFieldStaffUrl/time-entry"
    },
    JOB_LIST {
        override fun url() = "$pdFieldStaffUrl/job-list"
    };

    abstract fun url(): String

    val pdFieldStaffUrl = "http://dev.premiumdesignscape.com/api"
}