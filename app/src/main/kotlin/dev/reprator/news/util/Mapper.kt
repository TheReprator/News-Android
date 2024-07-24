package dev.reprator.news.util

fun interface Mapper<Input, Output> {
    fun map(from: Input): Output
}

fun interface MapperAddition<Input, SupportingInput, Output> {
    fun map(from: Input, supportingInput: SupportingInput): Output
}

fun interface IndexedMapper<Input, Output> {
    fun map(index: Int, from: Input): Output
}

inline fun <Input, Output> Mapper<Input, Output>.mapAll(collection: Collection<Input>) =
    collection.map { map(it) }

inline fun <Input, SupportingInput, Output> MapperAddition<Input, SupportingInput, Output>.mapAll(
    supportingInput: SupportingInput, collection: Collection<Input>
) = collection.map {
    map(it, supportingInput)
}