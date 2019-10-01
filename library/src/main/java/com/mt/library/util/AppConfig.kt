package com.mt.library.util

class AppConfig {

    //    API STATUS CODE
    // 2X
    val STATUS_200 = 200 // Success
    val STATUS_201 = 201 // Success
    val STATUS_204 = 204 // No Content Found. (No More Data or No Sub Category Data Found).
    val STATUS_208 = 208 // 208 - Already Reported - user already exist

    // 4X
    val STATUS_400= 400 // Bad Request
    val STATUS_401= 401 // Unauthorise
    val STATUS_404 = 404 // No Data Found.
    val STATUS_405= 405 // Method not found
    val STATUS_406= 406  // Not acceptable - email not varified
    val STATUS_409 = 409 // Conflict - password and email is not match
    val STATUS_422 = 422 // Missing Parameters

    // 5X
    val STATUS_500= 500 // Internal server
}
