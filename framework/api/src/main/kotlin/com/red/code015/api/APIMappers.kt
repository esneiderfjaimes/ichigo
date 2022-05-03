package com.red.code015.api

import com.red.code015.api.retrofit.*
import com.red.code015.domain.*
import java.util.*

fun SummonerResponseServer.toProfile(platformID: PlatformID) = Profile(
    platformID = platformID,
    puuID = puuId!!,
    summonerID = id!!,
    accountID = accountId!!,
    name = name!!,
    profileIconID = profileIconId!!
)

object SummonerMapper {
    fun toDomain(
        platformID: PlatformID,
        summoner: SummonerResponseServer,
        account: AccountResponseServer?,
        leagues: List<LeagueResponseServer>,
    ): Summoner = summoner.run {
        Summoner(
            id = id!!,
            accountId = accountId!!,
            puuId = puuId!!,
            platformID = platformID,
            name = name!!,
            profileIconId = profileIconId!!,
            level = level!!,
            account = account?.toDomain(),
            leagues = leagues.toDomain(),
            dataSource = DataSources.API
        )
    }
}

fun List<MasteriesResponseServer>.toDom(): List<Mastery> = map { it.toDomain() }

fun List<MasteriesResponseServer>.toDom2(summonerId: String, platformID: PlatformID): Masteries =
    Masteries(
        summonerId = summonerId,
        platformID = platformID,
        data = map { it.toDomain() },
        dataSource = DataSource(DataSources.API, Date().time)
    )


fun MasteriesResponseServer.toDomain() = Mastery(
    championId = championId!!,
    championLevel = championLevel!!,
    championPoints = championPoints!!,
    lastPlayTime = lastPlayTime!!,
    championPointsSinceLastLevel = championPointsSinceLastLevel!!,
    championPointsUntilNextLevel = championPointsUntilNextLevel!!,
    chestGranted = chestGranted!!,
    tokensEarned = tokensEarned!!,
)

fun AccountResponseServer.toDomain() = Account(gameName, tagLine)

fun List<LeagueResponseServer>.toDomain() = map { it.toDomain() }

fun LeagueResponseServer.toDomain() = League(
    queueType = queueType!!.toQueueType(),
    tier = tier?.toTier(),
    rank = rank,
    points = points!!,
    wins = wins!!,
    losses = losses!!,
    veteran = veteran!!,
    inactive = inactive!!,
    freshBlood = freshBlood!!,
    hotStreak = hotStreak!!,
    miniSeries = miniSeries?.toDomain()
)

fun MiniSeriesRS.toDomain() = MiniSeries(losses!!, progress!!, target!!, wins!!)

fun String.toQueueType() = when (this) {
    LeagueAPIConstants.QueueTypeRankedSolo -> QueueType.RankedSolo
    LeagueAPIConstants.QueueTypeRankedFlex -> QueueType.RankedFlex
    else -> QueueType.Unknown
}

fun String.toTier() = when (this) {
    LeagueAPIConstants.TierIron -> Tier.Iron
    LeagueAPIConstants.TierBronze -> Tier.Bronze
    LeagueAPIConstants.TierSilver -> Tier.Silver
    LeagueAPIConstants.TierGold -> Tier.Gold
    LeagueAPIConstants.TierPlatinum -> Tier.Platinum
    LeagueAPIConstants.TierDiamond -> Tier.Diamond
    LeagueAPIConstants.TierMaster -> Tier.Master
    LeagueAPIConstants.TierGrandMaster -> Tier.GrandMaster
    LeagueAPIConstants.TierChallenger -> Tier.Challenger
    else -> Tier.Unknown
}

fun Map<String, ChampionResponseServer>.toList() =
    map {
        ChampListItem(id = it.value.id!!,
            key = it.value.key!!,
            name = it.value.name!!,
            image = it.value.image!!.toDomain(),
            tags = it.value.tags!!)
    }

fun Map<String, ChampionResponseServer>.toDomain(): Map<String, Champion> =
    map { it.key to it.value.toDomain() }.toMap()

fun ChampionResponseServer.toDomainItem() = ChampListItem(
    id = id!!,
    key = key!!,
    name = name!!,
    image = image!!.toDomain(),
    tags = tags!!,
    skins = skins?.toDomainSkins(),
)


fun ImageRS.toDomain() = Image(full!!, sprite!!, group!!, x!!, y!!, w!!, h!!)
fun SkinRS.toDomain() = Skin(id!!, num!!, name!!, chromas!!)
fun List<SkinRS>.toDomainSkins() = map { it.toDomain() }

fun ChampionResponseServer.toDomain() = Champion(
    id = id!!,
    key = key!!,
    name = name!!,
    title = title!!,
    image = image!!.toDomain(),
    blurb = blurb!!,
    info = info!!.toDomain(),
    tags = tags!!,
    partype = parType!!,
    stats = stats!!,
    skins = skins!!.toDomainSkins(),
    lore = lore,
    allytips = allyTips ?: emptyList(),
    enemytips = enemyTips ?: emptyList(),
    spells = spells!!.map { it.toDomain() },
    passive = passive!!.toDomain(),
    recommended = recommended!!.map { it.toDomain() }
)

fun InfoRS.toDomain() = Info(attack!!, defense!!, magic!!, difficulty!!)
fun SpellRS.toDomain() = Spell(
    id = id!!,
    name = name!!,
    description = description!!,
    tooltip = tooltip!!,
    leveltip = leveltip!!.toDomain(),
    maxrank = maxrank!!,
    cooldown = cooldown!!,
    cooldownBurn = cooldownBurn!!,
    cost = cost!!,
    costBurn = costBurn!!,
    datavalues = datavalues!!,
    effect = effect!!,
    effectBurn = effectBurn!!,
    vars = vars!!,
    costType = costType!!,
    maxammo = maxammo!!,
    range = range!!,
    rangeBurn = rangeBurn!!,
    image = image!!.toDomain(),
    resource = resource!!
)

fun PassiveRS.toDomain() = Passive(name!!, description!!, image!!.toDomain())

fun RecommendedRS.toDomain() = Recommended(
    champion = champion!!,
    title = title!!,
    map = map!!,
    mode = mode!!,
    type = type!!,
    customTag = customTag!!,
    sortrank = sortrank!!,
    extensionPage = extensionPage!!,
    useObviousCheckmark = useObviousCheckmark!!,
    customPanel = customPanel!!,
    blocks = blocks!!.map { it.toDomain() }
)

fun BlockRS.toDomain() = Block(
    type = type!!,
    recMath = recMath!!,
    recSteps = recSteps!!,
    minSummonerLevel = minSummonerLevel!!,
    maxSummonerLevel = maxSummonerLevel!!,
    showIfSummonerSpell = showIfSummonerSpell,
    hideIfSummonerSpell = hideIfSummonerSpell,
    appendAfterSection = appendAfterSection!!,
    visibleWithAllOf = visibleWithAllOf!!,
    hiddenWithAnyOf = hiddenWithAnyOf!!,
    items = items!!.map { it.toDomain() }
)

fun ItemRS.toDomain() = Item(id!!, count!!, hideCount!!)

fun LevelTipRS.toDomain() = LevelTip(label!!, effect!!)

fun ChampionsRotationResponseServer.toDomain() = ChampionsRotation(
    freeChampionIds = freeChampionIds!!.map { it.toString() },
    freeChampionIdsForNewPlayers = freeChampionIdsForNewPlayers!!.map { it.toString() },
    maxNewPlayerLevel = maxNewPlayerLevel!!,
    dataSource = DataSource(DataSources.API, Date().time)
)