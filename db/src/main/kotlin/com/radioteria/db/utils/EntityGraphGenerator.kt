package com.radioteria.db.utils

import com.radioteria.db.entities.*
import com.radioteria.db.enums.UserState
import java.util.*

object IdGenerator {
    private var lastId: Long = 1
    val newId: Long get() = lastId ++
}

fun generateUser(channelsAmount: Int = 10, tracksPerChannel: Int = 10): User {
    val user = User()

    user.name = generateRandomString()
    user.email = generateRandomString()
    user.password = generateRandomString()
    user.state = UserState.ACTIVE
    user.avatarFile = generateFile()
    user.channels = generateListOfChannels(amount = channelsAmount, user = user, tracksPerChannel = tracksPerChannel)

    return user
}

fun generateListOfChannels(amount: Int, user: User, tracksPerChannel: Int = 10): List<Channel> =
        (1..amount).map { generateChannel(user, tracksPerChannel = tracksPerChannel) }

fun generateChannel(user: User, tracksPerChannel: Int = 10): Channel {
    val channel = Channel(user = user)

    channel.name = generateRandomString()
    channel.artworkFile = generateFile()
    channel.description = generateRandomString()
    channel.tracks = generateListOfTracks(amount = tracksPerChannel, channel = channel)

    return channel
}

fun generateListOfTracks(amount: Int, channel: Channel): MutableList<Track> =
        (1..amount).map { generateTrack(channel = channel, orderId = it) }.toMutableList()

fun generateTrack(channel: Channel, orderId: Int = 1): Track {
    val track = Track(channel = channel, trackFile = generateFile())

    track.artist = generateRandomString()
    track.title = generateRandomString()
    track.duration = 1000 + generateRandomInt(100000).toLong()
    track.orderId = orderId
    track.channel = channel
    track.id = IdGenerator.newId

    return track
}

fun generateFile(): File =
        File(content = generateContent(), name = generateRandomString())

fun generateContent(): Content =
        Content(hash = generateRandomString(), length = generateRandomLong())

fun generateRandomString() = UUID.randomUUID().toString()

fun generateRandomLong() = Random().nextLong()

fun generateRandomInt(bound: Int) = Random().nextInt(bound)
