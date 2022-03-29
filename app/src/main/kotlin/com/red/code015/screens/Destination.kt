package com.red.code015.screens

sealed class Destination {
    object Home : Destination()
    object Register : Destination()
    object Summoner : Destination()
}
