package ktapi1.controllers

import ktapi1.models.UserModel
import ktapi1.services.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
@RequestMapping("/api/users")
class UserController(val userService: UserService) {

    @GetMapping
    fun getUsers(): ResponseEntity<List<UserModel>> {
        val users: List<UserModel> = userService.getUsers();
        return ResponseEntity.ok(users)
    }

    @PostMapping("createUser")
    fun createUser(@RequestBody newUser: CreateUserRequest): ResponseEntity<UserModel> {
        val partialUserModel = UserModel( name = newUser.name, bio = newUser.bio)
        val createdUser = userService.createNewUser(partialUserModel)
        return ResponseEntity.created(URI("/${createdUser.id}")).body(createdUser)
    }

    @GetMapping("/{userId}")
    fun getUserById(@PathVariable userId: Long): ResponseEntity<UserResponse> {
        val foundUser: UserModel = userService.findUserById(userId).orElse(UserModel(id = 0, name = "", bio = ""))
        val responseMessage = if(foundUser.id == 0.toLong()) {
            ""
        } else {
            "User not found"
        }
        val user = UserDto( name = foundUser.name, bio = foundUser.bio)
        val response = UserResponse(user = user, message = responseMessage)

        return ResponseEntity.status(200).body(response)
    }

    @PutMapping("/{userId}")
    fun updateUserById(@PathVariable userId: Long, @RequestBody userChanges: UserDto): ResponseEntity<UserResponse>
    {
        val userExists= userService.existsById(userId)
        if(!userExists) {
            return userNotFoundResponse()
        }

        val userModel = UserModel(id = userId, name = userChanges.name, bio = userChanges.bio)
        val updatedUser = userService.update(userModel)
        val userDto = UserDto(name = updatedUser.name, bio = updatedUser.bio)

        return ResponseEntity.status(200).body(UserResponse( user = userDto, message = ""))
    }

    @DeleteMapping("/{userId}")
    fun deleteUserById(@PathVariable userId: Long): ResponseEntity<UserResponse>{
        val userExists= userService.existsById(userId)
        if(!userExists) {
            return userNotFoundResponse()
        }

        val deletedUser = userService.delete(userId)
        val userDto = UserDto(name = deletedUser.name, bio = deletedUser.bio)
        return ResponseEntity.status(202).body(UserResponse(user = userDto, message = ""))
    }

    private fun userNotFoundResponse(): ResponseEntity<UserResponse> =
        ResponseEntity.status(200).body(UserResponse( user = UserDto(name = "", bio = ""), message = "cant find user with provided Id"))

}

data class CreateUserRequest(val name: String, val bio: String)

data class UserResponse(val user: UserDto?, val message: String)
data class UserDto (val name: String = "", val bio: String = "")