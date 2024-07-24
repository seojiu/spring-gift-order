package gift.ControllerTest;

import gift.model.Category;
import gift.model.Member;
import gift.model.Option;
import gift.model.Product;
import gift.model.Wishlist;
import gift.repository.CategoryRepository;
import gift.repository.MemberRepository;
import gift.repository.ProductRepository;
import gift.repository.WishlistRepository;
import gift.util.JwtUtility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class WishlistControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WishlistRepository wishlistRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    void setup() {
        wishlistRepository.deleteAll();
        productRepository.deleteAll();
        categoryRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Test
    void testAddProductToWishlist() throws Exception {
        Member member = new Member("test@example.com", "password");
        memberRepository.save(member);

        Category category = new Category("카테고리1", "#FFFFFF", "이미지URL", "설명");
        categoryRepository.save(category);

        Option option = new Option("옵션1", 10);
        Product product = new Product("상품1", 1000, "이미지URL", category, Collections.singletonList(option));
        productRepository.save(product);

        String token = JwtUtility.generateToken(member.getEmail());

        mockMvc.perform(post("/api/wishlist/" + product.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isCreated());
    }

    @Test
    void testRemoveProductFromWishlist() throws Exception {
        Member member = new Member("test@example.com", "password");
        memberRepository.save(member);

        Category category = new Category("카테고리1", "#FFFFFF", "이미지URL", "설명");
        categoryRepository.save(category);

        Option option = new Option("옵션1", 10);
        Product product = new Product("상품1", 1000, "이미지URL", category, Collections.singletonList(option));
        productRepository.save(product);

        Wishlist wishlist = new Wishlist(member, product);
        wishlistRepository.save(wishlist);

        String token = JwtUtility.generateToken(member.getEmail());

        mockMvc.perform(delete("/api/wishlist/" + product.getId())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isNoContent());
    }

    @Test
    void testGetWishlist() throws Exception {
        Member member = new Member("test@example.com", "password");
        memberRepository.save(member);

        Category category = new Category("카테고리1", "#FFFFFF", "이미지URL", "설명");
        categoryRepository.save(category);

        Option option = new Option("옵션1", 10);
        Product product = new Product("상품1", 1000, "이미지URL", category, Collections.singletonList(option));
        productRepository.save(product);

        Wishlist wishlist = new Wishlist(member, product);
        wishlistRepository.save(wishlist);

        String token = JwtUtility.generateToken(member.getEmail());

        mockMvc.perform(get("/api/wishlist")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].product.name").value("상품1"));
    }
}
