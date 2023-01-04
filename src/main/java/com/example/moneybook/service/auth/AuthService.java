package com.example.moneybook.service.auth;

import com.example.moneybook.common.config.security.JwtTokenProvider;
import com.example.moneybook.common.config.security.dto.TokenResponseDto;
import com.example.moneybook.common.exception.model.ConflictException;
import com.example.moneybook.common.exception.model.InternalServerException;
import com.example.moneybook.common.exception.model.NotFoundException;
import com.example.moneybook.common.exception.model.ValidationException;
import com.example.moneybook.common.repository.RedisRepository;
import com.example.moneybook.controller.auth.dto.request.CompleteAuthEmailRequestDto;
import com.example.moneybook.controller.auth.dto.request.SendAuthEmailRequestDto;
import com.example.moneybook.doamin.MemberRole;
import com.example.moneybook.doamin.member.Member;
import com.example.moneybook.doamin.member.MemberRepository;
import com.example.moneybook.controller.auth.dto.request.CreateMemberRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

import static com.example.moneybook.common.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class AuthService {
    public static final long AuthEmailRequestWillExpireIn = 60 * 60 * 24L;
    public static final long AuthKeyExpiration = 60 * 3L;
    private final MemberRepository memberRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final JavaMailSender javaMailSender;
    private final RedisRepository redisRepository;

    public void validateEmail(String email) {

        if (memberRepository.findByEmail(email).isPresent()) {
            throw new ConflictException(
                    String.format("이미 가입된 유저의 이메일 (%s) 입니다.", email),
                    CONFLICT_USER_EXCEPTION
            );
        }
    }

    public void sendAuthEmail(SendAuthEmailRequestDto request) {
        String email = request.getEmail();

        validateEmail(email);

        String authKey = getAuthKey();

        String subject = "짠짠이 가입을 위한 인증 이메일입니다.";
        String text = "회원가입을 위한 인증번호는 " + authKey + "입니다.<br/>";

        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "utf-8");
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(text, true);
            javaMailSender.send(mimeMessage);

        } catch (MessagingException e) {
            throw new InternalServerException(String.format("(%s) 이메일에 대한 인증 메일을 전송하는 중 에러가 발생했습니다.", email));
        }
        redisRepository.setDataExpire(authKey, email, AuthKeyExpiration);
        redisRepository.setDataExpire("EMAIL-AUTH:${" + email + "}", "이메일 인증 신청", AuthEmailRequestWillExpireIn);
    }

    public void completeAuthEmail(CompleteAuthEmailRequestDto request) {

        String email = redisRepository.getData(request.getAuthKey());
        // 인증 이메일 전송 2번하는 경우에 대한 예외처리해야함

        try {
            if (!email.equals(request.getEmail())) { // 인증키로 가져온 이메일과 입력한 이메일이 다른 경우
                throw new ValidationException("잘못된 이메일 입니다.", VALIDATION_EMAIL_AUTH_KEY_EXCEPTION);
            }
        } catch (NullPointerException e) { // 이메일에 해당하는 인증키가 없음
            throw new ValidationException("잘못된 이메일 인증번호입니다.", VALIDATION_EMAIL_AUTH_KEY_EXCEPTION);
        }
        // 인증 완료 후 하루안에 회원가입해야함
        redisRepository.setDataExpire(email, "이메일 인증 완료", AuthEmailRequestWillExpireIn);
    }

    public Member getMemberByEmail(String email) {
        Optional<Member> optionalMember = memberRepository.findByEmail(email);
        if (optionalMember.isEmpty()) {
            throw new NotFoundException("이메일에 해당하는 사용자가 없습니다.", NOT_FOUND_USER_EXCEPTION);
        }

        return optionalMember.get();
    }

    public void createMember(CreateMemberRequestDto request) {
        String email = request.getEmail();

        validateEmail(email);

        // 인증 신청하지 않은 이메일인 경우
        if(redisRepository.getData(email).isEmpty()) {
            throw new ValidationException("이메일 인증 완료 후 회원가입 할 수 있습니다.", UNAUTHORIZED_EMAIL_EXCEPTION);
        }

        // 이메일 인증 신청했지만, 인증키를 입력하지 않은 경우
        if (redisRepository.getData(email).equals("이메일 인증 신청")) {
            throw new ValidationException("인증키를 입력 해주세요.", UNAUTHORIZED_AUTH_KEY_EXCEPTION);
        }

        memberRepository.save(Member.builder()
                .memberName(request.getMemberName())
                .password(request.getPassword())
                .email(request.getEmail())
                .role(MemberRole.ROLE_MEMBER)
                .createdAt(LocalDateTime.now())
                .build());
    }

    @Transactional
    public TokenResponseDto signIn(String email, String password) {

        // 1. Login id/pw를 기반으로 Authentication 객체 생성
        // 이때 authentication 는 인증 여부를 확인하는 authenticated 값이 false
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(email, password);

         // 2. 실제 검증 (사용자 비밀번호 체크)이 이뤄지는 부분
         // authenticate 메서드가 실행될 때 CustomUserDetailsService 에서 만든
         // loadUserByUsername 메서드가 실행
        Authentication authentication =
                authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        TokenResponseDto tokenResponseDto = jwtTokenProvider.generateToken(authentication);

        return tokenResponseDto;
    }

    private String getAuthKey() {
        String authKey;

        do {
            Random random = new Random();
            authKey = String.valueOf(random.nextInt(999999));
        } while (redisRepository.existKey(authKey));

        return authKey;
    }
}
