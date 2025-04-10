# :newspaper: WhatsNew

 Browse the news from around the globe! Filter, search, save and much more!

  ## Expected Guest user flow

| Introduction Screen | Articles Screen | Detail Article Screen |  Filter Screen |  List Screen with filters |
:-------------------------:|:-------------------------:|:-------------------------:|:-------------------------:|:-------------------------:
| ![whatsNew_Guest_Intro](https://github.com/user-attachments/assets/1fba67b8-290e-44f2-982e-585e85d05489) | ![whatsNew_Guest_Articles](https://github.com/user-attachments/assets/9d0d7f45-f447-4537-82e5-8b3d8754e7d6) | ![whatsNew_Guest_Article_Detail](https://github.com/user-attachments/assets/0d835145-4010-4978-8a7f-f33eff3bd062) | ![whatsNew_Guest_Filters](https://github.com/user-attachments/assets/4734bb8a-9f26-4ecc-b1a8-22378c47eb11) | ![whatsNew_Guest_Articles_Filtered](https://github.com/user-attachments/assets/0c10981c-cfaa-463e-9681-23ca812474d3) | 



 ## Expected Authorized user flow

 | Registration Screen | Login Screen |
 :-----:|:-----|
 | ![whatsNew_Registration](https://github.com/user-attachments/assets/435055b3-5003-4977-9a88-cc24dd95476a) | ![whatsNew_SignIn](https://github.com/user-attachments/assets/efff0b27-4328-42c5-9d12-618f66bdef4f) |


https://github.com/user-attachments/assets/afdb626b-1a5c-49b4-90e7-7a968b8f82f3

https://github.com/user-attachments/assets/e138d32a-3b1c-48f7-ae3c-ec8c6247728f

https://github.com/user-attachments/assets/0e83ef87-f346-402d-8bdb-a6a0f3dde808

https://github.com/user-attachments/assets/42417002-9b9d-445d-9f0a-0c89ca02b721


## :key: Features
- Articles browsing
- Advanced filtering options
- Article saving
- Article share :globe_with_meridians:
- Custom design / dark mode :first_quarter_moon:
- Anonym / Registered user browsing

## 💻 Technologies
- Kotlin
- Jetpack Compose
- Coroutines / Flows
- Koin
- Coil
- Ktor
- EncryptedSharedPreferences
- Room + FTS4 :floppy_disk:
- Adaptive navigation
- JUnit
- Turbine
- Ktor Client Mock
- SOLID / MVI / UDF
- Custom design style

## :open_book: Project Structure
Application follows single module architecture, however, it is divided by features. Each feature represents own "standalone" part (functionality) of the app. Feature is further divided by three common layers - Data, Domain and Presentation. Feature should not have access to other feature. The exceptions are "core" and "app" feature. Core feature can be accessed accross the app - and its contains logic shared among the different features and components, however default Data, Domain and Presentation layering must be complied. Core feature cannot depend on any other feature. App feature acts like a "glue" between the features - their main focus (in this application) is to navigate between the screens and initialize our DI modules.
 - Data layers contains concrete implementations for our business logic abstractions which are defined in the Domain Layers.
 - Domain layer should stay free of any external dependencies and contain only pure business logic and may include models and abstractions.
 - Presentation layer is used to react to user interaction and provide the most actual visible content (state) for the user.

### What to keep in mind
Features may access core feature as long as they don't break layer pattern.
The layer pattern is as follow:
- Domain layer can be accessed from the Presentation and Data layer.
- Domain layer cannot depend on Presentation or Data layer and should be free of any external (ex. android) dependencies.
- Presentation and Data layer cannot access each other.
If we combine these rules together with core feature, we will get behaviour as follow:
- feat_profile.presentation can access core.presentation and core.domain.
- feat_profile.domain can access core.domain.
- feat_profile.presentation cannot access core.data or feat_profile.data

## Others
- WhatsNew contains basic Unit and Integration tests :test_tube:
- WhatsNew feat_articles contains one shared viewModel for multiple screens of the same feature.

