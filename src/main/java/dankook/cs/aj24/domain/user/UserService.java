package dankook.cs.aj24.domain.user;

import dankook.cs.aj24.common.error.CustomException;
import dankook.cs.aj24.domain.user.userdtos.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static dankook.cs.aj24.common.error.ErrorCode.USER_NOT_AUTHENTICATED;
import static dankook.cs.aj24.common.error.ErrorCode.USER_NOT_FOUND;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserDocument createUser(UserDTO userDTO) {
        UserDocument user = userDTO.toUserDocument();
        return userRepository.save(user);
    }

    public Optional<UserDocument> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }

    public UserDocument getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new CustomException(USER_NOT_AUTHENTICATED);
        }
        String userEmail = ((OAuth2User) authentication.getPrincipal()).getName();
        return userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));
    }

}
