package com.example.library.util

//    API STATUS CODE
// 2X
const val STATUS_200 = 200 // Success
const val STATUS_201 = 201 // Success
const val STATUS_204 = 204 // No Content Found. (No More Data or No Sub Category Data Found).
const val STATUS_208 = 208 // 208 - Already Reported - user already exist

// 4X
const val STATUS_400 = 400 // Bad Request
const val STATUS_401 = 401 // Unauthorise
const val STATUS_402 = 402 // Payment required
const val STATUS_404 = 404 // No Data Found.
const val STATUS_405 = 405 // Method not found
const val STATUS_406 = 406  // Not acceptable - email not varified
const val STATUS_408 = 408 // Conflict - password and email is not match
const val STATUS_409 = 409 // Conflict - password and email is not match
const val STATUS_410 = 410 //
const val STATUS_422 = 422 // Missing Parameters
const val STATUS_426 = 426 // Missing Parameters
const val STATUS_429 = 429 // Missing Parameters
const val STATUS_430 = 430 // Missing Parameters
const val STATUS_431 = 431 // Missing Parameters
const val STATUS_432 = 432 // Missing Parameters

// 5X
const val STATUS_500 = 500 // Internal server
const val STATUS_503 = 503 // Internal server

const val REQUEST_CAMERA = 200
const val REQUEST_GALLERY = 201

const val API_BROADCAST = "api_broadcast"
const val API_BROADCAST_DATA = "api_broadcast_data"
const val API_BROADCAST_STATUS_CODE_401 = "api_broadcast_status_code_401"
const val API_BROADCAST_STATUS_CODE_426 = "api_broadcast_status_code_426"

const val SOCKET_BROADCAST = "socket_broadcast_event"
const val SOCKET_BROADCAST_EVENT_NAME = "socket_broadcast_event_name"
const val SOCKET_BROADCAST_EVENT_DATA = "socket_broadcast_event_data"
const val SOCKET_BROADCAST_401 = "socket_broadcast_401"

//    PERMISSIONS CODE
const val REQUEST_CODE_CALL = 100
const val REQUEST_CODE_READ_EXTERNAL_STORAGE = 101
const val REQUEST_CODE_WRITE_EXTERNAL_STORAGE = 102
const val REQUEST_CODE_CAMERA = 103
const val REQUEST_CODE_ACCESS_FINE_LOCATION = 104
const val REQUEST_CODE_CROP_IMAGE = 105














