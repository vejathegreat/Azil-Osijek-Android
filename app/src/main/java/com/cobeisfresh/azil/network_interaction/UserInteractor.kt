package com.cobeisfresh.azil.network_interaction

import com.cobeisfresh.azil.data.models.ChangePasswordData
import com.cobeisfresh.azil.data.models.ResetPasswordData
import com.cobeisfresh.azil.data.models.UserRegistrationData
import com.cobeisfresh.azil.data.request.UserRequest
import com.cobeisfresh.azil.data.response.BaseResponse
import com.cobeisfresh.azil.data.response.LoginResponse
import com.cobeisfresh.azil.data.response.UserResponse
import io.reactivex.SingleObserver
import java.io.File

/**
 * Created by Zerina Salitrezic on 01/08/2017.
 */

interface UserInteractor {

    fun registerUser(userData: UserRegistrationData, file: File?, loginResponseSingleObserver: SingleObserver<LoginResponse>)

    fun loginUser(email: String, password: String, loginResponseSingleObserver: SingleObserver<LoginResponse>)

    fun getMe(userResponseSingleObserver: SingleObserver<UserResponse>)

    fun loginUserWithFacebook(token: String, loginResponseSingleObserver: SingleObserver<LoginResponse>)

    fun changePassword(changePasswordData: ChangePasswordData, baseResponseSingleObserver: SingleObserver<BaseResponse>)

    fun forgotPassword(email: String, baseResponseSingleObserver: SingleObserver<BaseResponse>)

    fun resetPassword(resetPasswordData: ResetPasswordData, baseResponseSingleObserver: SingleObserver<BaseResponse>)

    fun setMe(userRequest: UserRequest?, file: File?, userResponseSingleObserver: SingleObserver<UserResponse>)
}