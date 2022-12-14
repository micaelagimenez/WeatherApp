# Weather App
Simple Weather Android app written with Kotlin that uses the API Openweathermap

## Features
### Main Activity
Displays current weather in London. Features a Material Discrete Slider to obtain weather data from London,
Buenos Aires, Montevideo, San Pablo and Munich.

### GPS Access
User can click on a button to get weather data from their own city. GPS access must be granted to get the data.

### MVVM
App follows design pattern MVVM to separate logic from view. View gets data through the Viewmodel.

### Supports screen orientation change
App supports screen orientation change to show the data.

## Libraries Used
 <ul>
 <li> <a href="https://m3.material.io/components/sliders/overview">Material Slider</a> </li>
 <li> <a href="https://dagger.dev/hilt/">Dagger Hilt</a> </li>
<li> <a href="https://developer.android.com/topic/libraries/architecture/viewmodel">ViewModel</a> </li>
 <li> <a href="https://developer.android.com/topic/libraries/architecture/livedata">LiveData</a> </li>
 <li> <a href="http://square.github.io/retrofit/">Retrofit</a> </li>
 <li> <a href="http://square.github.io/picasso/">Picasso</a> </li>
 <li> <a href="http://square.github.io/okhttp/">OkHttp3</a> </li>
 </ul>