# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

## [v2.1.0]
### Added
- Add badge icon to season screen as well.

### Fixed
- Fix crash on score change in MediaListEntryComponent.
- Fixed crash on stats screen.

## [v2.0.10]
### Added
- Added MAL export service (experimental).
- Added badge to filter icon button in home, media list and activity screen.
- Added monochrome icon for the launcher.
- Added badge to notification icon.

### Changed
- Updated chart library.
- Refactored gradle files to use version catalogs.
- Min SDK 23

## [v2.0.9]
## Fixed
- Incorrect data persists when searching in or switching between anime/manga lists. \n

## [v2.0.8]
## Added
- Added option to open a random entry from your media list.
- Added compare with your media list using another user's list.

## Fixed
- Fixed issue where manga volumes wouldn't display correctly beyond volume 10.


## [v2.0.7]
## Added
- Added Polish language by paplyke

## Fixed
- Fix crash with new API 35 when removing last item from list (issue)[https://youtrack.jetbrains.com/issue/KT-71375/Prevent-Kotlins-removeFirst-and-removeLast-from-causing-crashes-on-Android-14-and-below-after-upgrading-to-Android-API-Level-35]


## [v2.0.6] 
### Added
- Add color picker [#101](https://github.com/AniLibApp/AniLib/issues/101)
- Added a one-time purchase to remove ads and support the app — thank you for your support! 💖

### Fixed
- Improved text compatibility with system font size settings for better readability across all devices.
- Fix error when browsing media overview after logout
- Add worker for Airing Schedule Widget and store it in data store.

## [v2.0.3] - 2024-11-18
### Added
- Add scroll-to-top functionality from home screen's bottom navigation bar.
- Add grid compact in media list display mode (previously known as Classic Mode)
- Add different widget appearance settings in widget settings.
- Add missing filter in media list filter bottomsheet
- Add refresh genre and tags button to sync with anilist inside filter setting

### Fixed
- Fix start & end dates not automatically added on watching and completing list. [#110](https://github.com/AniLibApp/AniLib/issues/110)
- Increasing progress will now stop resetting status and other fields. [#89](https://github.com/AniLibApp/AniLib/issues/89)
- Fix bug in title comparison caused by inconsistent casing

## [v2.0.2] - 2024-11-9
### Added
- Added filter settings
- Exclude tags and genre for anime and manga separately. Manually adjustment to this filter in search will stop adding default filters for anime and manga separately. [#74](https://github.com/AniLibApp/AniLib/issues/74)
- Excluded filter will be added when reloading previous filter [#75](https://github.com/AniLibApp/AniLib/issues/75)
- Add Doujin, On-List, etc filter on search filter [#106](https://github.com/AniLibApp/AniLib/issues/106)
- Show episodes and chapters in almost all cards [#103](https://github.com/AniLibApp/AniLib/issues/103)
- Add genre and tags filter in explore and season
- Update some themes in filter [#99](https://github.com/AniLibApp/AniLib/issues/99)

### Fixed
- Replaced library with a activity for splash screen implementation. For API >= 31 can't control splash dark/light theme from app. [#102](https://github.com/AniLibApp/AniLib/pull/102/files)
- Clicking on a tag of a manga will start a search of that tag on the manga section [#72](https://github.com/AniLibApp/AniLib/issues/72)
- Show none in year, season inside explore filters
- Date picker using wrong date in list editor and airing [#107](https://github.com/AniLibApp/AniLib/issues/107)

### Removed
- Removed CoreX splash library.

## [v2.0.1] - 2024-10-15
### Added
- Add display scaling to make component small [#98](https://github.com/AniLibApp/AniLib/pull/98/files)

### Fixed
- fix crash on comparing list entry
- fix crashing app on too many request on app start
- fix error when filter is immediately applied after login. [#94](https://github.com/AniLibApp/AniLib/issues/94)
- fix: add many and other in locales to fix crash with plurals

## [v2.0.0] - 2024-10-12
- **Complete App Rewrite**: Improved performance and stability with a brand-new codebase.
- **Enhanced UI/UX**: Enjoy a more modern and user-friendly interface for a smoother experience.
- **Note**: Any missing feature like filter settings will be added later on. ML translate is removed and won\'t be added.
- **Enjoy AniLib!**

[v2.0.9]: https://github.com/AniLibApp/AniLib/compare/v2.0.8...v2.0.9
[v2.0.8]: https://github.com/AniLibApp/AniLib/compare/v2.0.7...v2.0.8
[v2.0.7]: https://github.com/AniLibApp/AniLib/compare/v2.0.6...v2.0.7
[v2.0.6]: https://github.com/AniLibApp/AniLib/compare/v2.0.3...v2.0.6
[v2.0.3]: https://github.com/AniLibApp/AniLib/compare/v2.0.2...v2.0.3
[v2.0.2]: https://github.com/AniLibApp/AniLib/compare/v2.0.1...v2.0.2
[v2.0.1]: https://github.com/AniLibApp/AniLib/compare/v2.0.0...v2.0.1
