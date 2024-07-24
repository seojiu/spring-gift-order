package gift.ControllerTest;

import gift.model.Category;
import gift.repository.CategoryRepository;
import gift.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    void setup() {
        productRepository.deleteAll();
        categoryRepository.deleteAll();
    }

    @Test
    void testAddProduct() throws Exception {
        Category category = new Category("카테고리1");
        categoryRepository.save(category);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"name\": \"상품1\", \"price\": 1000, \"categoryId\": " + category.getId()
                                + ", \"options\": [ { \"name\": \"옵션1\", \"quantity\": 10 } ] }"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("상품1"))
                .andExpect(jsonPath("$.price").value(1000))
                .andExpect(jsonPath("$.options", hasSize(1)))
                .andExpect(jsonPath("$.options[0].name").value("옵션1"))
                .andExpect(jsonPath("$.options[0].quantity").value(10));
    }
}
