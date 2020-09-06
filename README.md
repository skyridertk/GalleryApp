<a href="https://android-arsenal.com/api?level=21"><img src="https://img.shields.io/badge/API-22%2B-brightgreen.svg?style=flat" alt="API" /></a>
<a href="https://ktlint.github.io/"><img src="https://img.shields.io/badge/code%20style-%E2%9D%A4-FF4081.svg" alt="ktlint" /></a>

# Gallery App
GalleryApp is a Firestore based image gallery



#### Features
-  Handles multiple file uploads (with a progress indicator)
-  Optimized for slow or inconsistent network connections
-  Stores image paths to Firestore collection
-  Fetches and displays data from firestore in realtime
-  Delete each image
-  See thumbnails of the uploaded images
-  Upload image(s) to firebase storage (also be able to go into airplane mode and then resume uploads when the network is connected)
-  Select multiple images
-  Option to choose Camera or File browser for image uploading


## Outputs
WIP

## Libraries and tools
<li><a href="https://developer.android.com/topic/libraries/architecture/navigation/">CameraX</a></li>
<li><a href="https://developer.android.com/topic/libraries/architecture/navigation/">Navigation</a></li>
<li><a href="https://developer.android.com/topic/libraries/architecture/viewmodel">ViewModel</a></li>
<li><a href="https://developer.android.com/topic/libraries/architecture/livedata">LiveData</a></li>
<li><a href="https://developer.android.com/topic/libraries/data-binding">Data Binding</a></li>
<li><a href="https://material.io/develop/android/docs/getting-started/">Material Design</a></li>
<li><a href="https://developer.android.com/jetpack/androidx/releases/recyclerview">Recyclerview</a></li>
<li><a href="https://developer.android.com/topic/libraries/architecture/workmanager/basics">WorkManager</a></li>
<li><a href="https://firebase.google.com/">Firebase</a></li>
<li><a href="https://github.com/bumptech/glide">Glide</a></li>

## Testing
WIP

## Architecture
The app uses Single Activity MVVM [Model-View-ViewModel] architecture

![Architecture](https://developer.android.com/topic/libraries/architecture/images/final-architecture.png)

![Firebase](https://miro.medium.com/max/700/1*h1Qxz2tOjXFJeXztW1CWfA.png)