package com.red.code015.api.host

import com.red.code015.api.host.Platform.*
import com.red.code015.api.host.Region.*
import com.red.code015.domain.PlatformID
import com.red.code015.domain.RegionID

sealed class Platform(val id: PlatformID, host: String) : HostInterceptor.BaseHost(host) {
    object KR : Platform(PlatformID.KR, "kr.api.riotgames.com")
    object NA1 : Platform(PlatformID.NA, "na1.api.riotgames.com")
    object LA1 : Platform(PlatformID.LAN, "la1.api.riotgames.com")
    object LA2 : Platform(PlatformID.LAS, "la2.api.riotgames.com")
    object BR1 : Platform(PlatformID.BR, "br.api.riotgames.com")
    object EUN1 : Platform(PlatformID.EUNE, "eun1.api.riotgames.com")
    object EUW1 : Platform(PlatformID.EUW, "euw1.api.riotgames.com")
    object JP1 : Platform(PlatformID.JP, "jp1.api.riotgames.com")
    object OC1 : Platform(PlatformID.OCE, "oc1.api.riotgames.com")
    object TR1 : Platform(PlatformID.TR, "tr.api.riotgames.com")
    object RU : Platform(PlatformID.RU, "ru.api.riotgames.com")
}

internal fun PlatformID.toAPI() = when (this) {
    PlatformID.NA -> NA1
    PlatformID.LAN -> LA1
    PlatformID.LAS -> LA2
    PlatformID.BR -> BR1
    PlatformID.EUW -> EUW1
    PlatformID.EUNE -> EUN1
    PlatformID.OCE -> OC1
    PlatformID.KR -> KR
    PlatformID.TR -> JP1
    PlatformID.JP -> TR1
    PlatformID.RU -> RU
}

sealed class Region(val id: RegionID, host: String) : HostInterceptor.BaseHost(host) {
    object AMERICAS : Region(RegionID.AMERICAS, "americas.api.riotgames.com")
    object ASIA : Region(RegionID.ASIA, "asia.api.riotgames.com")
    object EUROPE : Region(RegionID.EUROPE, "europa.api.riotgames.com")
}

internal fun RegionID.toAPI() = when (this) {
    RegionID.AMERICAS -> AMERICAS
    RegionID.ASIA -> ASIA
    RegionID.EUROPE -> EUROPE
}