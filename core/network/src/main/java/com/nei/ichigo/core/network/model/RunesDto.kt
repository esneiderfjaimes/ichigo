package com.nei.ichigo.core.network.model

import com.nei.ichigo.core.model.Rune
import com.nei.ichigo.core.model.RuneBranch
import com.nei.ichigo.core.model.RuneSlot

typealias RunesDto = List<BranchDto>

data class BranchDto(
    val id: String,
    val key: String,
    val icon: String,
    val name: String,
    val slots: List<SlotDto>
)

data class SlotDto(
    val runes: List<RuneDto>,
)

data class RuneDto(
    val id: String,
    val key: String,
    val icon: String,
    val name: String,
    val shortDesc: String,
    val longDesc: String
)

fun BranchDto.asExternalModel(): RuneBranch = RuneBranch(
    id = id,
    key = key,
    icon = icon,
    name = name,
    slots = slots.map(SlotDto::asExternalModel)
)

fun SlotDto.asExternalModel(): RuneSlot = RuneSlot(
    runes = runes.map(RuneDto::asExternalModel)
)

fun RuneDto.asExternalModel(): Rune = Rune(
    id = id,
    key = key,
    icon = icon,
    name = name,
    shortDesc = shortDesc,
    longDesc = longDesc
)
