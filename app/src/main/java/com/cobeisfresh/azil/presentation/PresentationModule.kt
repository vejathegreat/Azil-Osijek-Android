package com.cobeisfresh.azil.presentation

import com.cobeisfresh.azil.network_interaction.DogsInteractor
import com.cobeisfresh.azil.network_interaction.UserInteractor
import com.cobeisfresh.azil.shared_prefs.SharedPrefs
import com.cobeisfresh.azil.ui.adopt_pet.AdoptPetInterface
import com.cobeisfresh.azil.ui.all_pets.AllPetsInterface
import com.cobeisfresh.azil.ui.all_pets.filter.FilterPetsInterface
import com.cobeisfresh.azil.ui.all_pets.list.AllPetsHolderInterface
import com.cobeisfresh.azil.ui.edit_password.EditPasswordInterface
import com.cobeisfresh.azil.ui.edit_profile.EditProfileInterface
import com.cobeisfresh.azil.ui.forgot_reset_password.forgot_password.ForgotPasswordInterface
import com.cobeisfresh.azil.ui.forgot_reset_password.reset_password.ResetPasswordInterface
import com.cobeisfresh.azil.ui.login.LoginInterface
import com.cobeisfresh.azil.ui.main.MainInterface
import com.cobeisfresh.azil.ui.my_pets.MyPetsInterface
import com.cobeisfresh.azil.ui.my_pets.list.MyPetsHolderInterface
import com.cobeisfresh.azil.ui.pet_details.PetDetailsInterface
import com.cobeisfresh.azil.ui.pet_details.pet_images.PetImagesInterface
import com.cobeisfresh.azil.ui.profile.ProfileInterface
import com.cobeisfresh.azil.ui.register.RegisterInterface
import com.cobeisfresh.azil.ui.web_view.WebViewInterface
import com.cobeisfresh.azil.ui.welcome.WelcomeInterface
import dagger.Module
import dagger.Provides

/**
 * Created by Zerina Salitrezic on 31/07/2017.
 */

@Module
class PresentationModule {

    @Provides
    fun provideMainPresenter(sharedPrefs: SharedPrefs): MainInterface.Presenter = MainPresenter(sharedPrefs)

    @Provides
    fun provideAllPetsPresenter(dogsInteractor: DogsInteractor): AllPetsInterface.Presenter = AllPetsPresenter(dogsInteractor)

    @Provides
    fun provideMyPetsPresenter(dogsInteractor: DogsInteractor, sharedPrefs: SharedPrefs): MyPetsInterface.Presenter = MyPetsPresenter(dogsInteractor, sharedPrefs)

    @Provides
    fun provideRegisterPresenter(userInteractor: UserInteractor, sharedPrefs: SharedPrefs): RegisterInterface.Presenter = RegisterPresenter(userInteractor, sharedPrefs)

    @Provides
    fun provideLoginPresenter(userInteractor: UserInteractor, sharedPrefs: SharedPrefs): LoginInterface.Presenter = LoginPresenter(userInteractor, sharedPrefs)

    @Provides
    fun provideEditProfilePresenter(userInteractor: UserInteractor): EditProfileInterface.Presenter = EditProfilePresenter(userInteractor)

    @Provides
    fun provideWelcomePresenter(userInteractor: UserInteractor, sharedPrefs: SharedPrefs): WelcomeInterface.Presenter = WelcomePresenter(userInteractor, sharedPrefs)

    @Provides
    fun providePetDetailsPresenter(dogsInteractor: DogsInteractor, sharedPrefs: SharedPrefs): PetDetailsInterface.Presenter = PetDetailsPresenter(dogsInteractor, sharedPrefs)

    @Provides
    fun provideProfilePresenter(userInteractor: UserInteractor, sharedPrefs: SharedPrefs): ProfileInterface.Presenter = ProfilePresenter(userInteractor, sharedPrefs)

    @Provides
    fun provideAllPetsHolderPresenter(dogsInteractor: DogsInteractor, sharedPrefs: SharedPrefs): AllPetsHolderInterface.Presenter = AllPetsHolderPresenter(dogsInteractor, sharedPrefs)

    @Provides
    fun provideAdoptPetPresenter(dogsInteractor: DogsInteractor, userInteractor: UserInteractor, sharedPrefs: SharedPrefs): AdoptPetInterface.Presenter = AdoptPetPresenter(dogsInteractor, userInteractor, sharedPrefs)

    @Provides
    fun provideMyPetsHolderPresenter(dogsInteractor: DogsInteractor): MyPetsHolderInterface.Presenter = MyPetsHolderPresenter(dogsInteractor)

    @Provides
    fun provideForgotPasswordPresenter(userInteractor: UserInteractor): ForgotPasswordInterface.Presenter = ForgotPasswordPresenter(userInteractor)

    @Provides
    fun provideResetPasswordPresenter(userInteractor: UserInteractor): ResetPasswordInterface.Presenter = ResetPasswordPresenter(userInteractor)

    @Provides
    fun provideEditPasswordPresenter(userInteractor: UserInteractor): EditPasswordInterface.Presenter = EditPasswordPresenter(userInteractor)

    @Provides
    fun provideFilterPetsPresenter(): FilterPetsInterface.Presenter = FilterPetsPresenter()

    @Provides
    fun provideWebViewPresenter(): WebViewInterface.Presenter = WebViewPresenter()

    @Provides
    fun providePetImagesPresenter(): PetImagesInterface.Presenter = PetImagesPresenter()
}