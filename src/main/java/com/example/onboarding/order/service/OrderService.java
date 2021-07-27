package com.example.onboarding.order.service;


import com.example.onboarding.order.dto.OrderRequestDto;
import com.example.onboarding.order.dto.OrderResponseDto;
import com.example.onboarding.order.entity.OrderEntity;
import com.example.onboarding.order.repository.OrderRepository;
import com.example.onboarding.common.statics.UsageStatusConfiguration;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderResponseDto fetchOrder(int orderNumber) {

        OrderEntity orderEntity = orderRepository.findByOrderNumberAndUsageStatus(orderNumber, UsageStatusConfiguration.USAGE_STATUS).orElseThrow(() -> new NullPointerException("조회 정보가 없습니다."));
        return new OrderResponseDto(orderEntity);

    }

    public List<OrderResponseDto> fetchAll() {

        List<OrderResponseDto> list = orderRepository.findAllByUsageStatus(UsageStatusConfiguration.USAGE_STATUS).stream()
                .map(OrderResponseDto::new).collect(Collectors.toList());

        return list;

    }


    @Transactional
    public int registerOrder(OrderRequestDto orderDto) {

        OrderEntity orderEntity = orderDto.toEntity();
        OrderEntity response = orderRepository.save(orderEntity);
        return response.getOrderNumber();
    }

    @Transactional
    public void deleteOrder(int orderNumber) {

        orderRepository.deleteOrderStatus(orderNumber);

    }


}
