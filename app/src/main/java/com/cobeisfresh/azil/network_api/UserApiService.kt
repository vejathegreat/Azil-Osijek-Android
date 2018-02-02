package com.cobeisfresh.azil.network_api

import com.cobeisfresh.azil.data.request.ChangePasswordRequest
import com.cobeisfresh.azil.data.request.FbLoginRequest
import com.cobeisfresh.azil.data.request.LoginRequest
import com.cobeisfresh.azil.data.request.ResetPasswordRequest
import com.cobeisfresh.azil.data.response.BaseResponse
import com.cobeisfresh.azil.data.response.LoginResponse
import com.cobeisfresh.azil.data.response.UserResponse
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

/**
 * Created by Zerina Salitrezic on 01/08/2017.
 */

interface UserApiService {

    @Multipart
    @POST("auth/sign-up")
    fun registerUser(@Part("data") data: RequestBody, @Part image: MultipartBody.Part?): Single<LoginResponse>

    @POST("auth/sign-in")
    fun loginUser(@Body loginRequest: LoginRequest): Single<LoginResponse>

    @GET("me")
    fun getMe(): Single<UserResponse>

    @POST("auth/facebook")
    fun loginUserWithFacebook(@Body fbLoginRequest: FbLoginRequest): Single<LoginResponse>

    @POST("auth/change-password")
    fun changePassword(@Body changePasswordRequest: ChangePasswordRequest): Single<BaseResponse>

    @GET("auth/recover-password")
    fun forgotPassword(@Query("email") email: String): Single<BaseResponse>

    @POST("auth/recover-password")
    fun resetPassword(@Body resetPasswordRequest: ResetPasswordRequest): Single<BaseResponse>

    @Multipart
    @PUT("me")
    fun setMe(@Part("data") data: RequestBody, @Part image: MultipartBody.Part?): Single<UserResponse>
}