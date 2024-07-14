package dev.reprator.news.impl.retrofit

public class ApiFailureException(val error: EntityError) : RuntimeException()