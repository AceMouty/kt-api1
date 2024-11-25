package ktapi1.services

import ktapi1.models.UserModel
import ktapi1.repositories.UserRepository
import org.apache.catalina.User
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserService(private val db: UserRepository) {
    fun getUsers(): List<UserModel> = db.findAll().toList()

    fun createNewUser(newUser: UserModel): UserModel {
        val savedUser =  db.save(newUser)
        return savedUser
    }

    fun existsById(userId: Long) = db.existsById(userId)
    fun findUserById(userId: Long): Optional<UserModel> = db.findById(userId)

    fun update(userChanges: UserModel): UserModel {
        db.save(userChanges)
        return userChanges
    }

    fun delete(userId: Long): UserModel {
        val userToDelete = db.findByIdOrNull(userId)!!
        db.deleteById(userId)
        return userToDelete
    }
}