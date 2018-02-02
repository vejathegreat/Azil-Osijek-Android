package com.cobeisfresh.azil.common.extensions

import java.util.regex.Pattern

/**
 * Created by Zerina Salitrezic on 01/08/2017.
 */

private const val PASSWORD_LENGTH_MINIMUM = 6
private const val EMAIL_REGEX_PATTERN = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])"
private const val SPACE = " "

fun String?.isValid(): Boolean = this != null && isNotBlank()

fun String.isValidEmail(): Boolean = Pattern.compile(EMAIL_REGEX_PATTERN).matcher(this).matches()

fun String.isValidPassword(): Boolean = length >= PASSWORD_LENGTH_MINIMUM

fun String.isValidName(): Boolean {
    val words = this.split(SPACE)
    val elementsWithText = words.count { it.trim() != "" }
    return elementsWithText >= 2
}

fun String.isValidCode(): Boolean = this.length == 4