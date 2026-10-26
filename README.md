# 💎 Ashok Jewellers

**A bilingual jewellery showcase website and native Android administration app.**

> 🚧 Work in Progress — This project is actively being developed. The website and Android app are not yet connected to a shared backend.

Ashok Jewellers is a digital project designed to bring a jewellery business online through a customer-facing website and a separate Android app for shop administration.

## ✨ Project Overview

### 🌐 Jewellery Website

A responsive jewellery showcase built with HTML, CSS, and JavaScript.

- Kannada and English language toggle
- Product search and category filters
- Jewellery product cards with demo content
- WhatsApp enquiry links
- Responsive layout for different screen sizes

### 📱 Android Admin App

A native Android application built with Kotlin and Jetpack Compose.

- Local jewellery design records
- Metal-rate entry and management
- Billing and payment records
- PDF invoice generation
- Offline-first, local-data workflow

*Features are subject to ongoing development and testing.*

## 🛠️ Technology Stack

| Component | Technologies |
|---|---|
| Website | HTML, CSS, JavaScript |
| Android app | Kotlin, Jetpack Compose |
| Build system | Gradle with Kotlin DSL |
| Data storage | Local storage in the Android app |
| Cloud backend | Not configured |
| Website–app integration | Not implemented yet |

## 📁 Project Structure

```text
AshokJewellers_Starter/
├── android/
│   ├── app/
│   ├── gradle/
│   ├── build.gradle.kts
│   └── settings.gradle.kts
├── assets/
├── docs/
├── website/
│   ├── assets/
│   ├── index.html
│   ├── styles.css
│   ├── app.js
│   └── README.md
├── .gitignore
└── README.md
```

## 🚀 Getting Started

### Run the website

1. Clone or download this repository.
2. Open the `website/` directory.
3. Open `index.html` in a modern web browser.

No frontend build step is required for the current static website.

### Open the Android app

1. Install Android Studio.
2. Open the `android/` directory as an existing project.
3. Allow Gradle to sync and download required dependencies.
4. Run the app on a compatible Android emulator or device.

Build requirements may vary with the project's Gradle and Android Gradle Plugin configuration.

## ⚠️ Current Limitations

- The website uses demo product data.
- The website and Android app do not synchronize data.
- No cloud backend or authentication system is configured.
- Business details, jewellery prices, rates, tax calculations and invoice compliance require verification before real-world use.
- The application must be tested thoroughly before deployment.

## 🗺️ Roadmap

- [ ] Refine the website UI and mobile responsiveness
- [ ] Complete and test Android administration features
- [ ] Replace demo content with approved business data
- [ ] Design a secure backend and data synchronization workflow
- [ ] Add appropriate authentication and access controls
- [ ] Test billing and invoice calculations
- [ ] Deploy the website and document the release process

## 🎯 Project Goal

Build a useful digital platform for a jewellery business while learning practical web development, Android development, application architecture, and software engineering.

## 👨‍💻 Developer

**Deepu Bettaswamy Gowda**

Computer Science and Design Engineering Student

- GitHub: [@deepu-b-gowda89](https://github.com/deepu-b-gowda89)

---

*Built with curiosity, creativity, and a commitment to learning.*
