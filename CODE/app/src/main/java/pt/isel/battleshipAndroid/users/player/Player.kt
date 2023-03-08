package pt.isel.battleshipAndroid.users.player

import pt.isel.battleshipAndroid.main.LocalAuthorDto
import pt.isel.battleshipAndroid.main.LocalPlayerDto

/**
 * The domain entity for representing players
 */
data class Player(val id: Int, val username: String, val email: String) {
    init {
        require(username.isNotBlank() && email.isNotBlank())
    }
}

/**
 * The domain entity for representing authors
 */
data class Author(
    val name: String,
    val number: Int,
    val email: String,
    val username: String,
    val github_http: String,
    val linkedin_http: String,
    val ic_author: Int
) {
    init {
        require(username.isNotBlank() && email.isNotBlank())
    }
}

/**
 * Creates a [Player] instance from the given DTO
 */
fun Player(localDto: LocalPlayerDto): Player {
    return Player(id = localDto.id, username = localDto.userName, email = localDto.email)
}

/**
 * Creates a [Author] instance from the given DTO
 */
fun Author(localDto: LocalAuthorDto): Author {
    return Author(
        name = localDto.name,
        number = localDto.number,
        email = localDto.email,
        username = localDto.username,
        github_http = localDto.github_http,
        linkedin_http = localDto.linkedin_http,
        ic_author = localDto.ic_author
    )
}

/**
 * Converts this user to a local DTO, that can be placed in Bundles and
 * passed around between activities.
 */
fun Player.toLocalPlayerDto() = LocalPlayerDto(id, username, email)

/**
 * Converts this author to a local DTO, that can be placed in Bundles and
 * passed around between activities.
 */
fun Author.toLocalAuthorDto() =
    LocalAuthorDto(name, number, email, username, github_http, linkedin_http, ic_author)
