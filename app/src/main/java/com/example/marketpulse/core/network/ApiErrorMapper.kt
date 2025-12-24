package com.example.marketpulse.core.network

import retrofit2.HttpException
import java.io.IOException

object ApiErrorMapper {
    fun toMessage(t: Throwable): Pair<String, Int?> {
        return when (t) {
            is HttpException -> {
                val code = t.code()
                val msg = when (code) {
                    401, 403 -> "Нет доступа (ключ/лимит). Код: $code"
                    429 -> "Слишком много запросов (rate limit). Код: 429"
                    else -> "Ошибка сервера. Код: $code"
                }
                msg to code
            }
            is IOException -> "Проблема сети. Проверь интернет." to null
            else -> "Неизвестная ошибка." to null
        }
    }
}
