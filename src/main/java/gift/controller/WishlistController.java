package gift.controller;

import gift.model.Member;
import gift.model.Wishlist;
import gift.repository.MemberRepository;
import gift.service.WishlistService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@Tag(name = "Wishlist", description = "위시리스트 관련 api")
@RequestMapping("/api/wishlist")
public class WishlistController {
    private final WishlistService wishlistService;
    private final MemberRepository memberRepository;

    public WishlistController(WishlistService wishlistService, MemberRepository memberRepository) {
        this.wishlistService = wishlistService;
        this.memberRepository = memberRepository;
    }

    @GetMapping
    public ResponseEntity<Page<Wishlist>> getWishlist(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
            @PageableDefault(sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        String token = authHeader.substring(7);
        Member member = memberRepository.findByActiveToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));
        Page<Wishlist> wishlistPage = wishlistService.getWishlist(member.getId(), pageable);
        return ResponseEntity.ok(wishlistPage);
    }

    @PostMapping("/{productId}")
    public ResponseEntity<Void> addProductToWishlist(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader, @PathVariable Long productId) {
        String token = authHeader.substring(7);
        Member member = memberRepository.findByActiveToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));
        wishlistService.addProductToWishlist(member.getId(), productId);
        return ResponseEntity.created(URI.create("/api/wishlist/" + productId)).build();
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> removeProductFromWishlist(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader, @PathVariable Long productId) {
        String token = authHeader.substring(7);
        Member member = memberRepository.findByActiveToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));
        wishlistService.removeProductFromWishlist(member.getId(), productId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
