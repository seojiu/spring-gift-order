package gift.service;

import gift.config.KakaoProperties;
import gift.model.Member;
import gift.repository.MemberRepository;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
public class KakaoLoginService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final MemberRepository memberRepository;
    private final KakaoProperties properties;

    public KakaoLoginService(MemberRepository memberRepository, KakaoProperties properties) {
        this.memberRepository = memberRepository;
        this.properties = properties;
    }

    private static String getEmail(ResponseEntity<Map> responseEntity) {
        var response = responseEntity.getBody();
        if (response == null) {
            throw new NoSuchElementException("Response가 없습니다.");
        }
        Map<String, Object> kakaoAccount = (Map<String, Object>) response.get("kakao_account");
        if (kakaoAccount == null) {
            throw new NoSuchElementException("kakao_account가 없습니다.");
        }
        String email = (String) kakaoAccount.get("email");
        if (email == null) {
            throw new NoSuchElementException("response에서 Email 정보가 없습니다.");
        }
        return email;
    }

    public String getUrl() {
        return "https://kauth.kakao.com/oauth/authorize?"
                + "scope=account_email"
                + "&response_type=code"
                + "&redirect_uri=" + properties.redirectUrl()
                + "&client_id=" + properties.clientId();
    }

    public String getAccessToken(String code) {
        var url = "https://kauth.kakao.com/oauth/token";
        var headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        var body = createBody(code);

        var request = new RequestEntity<>(body, headers, HttpMethod.POST, URI.create(url));
        var responseEntity = restTemplate.exchange(request, Map.class);
        var response = responseEntity.getBody();

        if (response == null) {
            throw new NoSuchElementException("Response가 없습니다.");
        }
        return response.get("access_token").toString();
    }

    public String signUpAndLogin(String accessToken) {
        var url = "https://kapi.kakao.com/v2/user/me";
        var headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        var request = new RequestEntity<>(headers, HttpMethod.GET, URI.create(url));
        var responseEntity = restTemplate.exchange(request, Map.class);
        String email = getEmail(responseEntity);

        if (!memberRepository.existsByEmail(email)) {
            memberRepository.save(new Member(email, "비밀번호", accessToken));
            return "회원가입 및 로그인 되었습니다.";
        }
        return "로그인 되었습니다";
    }

    private LinkedMultiValueMap<String, String> createBody(String code) {
        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", properties.clientId());
        body.add("redirect_uri", properties.redirectUrl());
        body.add("code", code);
        return body;
    }
}
