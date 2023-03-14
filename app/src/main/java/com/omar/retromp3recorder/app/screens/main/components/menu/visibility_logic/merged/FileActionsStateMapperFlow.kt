package com.omar.retromp3recorder.app.screens.main.components.menu.visibility_logic.merged

import com.omar.retromp3recorder.app.screens.main.components.menu.MenuContract
import com.omar.retromp3recorder.app.screens.main.components.menu.visibility_logic.MenuVisibilityMapperFlow
import com.omar.retromp3recorder.app.screens.main.components.menu.visibility_logic.file.DeleteFileMenuStateMapperFlow
import com.omar.retromp3recorder.app.screens.main.components.menu.visibility_logic.file.RenameFileMenuStateMapperFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class FileActionsStateMapperFlow @Inject constructor(
    private val deleteFileMenuStateMapper: DeleteFileMenuStateMapperFlow,
    private val renameFileMenuStateMapper: RenameFileMenuStateMapperFlow
) : MenuVisibilityMapperFlow {
    override fun flow(): Flow<List<MenuContract.Item>> {
        return combine(
            listOf(
                deleteFileMenuStateMapper.flow(),
                renameFileMenuStateMapper.flow()
            )
        ) { array ->
            array.toList().flatten()
        }
    }
}

