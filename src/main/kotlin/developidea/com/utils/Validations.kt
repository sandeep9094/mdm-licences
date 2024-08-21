package developidea.com.utils

fun String.isEmailValid(): Boolean {
    val emailPattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
    return this.isNotEmpty() && Regex(emailPattern).matches(this)
}

fun String.isPasswordValid(): Boolean {
    val passwordPattern = "^(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#\$%^&*(),.?\":{}|<>]).{8,}$"
    return this.isNotEmpty() && Regex(passwordPattern).matches(this)
}

