package com.red.code015.data.model

import android.os.Parcelable
import com.red.code015.R
import com.red.code015.domain.PlatformID
import com.red.code015.domain.PlatformID.BR
import com.red.code015.domain.PlatformID.EUNE
import com.red.code015.domain.PlatformID.EUW
import com.red.code015.domain.PlatformID.JP
import com.red.code015.domain.PlatformID.KR
import com.red.code015.domain.PlatformID.LAN
import com.red.code015.domain.PlatformID.LAS
import com.red.code015.domain.PlatformID.NA
import com.red.code015.domain.PlatformID.OCE
import com.red.code015.domain.PlatformID.RU
import com.red.code015.domain.PlatformID.TR
import kotlinx.parcelize.Parcelize

@Parcelize
data class Platform(val id: PlatformID, val resIdName: Int) : Parcelable

@Parcelize
data class Region(val suffix: String, val resIdName: Int) : Parcelable

val Platforms = listOf(NA, LAN, LAS, BR, EUW, EUNE, OCE, KR, TR, JP, RU).map { it.toUI() }

fun PlatformID.toUI() = when (this) {
    NA -> Platform(this, R.string.region_NA1)
    LAN -> Platform(this, R.string.region_LA1)
    LAS -> Platform(this, R.string.region_LA2)
    BR -> Platform(this, R.string.region_BR1)
    EUW -> Platform(this, R.string.region_EUW1)
    EUNE -> Platform(this, R.string.region_EUN1)
    OCE -> Platform(this, R.string.region_OC1)
    KR -> Platform(this, R.string.region_KR)
    TR -> Platform(this, R.string.region_TR1)
    JP -> Platform(this, R.string.region_JP1)
    RU -> Platform(this, R.string.region_RU)
}
