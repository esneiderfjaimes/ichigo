package com.nei.ichigo.common.utils

infix fun <A, B, C> Pair<A, B>.and(that: C) = Triple(this.first, this.second, that)