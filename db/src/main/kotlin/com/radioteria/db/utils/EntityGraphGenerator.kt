package com.radioteria.db.utils

import com.radioteria.db.entities.*
import com.radioteria.db.enums.UserState
import java.util.*


fun generateListOfUsers(amount: Int): List<User> =
        (1..amount).map { generateUser("User # $it") }

fun generateUser(name: String = generateRandomString()): User {
    val user = User()

    user.name = name
    user.email = generateRandomString()
    user.password = generateRandomString()
    user.state = UserState.ACTIVE
    user.avatarFile = generateFile()
    user.channels = generateListOfChannels(amount = 10, user = user)

    return user
}

fun generateListOfChannels(amount: Int, user: User): List<Channel> =
        (1..amount).map { generateChannel(user) }

fun generateChannel(user: User): Channel {
    val channel = Channel(user = user)

    channel.name = generateRandomString()
    channel.artworkFile = generateFile()
    channel.description = generateRandomString()
    channel.tracks = generateListOfTracks(amount = 10, channel = channel)

    return channel
}

fun generateListOfTracks(amount: Int, channel: Channel): MutableList<Track> =
        (1..amount).map { generateTrack(channel = channel, orderId = it) }.toMutableList()

fun generateTrack(channel: Channel, orderId: Int = 1): Track {
    val track = Track(channel = channel, trackFile = generateFile())

    track.artist = generateRandomString()
    track.title = generateRandomString()
    track.duration = generateRandomLong()
    track.orderId = orderId
    track.channel = channel

    return track
}

fun generateFile(): File =
        File(content = generateContent(), name = generateRandomString())

fun generateContent(): Content =
        Content(hash = generateRandomString(), length = generateRandomLong())

fun generateRandomString() = UUID.randomUUID().toString()

fun generateRandomLong() = Random().nextLong()
