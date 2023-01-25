package com.omar.retromp3recorder.app.ui.menu.visibility_logic

import com.omar.retromp3recorder.app.ui.menu.MenuContract
import io.reactivex.rxjava3.core.Observable

interface MenuVisibilityMapper {
    fun observe(): Observable<List<MenuContract.Item>>
}

@Suppress("UNCHECKED_CAST")
fun Array<Any>.mapToMenuList(): List<MenuContract.Item> {
   return this.map { it as List<MenuContract.Item> }.flatten()
}