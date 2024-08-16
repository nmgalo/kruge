package dev.nmgalo.kruge.util

@RequiresOptIn(
    message = "This is an internal API that Kruge library uses, please don't rely on it",
    level = RequiresOptIn.Level.ERROR
)
@Retention(AnnotationRetention.BINARY)
@Target(
    AnnotationTarget.CLASS,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.FIELD,
    AnnotationTarget.TYPEALIAS,
    AnnotationTarget.PROPERTY
)
annotation class InternalUse
