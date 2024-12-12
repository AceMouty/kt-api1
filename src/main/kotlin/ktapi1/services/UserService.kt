package ktapi1.services

import ktapi1.models.UserModel
import ktapi1.repositories.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserService(private val userRepo: UserRepository) {
    fun getUsers(): List<UserModel> = userRepo.findAll().toList()

    fun createNewUser(newUser: UserModel): UserModel {
        val savedUser =  userRepo.save(newUser)
        return savedUser
    }

    fun findUserById(userId: Long): Optional<UserModel> = userRepo.findById(userId)

    fun update(userChanges: UserModel): UserModel? {
        val userExists = userRepo.existsById(userChanges.id)
        if(!userExists) {
            return null
        }

        return userRepo.save(userChanges)
    }

    fun delete(userId: Long): UserModel? {
        val userToDelete = userRepo.findByIdOrNull(userId)
        if(userToDelete == null) {
            return null
        }

        userRepo.deleteById(userId)
        return userToDelete
    }
}