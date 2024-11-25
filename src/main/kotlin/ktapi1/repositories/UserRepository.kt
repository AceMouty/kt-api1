package ktapi1.repositories
import ktapi1.models.UserModel
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : CrudRepository<UserModel, Long>