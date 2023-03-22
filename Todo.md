## TODO's ##
In order of appearance


### Bugs:
-  fix search screen settings checkbox 
- fix play button not working *
- check recording on phone *
- fix cropping *
- fix logs *
- V fix circle ci
- do we still need RangeBarResetBus? If it is removed, will there be bugs? *
- what happens when app is killed by the system (Don't keep activity) in different screens? Is there a need to save state somewhere?
- fix control buttons ripple *
- check main screen recomposition
- re-enable log view *
- fix size jumping of waveform on main view * ( should go away after migration to compose)
- V progress issue *
- search activity toolbar color *


### Refactoring:
- V convert to flow and write tests one by one
- add some UI tests for buttons
- convert wavebar to compose
- refactor search toolbar
- refactor search ui
- order strings
- add previews
- V replace dagger with hilt
- V Replace search screen with jpc
- V cleanup dependency
- V refactor permissions
- convert remaining classes to flow
- convert JoinedProgressMapper, AudioStateMapper to flow
- Crop interactor - fix unclear flow with canCropFileRepo flow
- cleanup toast repo
- player recorder flow api
- add coroutines to database
- rename popup remove name state from interactor, retain name in composable
- for popups move dismiss state to composable
- cleanup and refactor search paging, cover with tests. Filtering should happen on data side, not on view
- add settings to bottom menu
- ? move open file to tool bar
- V reassamble delete rename popups in JPC, add to menu
- convert player and recorder to flow
- X (testing with viewmodel scope consumes errors) merge all interactors with view models
- X (testing with viewmodel scope consumes errors) merge view model and interactor, by introducing a base vm class
- refactor getting audio projection
- refactor main activity



### Features:
- create scoped track
- add mp3 tags to a cropped file
- check low storage
- zoom in waveform mode V add search
- cover buttons with ui automation
- add slider to settings instead of switch (like in one fit app)
- V cleanup circleci script
- design local progress and state of track
- add bit counter
- show snackbar instead of toast



V means completed
X means can't be done (please provide explanation)


