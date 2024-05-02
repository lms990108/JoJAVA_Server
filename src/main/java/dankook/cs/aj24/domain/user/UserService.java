package dankook.cs.aj24.domain.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserDocument createUser(UserDocument user) {
        return userRepository.save(user);
    }

    public Optional<UserDocument> getUserById(String id) {
        return userRepository.findById(id);
    }

    public List<UserDocument> getAllUsers() {
        return userRepository.findAll();
    }

    public UserDocument updateUser(UserDocument user) {
        return userRepository.save(user);
    }

    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }
}
