
## TODO's ##

In order of appearance

V means completed
X means can't be done (please provide explanation)

### Bugs:

- do we still need RangeBarResetBus? If it is removed, will there be bugs? *
- what happens when app is killed by the system (Don't keep activity) in different screens? Is there
  a need to save state somewhere?
- check main screen recomposition
- check audio player issues
- X investigate crash when saving file - being skipped for current realse. Will be reenabled if present in crash reports
- X align log duration with real duration - partially fixed. DIff is 300 millis, which is probably ok 
- issue with "record message" popping up sometimes

- V fix logs
- V time counter going in 2 lines
- V make audio title smaller
- V correct record counter
- V fix cropping
- V what's with waveform when failed cropping?
- V add mp3 tags to a cropped file
- V range enabled disabled on menu icon
- V check recording on phone
- V fix size jumping of waveform on main view * ( should go away after migration to compose)
- V search activity toolbar color
- V bug with paging loading
- V startup file bug
- V fix circle ci
- V progress issue
- V fix search screen settings checkbox
- V fix play button not working
- V fix control buttons ripple
- V fix counter with range
- V fix position when range changes

### Refactoring:

- V order strings
- X add previews - being skipped
- X cleanup toast repo - being skipped
- X rename popup remove name state from interactor, retain name in composable - being skipped
- X - being skipped for popups move dismiss state to composable
- convert player and recorder to flow
- V StartPlaybackUCSuspend why here using length of file not duration in the progress?
- show snackbar instead of toast
- make record button bigger then others
- add dependency diagram
- main interactor tests
- billing views tests


- V Crop interactor - fix unclear flow with canCropFileRepo flow
- V add coroutines to database
- V add settings to bottom menu
- V refactor getting audio projection
- V refactor main activity
- V convert wavebar to compose
- V cleanup items UI in the search results (make smaller)
- V cleanup and refactor search paging, cover with tests. Filtering should happen on data side, not
  on
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
- check low storage
- zoom in waveform mode V add search
- cover buttons with ui automation
- design local progress and state of track
- add bit counter
- add benchmark tests
- integrate jococo
- add static analysis
- loading screen
- V cleanup circleci script
- V enroll into 15% google pay program
- split testing
- website with a blog (similar theme)
- firebase analytics
- firebase feature flags
- add mob 
- analytics
- add developer account profile
- transfer ownership to nl
- add effects
- explore bundles in order to add AI feature as a separate bundle


#### payments
- V save purcheses in database
- V consume, and let use if user is offline
- add eventually the loading screen
- V log how many useges were made, with or without subscription, payed or not payed (ab test this maybe)
- add google analytics, or any other analytics tool
- V add product counter (available at the moment) to settings
- X add messaging (added but does not work during testing, this is a minor feature and is being skipped)
- V test payment with different response type
- X contact support to transfer accoutn to NL - no need, just create an other account
- create NL account to receive payments


mvp
- network
- widget
- multy track
- audio effects
- online synchronisation
- most importantly - payment system integration
- collapsable list items
- [native audio player](https://developer.android.com/ndk/guides/audio/aaudio/aaudio)
- google add words
- write downe hepothsis, organize documentation
- add voice recognition AI

