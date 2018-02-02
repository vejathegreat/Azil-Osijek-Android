package com.cobeisfresh.azil.network_interaction

import com.cobeisfresh.azil.common.extensions.observeWith
import com.cobeisfresh.azil.data.models.*
import com.cobeisfresh.azil.data.request.FbLoginRequest
import com.cobeisfresh.azil.data.request.LoginRequest
import com.cobeisfresh.azil.data.request.UserRequest
import com.cobeisfresh.azil.data.response.BaseResponse
import com.cobeisfresh.azil.data.response.LoginResponse
import com.cobeisfresh.azil.data.response.UserResponse
import com.cobeisfresh.azil.network_api.UserApiService
import com.cobeisfresh.azil.schedulers.SchedulerManager
import com.google.gson.Gson
import io.reactivex.SingleObserver
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

/**
 * Created by Zerina Salitrezic on 01/08/2017.
 */

private const val IMAGE = "image"
private const val APPLICATION_JSON = "application/json"
private const val CONTENT_TYPE = "image/jpeg"

class UserInteractorImpl(private val schedulerManager: SchedulerManager, private val userApiService: UserApiService) : UserInteractor {

    override fun registerUser(userData: UserRegistrationData, file: File?, loginResponseSingleObserver: SingleObserver<LoginResponse>) {
        val registerRequest = userData.mapToRegisterRequest()
        val parsedObject = Gson().toJson(registerRequest)
        val data = RequestBody.create(MediaType.parse(APPLICATION_JSON), parsedObject)
        val imageData: MultipartBody.Part?

        imageData = if (file != null && file.exists()) {
            val image = RequestBody.create(MediaType.parse(CONTENT_TYPE), file)
            MultipartBody.Part.createFormData(IMAGE, file.name, image)
        } else {
            null
        }

        userApiService.registerUser(data, imageData).observeWith(manager = schedulerManager, observer = loginResponseSingleObserver)
    }

    override fun loginUser(email: String, password: String, loginResponseSingleObserver: SingleObserver<LoginResponse>) {
        userApiService.loginUser(LoginRequest(email, password)).observeWith(manager = schedulerManager, observer = loginResponseSingleObserver)
    }

    override fun getMe(userResponseSingleObserver: SingleObserver<UserResponse>) {
        userApiService.getMe().observeWith(manager = schedulerManager, observer = userResponseSingleObserver)
    }

    override fun loginUserWithFacebook(token: String, loginResponseSingleObserver: SingleObserver<LoginResponse>) {
        userApiService.loginUserWithFacebook(FbLoginRequest(token)).observeWith(manager = schedulerManager, observer = loginResponseSingleObserver)
    }

    override fun changePassword(changePasswordData: ChangePasswordData, baseResponseSingleObserver: SingleObserver<BaseResponse>) {
        val changePasswordRequest = changePasswordData.mapToChangePasswordRequest()
        userApiService.changePassword(changePasswordRequest).observeWith(manager = schedulerManager, observer = baseResponseSingleObserver)
    }

    override fun forgotPassword(email: String, baseResponseSingleObserver: SingleObserver<BaseResponse>) {
        userApiService.forgotPassword(email).observeWith(manager = schedulerManager, observer = baseResponseSingleObserver)
    }

    override fun resetPassword(resetPasswordData: ResetPasswordData, baseResponseSingleObserver: SingleObserver<BaseResponse>) {
        val resetPasswordRequest = resetPasswordData.mapToResetPasswordRequest()
        userApiService.resetPassword(resetPasswordRequest).observeWith(manager = schedulerManager, observer = baseResponseSingleObserver)
    }

    override fun setMe(userRequest: UserRequest?, file: File?, userResponseSingleObserver: SingleObserver<UserResponse>) {
        val parsedObject = Gson().toJson(userRequest)
        val data = RequestBody.create(MediaType.parse(APPLICATION_JSON), parsedObject)
        val imageData: MultipartBody.Part?

        imageData = if (file != null && file.exists()) {
            val avatar = RequestBody.create(MediaType.parse(CONTENT_TYPE), file)
            MultipartBody.Part.createFormData(IMAGE, file.name, avatar)
        } else {
            null
        }

        userApiService.setMe(data, imageData).observeWith(manager = schedulerManager, observer = userResponseSingleObserver)
    }
}