package com.radioteria.db.entities

import com.radioteria.db.enums.UserState
import javax.persistence.*

object UserMeta {
    const val TABLE_NAME = "users"
    const val EMAIL = "email"
    const val PASSWORD = "password"
    const val NAME = "name"
    const val STATE = "state"
    const val AVATAR_FILE_ID = "avatar_file_id"
}

@Entity
@Table(name = UserMeta.TABLE_NAME)
@Access(AccessType.FIELD)
class User(
        @Column(name = UserMeta.EMAIL, unique = true, nullable = false)
        var email: String = "",

        @Column(name = UserMeta.PASSWORD, nullable = false)
        var password: String = "",

        @Column(name = UserMeta.NAME, nullable = false)
        var name: String = "",

        @Enumerated
        @Column(name = UserMeta.STATE, nullable = false)
        var state: UserState = UserState.INACTIVE,

        @ManyToOne(targetEntity = File::class)
        @JoinColumn(name = UserMeta.AVATAR_FILE_ID)
        var avatarFile: File? = null,

        @OneToMany(mappedBy = "user", cascade = arrayOf(CascadeType.ALL), orphanRemoval = true)
        var channels: List<Channel> = emptyList(),

        id: Long? = null
) : IdAwareEntity<Long>(id)