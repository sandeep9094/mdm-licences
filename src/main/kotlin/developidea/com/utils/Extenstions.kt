package developidea.com.utils

import java.util.*

fun String.encryptPassword(): String {
    val base64Encoder = Base64.getEncoder()
    val encryptedPassword = String(base64Encoder.encode(Cipher.encrypt(this)))
    return encryptedPassword;
}