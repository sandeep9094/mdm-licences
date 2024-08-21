package developidea.com.domain.model.local


class ValidationException(override val message: String): Throwable()
class ParsingException(override val message: String): Throwable()
class InvalidExpiryTimestampException(message: String) : Throwable()