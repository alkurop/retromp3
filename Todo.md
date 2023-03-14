## TODO's ##
In order of appearance

- V Replace search screen with jpc
- V cleanup dependency
- V refactor permissions
- refactor main activity
- replace dagger with hilt
- progress issue *
- search activity toolbar color * 
- convert to flow and write tests one by one
- add some UI tests
- try to add second track
- convert wavebar to compose
- refactor search toolbar
- refactor search ui
- order strings
- add previews
- re-enable log view *
- V fix search screen settings checkbox *
- fix size jumping of waveform on main view * ( should go away after migration to compose)
- refactor getting audio projection
- show snackbar instead of toast
- fix control buttons ripple *
- check main screen recomposition
- design local progress and state of track
- add bit counter
- cleanup circleci script
- what happens when app is killed by the system (Don't keep activity) in different screens? Is there a need to save state somewhere?
- add slider to settings instead of switch (like in one fit app)
- V merge view model and interactor, by introducing a base vm class
- merge all interactors with view models
- do we still need RangeBarResetBus? If it is removed, will there be bugs? *
- convert player and recorder to flow
- properly manual tests before releasing
- cover buttons with ui automation
- check low storage
- zoom in waveform mode
- add mp3 tags to a cropped file
- add search
- V reassamble delete rename popups in JPC, add to menu
- add settings to bottom menu
- move open file to tool bar
- fix circle ci
- cleanup and refactor search paging, cover with tests. Filtering should happen on data side, not on view
- for popups move dismiss state to composable


* means bug
V means completed
