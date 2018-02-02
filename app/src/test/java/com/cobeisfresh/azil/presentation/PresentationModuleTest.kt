package com.cobeisfresh.azil.presentation

import com.cobeisfresh.azil.network_interaction.DogsInteractor
import com.cobeisfresh.azil.network_interaction.UserInteractor
import com.cobeisfresh.azil.shared_prefs.SharedPrefs
import com.nhaarman.mockito_kotlin.mock
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

/**
 * Created by Zerina Salitrezic on 13/10/2017.
 */

class PresentationModuleTest {

    private lateinit var presentationModule: PresentationModule

    private val dogsInteractor: DogsInteractor = mock()
    private val userInteractor: UserInteractor = mock()
    private val sharedPrefs: SharedPrefs = mock()

    @Before
    fun setUp() {
        presentationModule = PresentationModule()
    }

    @Test
    fun moduleNotNull() {
        assertNotNull(presentationModule)
    }

    @Test
    fun provideMainPresenterNotNull() {
        assertNotNull(presentationModule.provideMainPresenter(sharedPrefs))
    }

    @Test
    fun provideAllPetsPresenterNotNull() {
        assertNotNull(presentationModule.provideAllPetsPresenter(dogsInteractor))
    }

    @Test
    fun provideMyPetsPresenterNotNull() {
        assertNotNull(presentationModule.provideMyPetsPresenter(dogsInteractor, sharedPrefs))
    }

    @Test
    fun provideRegisterPresenterNotNull() {
        assertNotNull(presentationModule.provideRegisterPresenter(userInteractor, sharedPrefs))
    }

    @Test
    fun provideLoginPresenterNotNull() {
        assertNotNull(presentationModule.provideLoginPresenter(userInteractor, sharedPrefs))
    }

    @Test
    fun provideEditProfilePresenterNotNull() {
        assertNotNull(presentationModule.provideEditProfilePresenter(userInteractor))
    }

    @Test
    fun provideLoginRegisterPresenterNotNull() {
        assertNotNull(presentationModule.provideWelcomePresenter(userInteractor, sharedPrefs))
    }

    @Test
    fun providePetDetailsPresenterNotNull() {
        assertNotNull(presentationModule.providePetDetailsPresenter(dogsInteractor, sharedPrefs))
    }

    @Test
    fun provideProfilePresenterNotNull() {
        assertNotNull(presentationModule.provideProfilePresenter(userInteractor, sharedPrefs))
    }

    @Test
    fun provideAllPetsHolderPresenterNotNull() {
        assertNotNull(presentationModule.provideAllPetsHolderPresenter(dogsInteractor, sharedPrefs))
    }

    @Test
    fun provideAdoptPetPresenterNotNull() {
        assertNotNull(presentationModule.provideAdoptPetPresenter(dogsInteractor, userInteractor, sharedPrefs))
    }

    @Test
    fun provideMyPetsHolderPresenterNotNull() {
        assertNotNull(presentationModule.provideMyPetsHolderPresenter(dogsInteractor))
    }

    @Test
    fun provideForgotPasswordPresenterNotNull() {
        assertNotNull(presentationModule.provideForgotPasswordPresenter(userInteractor))
    }

    @Test
    fun provideResetPasswordPresenterNotNull() {
        assertNotNull(presentationModule.provideResetPasswordPresenter(userInteractor))
    }

    @Test
    fun provideEditPasswordPresenterNotNull() {
        assertNotNull(presentationModule.provideEditPasswordPresenter(userInteractor))
    }

    @Test
    fun provideFilterPetsPresenterNotNull() {
        assertNotNull(presentationModule.provideFilterPetsPresenter())
    }

    @Test
    fun provideWebViewPresenterNotNull() {
        assertNotNull(presentationModule.provideWebViewPresenter())
    }

    @Test
    fun providePetImagesPresenterNotNull() {
        assertNotNull(presentationModule.providePetImagesPresenter())
    }
}