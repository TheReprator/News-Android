package dev.reprator.news.impl.retrofit

import java.lang.reflect.Type
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import javax.inject.Inject

class EnvelopeConverterFactory @Inject constructor(
    private val types: AppReflectionTypes, private val parseError: ParseError): Converter.Factory() {

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *> {

        val envelopeType: Type = types.newParameterizedType(EntityResponseContainer::class.java, type)

        val delegate: Converter<ResponseBody, EntityResponseContainer<Type>> =
            retrofit.nextResponseBodyConverter(this, envelopeType, annotations)

        return EnvelopeConverter(delegate, parseError)
    }

}