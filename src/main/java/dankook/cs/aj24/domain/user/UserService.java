package dankook.cs.aj24.domain.user;

import dankook.cs.aj24.domain.user.userdtos.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserDocument createUser(UserDTO userDTO) {
        UserDocument user = userDTO.toUserDocument();
        return userRepository.save(user);
    }

    public Optional<UserDocument> getUserById(String id) {
        return userRepository.findById(id);
    }

    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }
}
