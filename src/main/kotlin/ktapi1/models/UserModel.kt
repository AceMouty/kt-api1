package ktapi1.models

import jakarta.persistence.*

@Entity
@Table(name="users")
data class UserModel(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long = 0,
    var name: String,
    var bio: String) {

    constructor() : this(0, "", "")
}