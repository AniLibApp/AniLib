# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]
### Fixed
- Replaced library with a activity for splash screen implementation. For API >= 31 can't control splash dark/light theme from app. [#102](https://github.com/AniLibApp/AniLib/pull/102/files)

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

[v2.0.1]: https://github.com/AniLibApp/AniLib/compare/v2.0.0...v2.0.1
