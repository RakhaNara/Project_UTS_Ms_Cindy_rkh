# Implementation Plan - Project Fix, Build, and Run

This plan outlines the steps to configure `gradle.properties`, adjust `app/build.gradle.kts` dependencies, verify the project structure, and ultimately run the application on an emulator or device as requested.

## Proposed Changes

### Configuration

#### [MODIFY] [gradle.properties](file:///C:/Users/Rakha%20naraya/AndroidStudioProjects/ProjectUTSMsCindy/gradle.properties)
- Add `android.useAndroidX=true` and `android.enableJetifier=true` to ensure compatibility with AndroidX libraries.

#### [MODIFY] [app/build.gradle.kts](file:///C:/Users/Rakha%20naraya/AndroidStudioProjects/ProjectUTSMsCindy/app/build.gradle.kts)
- Downgrade `androidx.lifecycle:lifecycle-viewmodel-compose` from `2.8.6` to `2.7.0` as specified.
- Ensure `kotlin-kapt` is correctly applied.

### Verification (Package & Import)

I have already verified that:
- `Entities.kt`, `AppDao.kt`, `AppDatabase.kt`, and `MainActivity.kt` are in `com.example.projectutsmscindy`.
- `LoanRepository.kt` is in `com.example.projectutsmscindy.repository`.
- `LoanViewModel.kt` and `MainScreen.kt` are in `com.example.projectutsmscindy.ui`.
- Imports across these files are correctly configured to link the components.

## Execution Plan

1.  **Update `gradle.properties`**: Apply the required AndroidX and Jetifier settings.
2.  **Update `app/build.gradle.kts`**: Adjust the ViewModel Compose version.
3.  **Gradle Sync**: Run a full sync to ensure the environment is ready.
4.  **Build & Install**: Run `./gradlew installDebug` to compile and install the app.
5.  **Launch**: Start the `MainActivity` on the connected device/emulator.

## Verification Plan

### Automated Verification
- **Gradle Sync**: Verify successful completion.
- **Gradle Build**: Run `./gradlew assembleDebug` to check for compilation errors.

### Manual Verification
- **App Launch**: Confirm the application opens on the emulator and displays the "Rental & Loan Tracker" UI.
