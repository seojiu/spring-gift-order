package gift.service;

import gift.model.Member;
import gift.repository.MemberRepository;
import gift.util.TokenBlacklist;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final TokenBlacklist tokenBlacklist;

    public MemberService(MemberRepository memberRepository, TokenBlacklist tokenBlacklist) {
        this.memberRepository = memberRepository;
        this.tokenBlacklist = tokenBlacklist;
    }

    public void logout(String token) {
        Member member = memberRepository.findByActiveToken(token)
                .orElseThrow(() -> new NoSuchElementException("유효하지 않은 토큰입니다."));
        Member updatedMember = new Member(member);
        memberRepository.save(updatedMember);
        tokenBlacklist.add(token);
    }

    public boolean isTokenBlacklisted(String token) {
        return tokenBlacklist.contains(token);
    }
}
