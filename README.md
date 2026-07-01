# 🚚 MovEase – Buy & Move Simplified

**MovEase** is a two‑sided marketplace Android app that helps users in Lahore **buy a new house and move in** or **move into a house they already own** by combining real estate listings, labour, packing, and transport services into a single optimised moving plan.

---

## ✨ Key Features

- 🔐 **Firebase Authentication** – Email/password sign‑up & login with role selection (Mover / Service Provider). Mover is the default role.
- 🧠 **Smart Plan Generator**  
  - Cartesian product of House × Labour × Packing × Transport  
  - Filters by budget, scores by cost & rating  
  - Top‑5 plans with **advantages and disadvantages**  
  - Rating shown out of 5 ⭐  
- 🏠 **Already Have a House?** – Toggle to skip property search; only service combinations are generated.
- 📍 **Real Zameen.com Data** – 20 recent Lahore property listings (price, beds, area) baked in via mock API.
- 🌐 **Dynamic Marketplace** – Providers add services (labour, packing, transport) which appear in real‑time in mover plans.
- 📋 **Complete Booking Lifecycle**  
  - Mover books a plan → status `pending`  
  - Provider accepts → `accepted`  
  - Provider marks complete → `completed`  
  - Mover rates services (1‑5) → `rated`  
- ⭐ **Service Ratings** – New services get an automatic initial rating based on cost; mover ratings update the average.
- 🗺️ **Free Map View** – OpenStreetMap + Leaflet.js inside a WebView (no API key required).
- 🔔 **Push Notifications** – Firebase Cloud Messaging for real‑time alerts.
- 🔄 **Real‑time Updates** – Pull‑to‑refresh & 60‑second auto‑refresh on the plan list.
- 🎨 **Material Design UI** – Slate Gray & Warm Cream theme, high contrast, custom icons, adaptive launcher icon.
- ♿ **Accessibility** – All icons have content descriptions, touch targets ≥48dp, readable text sizes.
- 🚚 **Animated Splash Screen** – Truck carrying a package moves between two houses.
- 📱 **Both Mover & Provider Dashboards** – Mover has “My Bookings” (with ratings), Provider has “My Services” and “My Orders” (accept/complete actions).

---

## 🧱 Architecture & Tech Stack

- **Language:** Java
- **UI:** XML, Material Components, RecyclerView, CardView
- **Authentication:** Firebase Auth (Email/Password)
- **Database:** Cloud Firestore
- **Notifications:** Firebase Cloud Messaging (FCM)
- **Networking:** Retrofit2 + OkHttp3 (hardcoded mock interceptor serving JSON)
- **Map:** WebView + Leaflet.js + OpenStreetMap (free)
- **Build:** Gradle (Groovy DSL), minSdk 24

### Project Structure
```
com.example.movease
├── data
│   ├── api          (Retrofit service, MockInterceptor, RetrofitClient)
│   ├── model        (House, LaborProvider, PackingProvider, TransportProvider, Service)
│   └── repository   (MoveRepository, ServiceRepository)
├── engine           (Plan, PlanGenerator)
├── ui               (PlanAdapter, OrdersAdapter, MoverBookingsAdapter, ServiceAdapter)
├── SplashActivity
├── LoginActivity
├── SignupActivity
├── MoverHomeActivity
├── ProviderHomeActivity
├── PlanListActivity
├── PlanDetailActivity
├── MapActivity
├── MoverBookingsActivity
├── OrdersActivity
├── MyFirebaseMessagingService
└── UserSessionManager
```

---

## 🚀 Getting Started

### Prerequisites
- Android Studio (latest stable)
- Android SDK with API 24+ (Nougat)
- Firebase project with **Email/Password** auth and **Firestore** enabled
- `google-services.json` placed in `app/`

### Setup
1. **Clone the repository**
   ```bash
   git clone https://github.com/your-username/MovEase.git
   ```
2. **Open in Android Studio**, sync Gradle.
3. **Add your `google-services.json`** (from Firebase Console).
4. **Run on emulator/device** (minSdk 24).

### Mock Data
- The `MockInterceptor` contains hardcoded JSON for houses (20 real Zameen listings) and empty arrays for services.
- All provider services are fetched **dynamically from Firestore**, making the marketplace truly two‑sided.

---

## 🧪 Testing the Full Flow

1. **Sign up as a Mover** (default) → fill the form (area, budget, date, bedrooms) → generate plans.
2. **View plans** → toggle details, open map, tap a plan to see details.
3. **Book a plan** → booking status becomes `pending`.
4. **Log out** → sign up as a **Service Provider**.
5. **Add services** (e.g., labour 400/hr) – initial rating auto‑calculated.
6. **Open “My Orders”** → see the booking, tap **Accept** → status becomes `accepted`.
7. **Tap Complete** → status becomes `completed`.
8. **Log out** → log back in as the **Mover**.
9. **Go to “My Bookings”** → see the completed booking, tap **Rate Services**.
10. **Rate each service** → average ratings update in Firestore.
11. **Generate plans again** – updated ratings are reflected.

- **Push notifications:** Send a test message from Firebase Console → Cloud Messaging.
- **Map:** Tap the map button on any plan to see the house location.

---

## 📸 Screenshots

| Splash | Login | Plan Input | Plan List | Map |
|--------|-------|------------|-----------|-----|
| ![Splash](screenshots/splash.png) | ![Login](screenshots/login.png) | ![Input](screenshots/input.png) | ![Plans](screenshots/plans.png) | ![Map](screenshots/map.png) |

| Plan Detail | Mover Bookings | Rate Services | Provider Orders |
|-------------|----------------|---------------|-----------------|
| ![Detail](screenshots/detail.png) | ![MoverBookings](screenshots/mover_bookings.png) | ![Rate](screenshots/rate.png) | ![Orders](screenshots/orders.png) |

---

## 📄 Project Report & Demo

- Full project report: [Project_Report.pdf](docs/Project_Report.pdf) (add your own)
- Demo video: [Watch on YouTube](#) (add link)

---

## 👥 Contributors

- Muhammad Abdullah Tariq – [GitHub Profile](https://github.com/QMAT)
- Hannan Shoaib – [GitHub Profile](https://github.com/Hannan-95)

---

## 📜 License

This project is for educational purposes as part of a semester project.  
Property data from Zameen.com is used for demonstration only.

---

**⭐ If you find this project useful, give it a star!**
