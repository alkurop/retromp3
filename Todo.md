## TODO's ##

In order of appearance

V means completed
X means can't be done (please provide explanation)

### Bugs:



- check recording on phone
- fix cropping
- fix logs
- do we still need RangeBarResetBus? If it is removed, will there be bugs? *
- what happens when app is killed by the system (Don't keep activity) in different screens? Is there
  a need to save state somewhere?
- cleanup items UI in the search results (make smaller)
- check main screen recomposition
- re-enable log view
- fix size jumping of waveform on main view * ( should go away after migration to compose)
- time counter going in 2 lines
- check audio player issues

- V search activity toolbar color
- V bug with paging loading
- V startup file bug
- V fix circle ci
- V progress issue
- V fix search screen settings checkbox
- V fix play button not working
- V fix control buttons ripple

### Refactoring:

- add some UI tests for buttons
- convert wavebar to compose
- order strings
- add previews
- Crop interactor - fix unclear flow with canCropFileRepo flow
- cleanup toast repo
- add coroutines to database
- rename popup remove name state from interactor, retain name in composable
- for popups move dismiss state to composable
- add settings to bottom menu
- convert player and recorder to flow
- refactor getting audio projection
- refactor main activity
- StartPlaybackUCSuspend why here using length of file not duration in the progress?


- V cleanup and refactor search paging, cover with tests. Filtering should happen on data side, not on
  view
- V cover WavetableSummerTest
- V move open file to menu bar
- V player recorder flow api
- V convert remaining classes to flow
- V convert JoinedProgressMapper, AudioStateMapper to flow
- V refactor search toolbar
- V refactor search ui
- V convert to flow and write tests one by one
- V replace dagger with hilt
- V Replace search screen with jpc
- V cleanup dependency
- V refactor permissions
- V reassamble delete rename popups in JPC, add to menu
- X (testing with viewmodel scope consumes errors) merge all interactors with view models
- X (testing with viewmodel scope consumes errors) merge view model and interactor, by introducing a
  base vm class

### Features:

- create scoped track
- add mp3 tags to a cropped file
- check low storage
- zoom in waveform mode V add search
- cover buttons with ui automation
- add slider to settings instead of switch (like in one fit app)
- design local progress and state of track
- add bit counter
- show snackbar instead of toast
- make record button bigger then others
- add benchmark tests
- integrate jococo
- add dependency diagram
- add static analysis
- V cleanup circleci script

