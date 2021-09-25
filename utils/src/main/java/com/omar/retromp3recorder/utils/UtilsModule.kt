package com.omar.retromp3recorder.utils

import com.omar.retromp3recorder.utils.Constants.MAIN_THREAD
import dagger.Module
import dagger.Provides
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Named

@Module
class UtilsModule {
    @Provides
    fun provideScheduler(): Scheduler {
        return Schedulers.io()
    }

    @Provides
    @Named(MAIN_THREAD)
    fun provideMainThreadScheduler(): Scheduler {
        return AndroidSchedulers.mainThread()
    }

    @Provides
    fun provideFilePathGenerator(filePathGeneratorImpl: FilePathGeneratorImpl): FilePathGenerator =
        filePathGeneratorImpl

    @Provides
    fun provideFileLister(fileListerImpl: FileListerImpl): FileLister = fileListerImpl

    @Provides
    fun provideFileNonEmptyChecker(fileEmptyCheckerImpl: FileEmptyCheckerImpl): FileEmptyChecker =
        fileEmptyCheckerImpl

    @Provides
    fun providePermissionChecker(permissionCheckerImpl: PermissionCheckerImpl): PermissionChecker =
        permissionCheckerImpl

    @Provides
    fun provideFileDeleter(fileDeleter: FileDeleterImpl): FileDeleter = fileDeleter

    @Provides
    fun provideFileRenamer(fileRenamer: FileRenameImpl): FileRenamer = fileRenamer

    @Provides
    fun provideDirCreator(dirCreatorImpl: DirCreatorImpl): DirCreator = dirCreatorImpl

    @Provides
    fun provideMp3MetadataEditor(mp3TagsEditorImpl: Mp3TagsEditorImpl): Mp3TagsEditor =
        mp3TagsEditorImpl

}
