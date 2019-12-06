package com.example.library.credit_card


object CardPattern {

    val VISA_CARD = "Visa"
    val MASTERCARD_CARD = "MasterCard"
    val AMERICAN_EXPRESS_CARD = "American_Express"
    val DISCOVER_CARD = "Discover"
    val JCB_CARD = "JCB"
    val DINERS_CLUB_CARD = "Diners_Club"
    val UNKNOWN_CARD = "UNKNOWN"

    fun getCardType(cardNumber: String): String {
        val s = cardNumber.replace("-", "").trim()
        if (s.startsWith("4") || s.matches(VISA.toRegex())) {
            return VISA_CARD
        } else if (s.matches(MASTERCARD_SHORTER.toRegex()) || s.matches(MASTERCARD_SHORT.toRegex()) || s.matches(
                MASTERCARD.toRegex()
            )
        ) {
            return MASTERCARD_CARD
        } else if (s.matches(AMERICAN_EXPRESS.toRegex())) {
            return AMERICAN_EXPRESS_CARD
        } else if (s.matches(DISCOVER_SHORT.toRegex()) || s.matches(DISCOVER.toRegex())) {
            return DISCOVER_CARD
        } else if (s.matches(JCB_SHORT.toRegex()) || s.matches(JCB.toRegex())) {
            return JCB_CARD
        } else if (s.matches(DINERS_CLUB_SHORT.toRegex()) || s.matches(DINERS_CLUB.toRegex())) {
            return DINERS_CLUB_CARD
        } else {
            return UNKNOWN_CARD
        }
    }

    fun isValidCard(cardNumber: String): Boolean {
        if (cardNumber.matches(Regex(VISA_VALID))) return true
        if (cardNumber.matches(Regex(MASTERCARD_VALID))) return true
        if (cardNumber.matches(Regex(AMERICAN_EXPRESS_VALID))) return true
        if (cardNumber.matches(Regex(DISCOVER_VALID))) return true
        if (cardNumber.matches(Regex(DINERS_CLUB_VALID))) return true
        return cardNumber.matches(Regex(JCB_VALID))
    }

    // VISA
    val VISA = "4[0-9]{12}(?:[0-9]{3})?"
    val VISA_VALID = "^4[0-9]{12}(?:[0-9]{3})?$"

    // MasterCard
    val MASTERCARD =
        "^(?:5[1-5][0-9]{2}|222[1-9]|22[3-9][0-9]|2[3-6][0-9]{2}|27[01][0-9]|2720)[0-9]{12}$"
    val MASTERCARD_SHORT = "^(?:222[1-9]|22[3-9][0-9]|2[3-6][0-9]{2}|27[01][0-9]|2720)"
    val MASTERCARD_SHORTER = "^(?:5[1-5])"
    val MASTERCARD_VALID =
        "^(?:5[1-5][0-9]{2}|222[1-9]|22[3-9][0-9]|2[3-6][0-9]{2}|27[01][0-9]|2720)[0-9]{12}$"

    // American Express
    val AMERICAN_EXPRESS = "^3[47][0-9]{0,13}"
    val AMERICAN_EXPRESS_VALID = "^3[47][0-9]{13}$"

    // DISCOVER
    val DISCOVER = "^6(?:011|5[0-9]{1,2})[0-9]{0,12}"
    val DISCOVER_SHORT = "^6(?:011|5)"
    val DISCOVER_VALID = "^6(?:011|5[0-9]{2})[0-9]{12}$"

    // JCB
    val JCB = "^(?:2131|1800|35\\d{0,3})\\d{0,11}$"
    val JCB_SHORT = "^2131|1800"
    val JCB_VALID = "^(?:2131|1800|35\\d{3})\\d{11}$"

    // Discover
    val DINERS_CLUB = "^3(?:0[0-5]|[68][0-9])[0-9]{11}$"
    val DINERS_CLUB_SHORT = "^30[0-5]"
    val DINERS_CLUB_VALID = "^3(?:0[0-5]|[68][0-9])[0-9]{11}$"

}