package developidea.com.domain.model


class ValidationException(override val message: String): Throwable()
class ParsingException(override val message: String): Throwable()