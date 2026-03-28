package com.spacece.milestonetracker.data.model

data class LoginRequest(
    val email: String,
    val password: String,
    val user_type: String
)

data class SignupRequest(
    val name: String,
    val phone: String,
    val email: String,
    val password: String,
    val user_type: String
)

data class ForgotPasswordRequest(
    val email: String,
    val u_mobile: String,
    val password: String
)
