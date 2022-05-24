package pyo.quizgame.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pyo.quizgame.domain.UserInfo;
import pyo.quizgame.dto.UserInfoDto;
import pyo.quizgame.respository.UserRepository;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    public Long save(UserInfoDto infoDto) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        infoDto.setPassword(encoder.encode(infoDto.getPassword()));
        return userRepository.save(UserInfo.builder()
                .userId(infoDto.getUserId())
                .auth(infoDto.getAuth())
                .password(infoDto.getPassword()).build()).getId();
    }
    @Override
    public UserInfo loadUserByUsername(String userId) throws UsernameNotFoundException {
        return userRepository.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException((userId)));
    }
    public boolean isIdExists(String userId)
    {
        Optional<UserInfo> user = userRepository.findByUserId(userId);
        if (user.isEmpty())
            return false;
        return true;
    }
}
