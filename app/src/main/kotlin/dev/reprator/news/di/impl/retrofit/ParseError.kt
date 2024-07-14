package dev.reprator.news.impl.retrofit


import okhttp3.ResponseBody
import retrofit2.Retrofit

class ParseErrorImpl constructor(private val retrofit2: Retrofit): ParseError {

    override fun parseError(input: ResponseBody): EntityError {
        val annotation = Annotation::class.constructors.first().call("fooValue")

        val converter = retrofit2.responseBodyConverter<EntityError>(EntityError::class.java,
            arrayOf(annotation))

        return try {
             converter.convert(input)!!
        } catch (e: Exception) {
            e.printStackTrace()
            EntityError("0", "An error occurred")
        }
    }

}

interface ParseError{
    fun parseError(input: ResponseBody): EntityError
}





