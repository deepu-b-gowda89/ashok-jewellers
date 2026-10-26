# Ashok Jewellers — starter project

This package contains:
- `website/`: a responsive bilingual (English/Kannada) jewellery showcase website.
- `android/`: a native Android Kotlin + Jetpack Compose owner-app project.
- `docs/`: setup notes, limitations, and launch checklist.
- `assets/ashok-jewellers-logo.png`: the supplied full logo.

## Important scope note
This is a functional offline-first MVP starter, not yet a production-connected system.
The website currently uses its own demo catalogue data. The Android app stores owner data locally on the device. Cloud synchronisation between the app and public website, secure cloud backup/restore, and accountant-validated GST invoice compliance are not connected in this starter. Those need to be implemented and tested before using it for real shop records.

## Website
Open `website/index.html` directly in a browser. It supports:
- English/Kannada language toggle (remembers the last choice)
- Search and category filters
- Demo product cards and unavailable labels
- Manual demo rates
- Shop contact/location placeholders
- WhatsApp enquiry links

Edit `website/app.js` near the top to set the real shop phone, WhatsApp number, address, map link, opening hours and demo products. Replace demo products/photos before publishing.

## Android app
Requirements: Android Studio, JDK supplied by Android Studio, internet for the first Gradle sync.
1. Open the `android/` folder in Android Studio.
2. Allow Gradle sync.
3. Run on an Android 8.0+ device/emulator.
4. The app keeps designs, rates, bills and payment entries locally on the phone.
5. PDF invoices are generated and can be shared using Android's share sheet.

## Build limitations / safety
- No cloud backend is configured.
- Do not treat the sample GST invoice as legally compliant until GSTIN, HSN, place-of-supply, tax rules and required declarations are verified for this business by its accountant/tax adviser.
- Back up the phone and test recovery before storing real customer or financial data.
- A finalised bill edit creates an audit entry in this MVP, but production-grade tamper resistance and full audit controls require further work.
- The app does not calculate jewellery prices from metal rates or weights. Enter amounts manually.
