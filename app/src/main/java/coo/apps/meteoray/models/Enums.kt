package coo.apps.meteoray.models

enum class Icon(var type: String) {
    sun("weather-icon-sun.svg"),
    sunCloud("weather-icon-sun-cloud.svg"),
    clouds("weather-icon-clouds.svg"),
    rain("weather-icon-rain.svg"),
    snow("weather-icon-snow.svg"),
    thunderstorm("weather-icon-thunderstorm.svg"),
    hail("weather-icon-hail.svg"),
    moon("weather-icon-moon.svg"),
    moonCloud("weather-icon-moon-cloud.svg"),
    dust("weather-icon-dust.svg"),
    fog("weather-icon-fog.svg"),
    cloudy("cloudy"),
    dusty("dust"),
    foggy("fog"),
    heavyClouds("heavy-clouds"),
    heavyRain("heavy-rain"),
    nighhtClear("night-clear"),
    nightClouds("night-clouds"),
    rainy("rain"),
    snowy("snow"),
    sunny("sunny")
}

enum class NavigationDest{
    HOME,MAPS,ACTIONS,SETTINGS,LOCATIONS
}

enum class DbAction{
    EDIT,DELETE,SAVE
}
