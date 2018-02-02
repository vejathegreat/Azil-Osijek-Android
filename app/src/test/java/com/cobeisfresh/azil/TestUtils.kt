package com.cobeisfresh.azil

import com.cobeisfresh.azil.common.constants.KEY_AGE
import com.cobeisfresh.azil.common.constants.KEY_GENDER
import com.cobeisfresh.azil.common.constants.KEY_SIZE
import com.cobeisfresh.azil.data.models.*
import com.cobeisfresh.azil.data.response.*
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException
import okhttp3.ResponseBody
import retrofit2.Response
import java.util.*


/**
 * Created by Zerina Salitrezic on 09/10/2017.
 */

const val ID = "id"
const val NAME = "Jozo Jozic"
const val EMAIL = "someemail@gmail.com"
const val PASSWORD = "password"
const val IMAGE_URL = "image_url"
const val TOKEN = "token"
const val ERROR_CODE_BAD_REQUEST = 400
const val ERROR_CODE_INVALID_PASSWORD = 424
const val ERROR_CODE_NOT_REGISTERED = 409
const val ERROR_CODE_USER_ALREADY_EXISTS: Int = 409
const val ERROR_CODE_WRONG_TYPE_FILE: Int = 415
const val ERROR_CODE_IMAGE_UPLOAD: Int = 503
const val ERROR_CODE_INVALID_OLD_PASSWORD: Int = 424
const val ERROR_CODE_PASSWORDS_MISMATCHED: Int = 406
const val ERROR_CODE_USER_NOT_REGISTERED: Int = 409
const val ERROR_CODE_WRONG_RECOVERY_CODE = 406
const val ERROR_CODE_FORGOT_INVALID_PASSWORD = 424
const val ERROR_CODE_EXPIRED_RECOVERY_CODE = 422
const val REQUEST_CODE_CAMERA: Int = 1
const val REQUEST_CODE_GALLERY: Int = 2
const val MESSAGE = "message"
const val CODE = "g54j"
const val URL = "url"
const val GENDER = "female"
const val AGE = "adult"
const val SIZE = "large"
const val DESCRIPTION = "description"
const val SEARCH_TERM = "pas"
const val FILTER_GENDER = "Spol"
const val FILTER_AGE = "Dob"
const val FILTER_SIZE = "Rast"
const val FEMALE = "female"
const val MALE = "male"
const val PUPPY = "puppy"
const val YOUNG = "young"
const val ADULT = "adult"
const val SMALL = "small"
const val MEDIUM = "medium"
const val LARGE = "large"
const val FEMALE_VALUE = "žensko"
const val MALE_VALUE = "muško"
const val PUPPY_VALUE = "štenci"
const val YOUNG_VALUE = "mladi psi"
const val ADULT_VALUE = "odrasli psi"
const val SMALL_VALUE = "mali"
const val MEDIUM_VALUE = "srednji"
const val LARGE_VALUE = "veliki"
const val URL_TYPE = 1
const val EMPTY_TEXT = ""

fun mockUserResponse() = UserResponse(id = ID, name = NAME, email = EMAIL, profileImage = IMAGE_URL, isFacebookUser = false)

fun mockLoginResponse() = LoginResponse(token = TOKEN)

fun mockHttpResponse(code: Int): HttpException {
    val response: Response<BaseModel> = Response.error(code, ResponseBody.create(null, ""))
    return HttpException(response)
}

fun mockPhotosList(): List<String> {
    val photos = mutableListOf<String>()
    photos.add(URL)
    photos.add(URL)
    return photos
}

fun mockDogDetailsResponse() = DogDetailsResponse(id = ID, name = NAME, gender = GENDER, age = AGE,
        size = SIZE, longDescription = DESCRIPTION, photo = mockPhotosList(), isFavorite = false)

fun mockDogsList(): List<DogModel> {
    val dogs = mutableListOf<DogModel>()
    dogs.add(DogModel())
    return dogs
}

fun mockDog() = DogModel(id = ID, name = NAME, gender = GENDER, age = AGE,
        size = SIZE, shortDescription = DESCRIPTION, photo = mockPhotosList(), isFavorite = false)

fun mockBaseResponse() = BaseResponse(message = TOKEN)

fun mockDogsResponse() = DogsResponse(mockDogsList())

fun mockUncheckedFilters(): ArrayList<FilterModel> {
    val filters = ArrayList<FilterModel>()
    filters.add(FilterModel(FILTER_GENDER, KEY_GENDER, MALE_VALUE, MALE))
    filters.add(FilterModel(FILTER_GENDER, KEY_GENDER, FEMALE_VALUE, FEMALE))
    filters.add(FilterModel(FILTER_AGE, KEY_AGE, PUPPY_VALUE, PUPPY))
    filters.add(FilterModel(FILTER_AGE, KEY_AGE, YOUNG_VALUE, YOUNG))
    filters.add(FilterModel(FILTER_AGE, KEY_AGE, ADULT_VALUE, ADULT))
    filters.add(FilterModel(FILTER_SIZE, KEY_SIZE, SMALL_VALUE, SMALL))
    filters.add(FilterModel(FILTER_SIZE, KEY_SIZE, MEDIUM_VALUE, MEDIUM))
    filters.add(FilterModel(FILTER_SIZE, KEY_SIZE, LARGE_VALUE, LARGE))
    return filters
}

fun mockCheckedFilters(): ArrayList<FilterModel> {
    val filters = ArrayList<FilterModel>()
    filters.add(FilterModel(FILTER_SIZE, KEY_GENDER, MALE_VALUE, MALE, true))
    filters.add(FilterModel(FILTER_SIZE, KEY_GENDER, FEMALE_VALUE, FEMALE, true))
    filters.add(FilterModel(FILTER_SIZE, KEY_AGE, PUPPY_VALUE, PUPPY, true))
    filters.add(FilterModel(FILTER_SIZE, KEY_AGE, YOUNG_VALUE, YOUNG, true))
    filters.add(FilterModel(FILTER_SIZE, KEY_AGE, ADULT_VALUE, ADULT, true))
    filters.add(FilterModel(FILTER_SIZE, KEY_SIZE, SMALL_VALUE, SMALL, true))
    filters.add(FilterModel(FILTER_SIZE, KEY_SIZE, MEDIUM_VALUE, MEDIUM, true))
    filters.add(FilterModel(FILTER_SIZE, KEY_SIZE, LARGE_VALUE, LARGE, true))
    return filters
}

fun mockPetImagesDataOneImage(): PetImagesData {
    val images = arrayListOf(IMAGE_URL)
    return PetImagesData(0, images)
}

fun mockPetImagesDataMoreImages(): PetImagesData {
    val images = arrayListOf(IMAGE_URL, IMAGE_URL)
    return PetImagesData(0, images)
}

fun mockAdoptDogData() = AdoptDogData(NAME, EMAIL, MESSAGE, ID, true)

fun mockChangePasswordData() = ChangePasswordData(PASSWORD, PASSWORD, PASSWORD)

fun mockFilterDataFiltersExist() = FilterData(mockCheckedFilters())

fun mockFilterDataNoFilters() = FilterData()

fun mockResetPasswordData() = ResetPasswordData(CODE, EMAIL, PASSWORD, PASSWORD)
