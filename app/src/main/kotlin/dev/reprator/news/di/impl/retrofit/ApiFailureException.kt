package dev.reprator.news.di.impl.retrofit

public class ApiFailureException(val error: EntityError) : RuntimeException()