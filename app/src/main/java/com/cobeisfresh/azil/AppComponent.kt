package com.cobeisfresh.azil

import com.cobeisfresh.azil.analytics.AnalyticsModule
import com.cobeisfresh.azil.network_interaction.NetworkInteractionModule
import com.cobeisfresh.azil.presentation.PresentationModule
import com.cobeisfresh.azil.shared_prefs.SharedPrefsModule
import com.cobeisfresh.azil.ui.adopt_pet.AdoptPetActivity
import com.cobeisfresh.azil.ui.all_pets.AllPetsFragment
import com.cobeisfresh.azil.ui.all_pets.filter.FilterPetsActivity
import com.cobeisfresh.azil.ui.all_pets.list.AllPetsHolder
import com.cobeisfresh.azil.ui.base.BaseActivity
import com.cobeisfresh.azil.ui.base.BaseFragment
import com.cobeisfresh.azil.ui.edit_password.EditPasswordActivity
import com.cobeisfresh.azil.ui.edit_profile.EditProfileActivity
import com.cobeisfresh.azil.ui.forgot_reset_password.forgot_password.ForgotPasswordActivity
import com.cobeisfresh.azil.ui.forgot_reset_password.reset_password.ResetPasswordActivity
import com.cobeisfresh.azil.ui.login.LoginActivity
import com.cobeisfresh.azil.ui.main.MainActivity
import com.cobeisfresh.azil.ui.my_pets.MyPetsFragment
import com.cobeisfresh.azil.ui.my_pets.list.MyPetsHolder
import com.cobeisfresh.azil.ui.pet_details.PetDetailsActivity
import com.cobeisfresh.azil.ui.pet_details.pet_images.PetImagesActivity
import com.cobeisfresh.azil.ui.profile.ProfileFragment
import com.cobeisfresh.azil.ui.register.RegisterActivity
import com.cobeisfresh.azil.ui.splash.SplashActivity
import com.cobeisfresh.azil.ui.web_view.WebViewActivity
import com.cobeisfresh.azil.ui.welcome.WelcomeActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [(AppModule::class), (AnalyticsModule::class), (NetworkInteractionModule::class), (SharedPrefsModule::class), (PresentationModule::class)])
interface AppComponent {

    fun inject(app: App)

    fun inject(splashActivity: SplashActivity)

    fun inject(welcomeActivity: WelcomeActivity)

    fun inject(registerActivity: RegisterActivity)

    fun inject(loginActivity: LoginActivity)

    fun inject(mainActivity: MainActivity)

    fun inject(allPetsFragment: AllPetsFragment)

    fun inject(myPetsFragment: MyPetsFragment)

    fun inject(petDetailsActivity: PetDetailsActivity)

    fun inject(profileFragment: ProfileFragment)

    fun inject(allPetsHolder: AllPetsHolder)

    fun inject(adoptPetActivity: AdoptPetActivity)

    fun inject(myPetsHolder: MyPetsHolder)

    fun inject(forgotPasswordActivity: ForgotPasswordActivity)

    fun inject(resetPasswordActivity: ResetPasswordActivity)

    fun inject(editProfileActivity: EditProfileActivity)

    fun inject(editPasswordActivity: EditPasswordActivity)

    fun inject(filterPetsActivity: FilterPetsActivity)

    fun inject(webViewActivity: WebViewActivity)

    fun inject(petImagesActivity: PetImagesActivity)

    fun inject(baseActivity: BaseActivity)

    fun inject(baseFragment: BaseFragment)
}