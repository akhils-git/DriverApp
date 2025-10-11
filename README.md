# GirfalcoDriverApp

A native Android application for Girfalco drivers built with Kotlin and Material Design.

## 🚗 Project Overview

GirfalcoDriverApp is a modern Android application designed for drivers in the Girfalco platform. Built with the latest Android development practices using Kotlin, Material Design 3, and AndroidX libraries.

## 🛠️ Technical Stack

- **Language**: Kotlin
- **Minimum SDK**: API 24 (Android 7.0)
- **Target SDK**: API 34 (Android 14)
- **Architecture**: Modern Android with View Binding
- **UI Framework**: Material Design 3
- **Navigation**: Navigation Component
- **Build System**: Gradle with Kotlin DSL

## 📱 Features

- Modern Material Design 3 UI
- Navigation Component for seamless screen transitions
- Lifecycle-aware components
- View Binding for type-safe view references
- Ready for driver-specific functionality implementation

## 🔧 Development Requirements

- **Android Studio**: 2023.1.1 or later
- **JDK**: 17 (OpenJDK recommended)
- **Gradle**: 8.0+
- **Android SDK**: API 34

## 🚀 Getting Started

### Prerequisites
1. Install Android Studio
2. Install JDK 17
3. Set up Android SDK (API 34)

### Build and Run
1. Open project in Android Studio
2. Sync Gradle files
3. Run on device or emulator

### Gradle Commands
```bash
# Build debug APK
./gradlew assembleDebug

# Run tests
./gradlew test

# Install on connected device
./gradlew installDebug
```

## 📁 Project Structure

```
app/
├── src/main/
│   ├── java/com/girfalco/driverapp/
│   │   └── MainActivity.kt
│   ├── res/
│   │   ├── layout/
│   │   ├── values/
│   │   └── drawable/
│   └── AndroidManifest.xml
├── build.gradle
└── proguard-rules.pro
```

## 🎨 UI Components

- **Material Toolbar**: App navigation and branding
- **Floating Action Button**: Quick actions
- **Navigation Host**: Fragment container for navigation
- **Material Design 3 Theming**: Consistent visual design

## 📋 Development Status

- ✅ Project structure created
- ✅ Basic Material Design setup
- ✅ Navigation component integrated
- ✅ Build configuration ready
- 🔄 Ready for feature implementation

## 🤝 Contributing

1. Follow Android coding standards
2. Use Kotlin for all new features
3. Maintain Material Design consistency
4. Write unit tests for business logic

## 📄 License

This project is part of the Girfalco platform.

---
**Generated**: October 2025 | **Platform**: Native Android