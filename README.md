# DLTunner

DLTunner is a Kotlin-based Android guitar tuner app built with Jetpack Compose. It listens to your guitar through the microphone, detects the active string and pitch, and shows whether the note is flat, sharp, or in tune.

## Features

- Real-time guitar tuning using microphone input
- Support for multiple guitar tunings:
  - Standard
  - Drop D
  - Eb Standard
  - D Standard
- String detection and pitch analysis
- Visual cents meter and tuning status indicators
- Material 3 UI with a modern Android experience
- Splash screen and permission flow for microphone access

## App Overview

This project is an Android application that analyzes audio input and estimates the fundamental frequency of the note being played. The app uses a lightweight pitch detection approach and presents the tuned result directly in the UI.

## Screenshots

The repository includes sample screenshots in the `screenshots/` directory:

- `screenshots/Screenshot_20261002-112509.png`
- `screenshots/Screenshot_20261002-112518.png`
- `screenshots/Screenshot_20261002-112639.png`
- `screenshots/Screenshot_20261002-112655.png`
- `screenshots/Screenshot_20261002_111731.png`
- `screenshots/Screenshot_20261002_111852.png`

## Tech Stack

- Kotlin
- Android Jetpack Compose
- Material 3
- Android Core KTX
- Lifecycle ViewModel
- Audio capture via Android microphone APIs
- Pitch detection logic based on a YIN-inspired implementation

## Project Structure

```text
DLTunner/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/darthleonard/dltunner/
│   │   │   │   ├── core/
│   │   │   │   ├── data/
│   │   │   │   ├── domain/
│   │   │   │   ├── presentation/
│   │   │   │   └── ui/
│   │   │   └── res/
│   │   ├── test/
│   │   └── androidTest/
│   └── build.gradle.kts
├── build.gradle.kts
├── gradle.properties
├── gradlew
├── gradlew.bat
├── settings.gradle.kts
├── screenshots/
├── .gitignore
└── README.md
```

## Getting Started

### Prerequisites

- Android Studio
- JDK 11 or newer
- Android SDK configured for the project
- A physical Android device or emulator

### Run the app

1. Clone the repository:

```bash
git clone https://github.com/darthleonard/DLTunner.git
cd DLTunner
```

2. Open the project in Android Studio.

3. Let Gradle sync and download required dependencies.

4. Select an emulator or connected device.

5. Run the app.

6. When prompted, allow microphone permission.

## How it works

The app listens to the microphone input, analyzes the audio waveform in the app's pitch detection layer, and estimates the frequency of the currently played note. The detected pitch is compared against the selected string's target frequency, and the app displays:

- current note frequency
- cents offset from the target
- whether the string is too low, too high, or in tune
- the selected string state within the tuning layout

## Tuning presets

The app includes several common tuning presets:

- Standard
- Drop D
- Eb Standard
- D Standard

These are defined in `app/src/main/java/com/darthleonard/dltunner/data/tuning/TuningPresets.kt`.

## Notes

- The project is currently configured as a private repository.
- There is no explicit license file included at the moment, so license terms should be confirmed before public distribution or reuse.

## Contributing

Contributions are welcome. If you want to improve the tuner, add more tuning presets, improve detection accuracy, or polish the UI, feel free to open an issue or submit a pull request.

## Contact

Repository owner: `darthleonard`

Project: `darthleonard/DLTunner`
