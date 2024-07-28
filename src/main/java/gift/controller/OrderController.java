package gift.controller;

import gift.dto.OrderDto;
import gift.model.Member;
import gift.model.Order;
import gift.repository.MemberRepository;
import gift.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;
    private final MemberRepository memberRepository;

    public OrderController(OrderService orderService, MemberRepository memberRepository) {
        this.orderService = orderService;
        this.memberRepository = memberRepository;
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody @Valid OrderDto orderDto,
                                             @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        String token = authHeader.substring(7);
        Member member = memberRepository.findByActiveToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));
        Order order = orderService.createOrder(orderDto, member.getId());
        return ResponseEntity.status(201).body(order);
    }
}
