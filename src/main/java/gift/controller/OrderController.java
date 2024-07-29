package gift.controller;

import gift.dto.OrderDto;
import gift.model.Member;
import gift.model.Order;
import gift.repository.MemberRepository;
import gift.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Order", description = "상품 주문 관련 api")
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;
    private final MemberRepository memberRepository;

    public OrderController(OrderService orderService, MemberRepository memberRepository) {
        this.orderService = orderService;
        this.memberRepository = memberRepository;
    }

    @PostMapping
    @Operation(summary = "주문하기", description = "주문을 합니다.")
    public ResponseEntity<Order> createOrder(@RequestBody @Valid OrderDto orderDto,
                                             @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        String token = authHeader.substring(7);
        Member member = memberRepository.findByActiveToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));
        Order order = orderService.createOrder(orderDto, member.getId());
        return ResponseEntity.status(201).body(order);
    }

    @GetMapping
    @Operation(summary = "모든 주문 조회", description = "모든 주문을 조회합니다.")
    public ResponseEntity<Page<Order>> getAllOrders(
            @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<Order> orderPage = orderService.getOrders(pageable);
        return ResponseEntity.ok(orderPage);
    }
}
