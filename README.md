# 🚚 MovEase – Buy & Move Simplified

**MovEase** is a two‑sided marketplace Android app that helps users in Lahore buy a new house and move in seamlessly.  
It combines real estate listings, labour services, packing materials, and transport into a **single optimised moving plan**.

---

## ✨ Key Features

- 🔐 **Firebase Authentication** – Email/password login & signup with role selection (Mover / Service Provider)
- 📋 **Moving Plan Generator** – Input area, budget, date, bedrooms; get top 5 plans ranked by cost & rating
- 🧠 **nCr Combination Engine** – Generates all possible (house × labour × packing × transport) bundles, filters by budget, scores, and picks the best
- ✅ **Advantages & Disadvantages** – Human‑readable pros/cons for each plan (e.g. “highly rated house”, “no parking”)
- 🗺️ **Free Map View** – OpenStreetMap + Leaflet.js inside a WebView (no API key required)
- 🔔 **Push Notifications** – Firebase Cloud Messaging for real‑time alerts
- 🏪 **Dynamic Marketplace** – Service providers can add/edit/delete their own labour, packing, or transport offers (Firestore)
- 🔄 **Real‑time Updates** – Pull‑to‑refresh + auto‑refresh every 60 seconds
- 🎨 **Material Design UI** – Theming, icons, accessibility, and responsive layouts
- 🚗 **Animated Splash Screen** – Truck moving between houses with a package
- 📍 **Real Zameen.com Data** – Scraped Lahore property listings used as mock API

---

## 📱 Screenshots

<!-- Add your own screenshots after the demo video -->
| Splash | Login | Plan Input | Plan List | Map |
|--------|-------|------------|-----------|-----|
| ![Splash](screenshots/splash.png) | ![Login](screenshots/login.png) | ![Input](screenshots/input.png) | ![Plans](screenshots/plans.png) | ![Map](screenshots/map.png) |

---

## 🧱 Architecture & Tech Stack

- **Language:** Java
- **UI:** XML, Material Components, RecyclerView, CardView
- **Authentication:** Firebase Auth (Email/Password)
- **Database:** Cloud Firestore
- **Notifications:** Firebase Cloud Messaging (FCM)
- **Networking:** Retrofit2 + OkHttp3 (mock interceptor for local JSON)
- **Map:** WebView + Leaflet.js + OpenStreetMap (no Google Maps key)
- **Build:** Gradle (Groovy DSL), minSdk 24

### Project Structure
