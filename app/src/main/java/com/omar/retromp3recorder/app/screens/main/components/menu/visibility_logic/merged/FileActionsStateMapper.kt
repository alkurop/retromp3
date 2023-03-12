package com.omar.retromp3recorder.app.screens.main.components.menu.visibility_logic.merged

import com.omar.retromp3recorder.app.screens.main.components.menu.MenuContract
import com.omar.retromp3recorder.app.screens.main.components.menu.visibility_logic.MenuVisibilityMapper
import com.omar.retromp3recorder.app.screens.main.components.menu.visibility_logic.file.DeleteFileMenuStateMapper
import com.omar.retromp3recorder.app.screens.main.components.menu.visibility_logic.file.RenameFileMenuStateMapper
import com.omar.retromp3recorder.app.screens.main.components.menu.visibility_logic.mapToMenuList
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class FileActionsStateMapper @Inject constructor(
    private val deleteFileMenuStateMapper: DeleteFileMenuStateMapper,
    private val renameFileMenuStateMapper: RenameFileMenuStateMapper
) : MenuVisibilityMapper {
    override fun observe(): Observable<List<MenuContract.Item>> =
        Observable.combineLatest(
            listOf(
                deleteFileMenuStateMapper.observe(),
                renameFileMenuStateMapper.observe()
            )
        ) { array ->
            array.mapToMenuList()
        }
}

