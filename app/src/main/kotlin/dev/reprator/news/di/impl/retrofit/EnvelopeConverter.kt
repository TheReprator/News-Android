package dev.reprator.news.di.impl.retrofit

import okhttp3.ResponseBody
import retrofit2.Converter

class EnvelopeConverter<T> constructor(
    private val delegate: Converter<ResponseBody,
        EntityResponseContainer<T>>,
    private val parseError: ParseError
):Converter<ResponseBody, T> {

    override fun convert(value: ResponseBody): T? {
        val container = delegate.convert(value) ?: return null

        return if("ok".equals(container.status, true))
            container.articles
        else
            throw ApiFailureException(parseError.parseError(value))
    }
}

