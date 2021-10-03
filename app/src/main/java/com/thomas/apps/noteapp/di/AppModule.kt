package com.thomas.apps.noteapp.di

import android.app.Application
import androidx.room.Room
import com.thomas.apps.noteapp.feature_login.data.data_source.LoginApi
import com.thomas.apps.noteapp.feature_login.data.repository.LoginRepositoryImpl
import com.thomas.apps.noteapp.feature_login.domain.repository.LoginRepository
import com.thomas.apps.noteapp.feature_login.domain.use_case.Login
import com.thomas.apps.noteapp.feature_login.domain.use_case.LoginUseCases
import com.thomas.apps.noteapp.feature_login.domain.use_case.SaveUser
import com.thomas.apps.noteapp.feature_note.data.data_source.NoteDatabase
import com.thomas.apps.noteapp.feature_note.data.repository.NoteRepositoryImpl
import com.thomas.apps.noteapp.feature_note.domain.repository.NoteRepository
import com.thomas.apps.noteapp.feature_note.domain.use_case.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideNoteDatabase(app: Application): NoteDatabase {
        return Room.databaseBuilder(
            app,
            NoteDatabase::class.java,
            NoteDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideNoteRepository(db: NoteDatabase): NoteRepository {
        return NoteRepositoryImpl(db.noteDao)
    }

    @Provides
    @Singleton
    fun provideNoteUseCases(repository: NoteRepository): NoteUseCases {
        return NoteUseCases(
            getNotes = GetNotes(repository),
            deleteNote = DeleteNote(repository),
            addNote = AddNote(repository),
            getNote = GetNote(repository)
        )
    }

    @Provides
    @Singleton
    fun provideLoginApi(): LoginApi {
        return LoginApi()
    }

    @Provides
    @Singleton
    fun provideLoginRepository(loginApi: LoginApi): LoginRepository {
        return LoginRepositoryImpl(loginApi)
    }

    @Provides
    @Singleton
    fun provideLoginUseCase(loginRepository: LoginRepository, app: Application): LoginUseCases {
        return LoginUseCases(
            login = Login(loginRepository),
            saveUser = SaveUser(app.applicationContext)
        )
    }
}