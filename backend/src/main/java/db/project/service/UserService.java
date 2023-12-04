package db.project.service;

import db.project.config.jwt.TokenProvider;
import db.project.dto.*;
import db.project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private long accessTokenValidTime = 60 * 60 * 1000L; //1시간
    private long refreshTokenValidTime = 7 * 24 * 60 * 60 * 1000L; //일주일

    public void save(User user) {
        userRepository.save(user)
                .orElseThrow(() -> new IllegalArgumentException("중복된 아이디입니다."));
    }

    @Transactional
    public UserLoginResponse login(UserLoginRequest userLoginRequest) {
        if(userLoginRequest.getId() == null) {
            throw new UsernameNotFoundException("존재하지 않는 아이디입니다.");
        }

        User user = userRepository.findUserById(userLoginRequest.getId())
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 아이디입니다."));

        if(!bCryptPasswordEncoder.matches(userLoginRequest.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
        }
        String accessToken = tokenProvider.createToken(user.getId(),  accessTokenValidTime);
        String refreshToken = tokenProvider.createToken(user.getId(), refreshTokenValidTime);

        refreshTokenService.save(RefreshToken.builder()
                .id(user.getId())
                .refreshToken(refreshToken)
                .build());

        return UserLoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .role(user.getRole())
                .build();
    }

    public void logout() {
        String id = SecurityContextHolder.getContext().getAuthentication().getName();
        refreshTokenService.deleteById(id);
    }

    public PWQuestionResponse findPWQuestion(PWQuestionRequest pwQuestionRequest) {
        String question = userRepository.findPWQuestionById(pwQuestionRequest.getId())
                .orElseThrow(() -> new IllegalArgumentException("가입하지 않은 id입니다."));
        return PWQuestionResponse.builder()
                .pw_question(question)
                .build();
    }

    public void checkPWAnswer(CheckAnswerRequest checkAnswerRequest) {
        User user = userRepository.findUserById(checkAnswerRequest.getId())
                .orElseThrow(() -> new IllegalArgumentException("가입하지 않은 id입니다."));
        if(!checkAnswerRequest.getPw_answer().equals(user.getPw_answer())) {
            throw new IllegalArgumentException("비밀번호 찾기 답변이 틀렸습니다.");
        }
    }

    public void updatePW(UpdatePWRequest updatePWRequest) {
        userRepository.updatePW(updatePWRequest.getId(), updatePWRequest.getNew_password());
    }
}