package dankook.cs.aj24.domain.user;

import dankook.cs.aj24.common.error.CustomException;
import dankook.cs.aj24.common.util.RedisUtil;
import dankook.cs.aj24.domain.jwt.JwtUtil;
import dankook.cs.aj24.domain.user.userdtos.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.Optional;

import static dankook.cs.aj24.common.error.ErrorCode.USER_NOT_AUTHENTICATED;
import static dankook.cs.aj24.common.error.ErrorCode.USER_NOT_FOUND;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RedisUtil redisUtil;

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

    public void logout(String token) {
        // validateToken 메서드로 클레임을 직접 추출하고 활용
        Map<String, Object> claims = JwtUtil.validateToken(token);
        long expirationTimeMillis = ((Integer) claims.get("exp")).longValue() * 1000; // 만료 시간을 millis로 변환
        long ttlMinutes = JwtUtil.tokenRemainTime(expirationTimeMillis);

        redisUtil.blacklistToken(token, ttlMinutes);
    }

}
