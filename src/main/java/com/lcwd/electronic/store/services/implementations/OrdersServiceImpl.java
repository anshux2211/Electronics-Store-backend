package com.lcwd.electronic.store.services.implementations;

import com.lcwd.electronic.store.dtos.CreateOrderRequest;
import com.lcwd.electronic.store.dtos.OrderDto;
import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.entities.*;
import com.lcwd.electronic.store.exception.BadApiRequestException;
import com.lcwd.electronic.store.exception.ResourceNotFoundException;
import com.lcwd.electronic.store.helper.ToPageableResponse;
import com.lcwd.electronic.store.repositories.CartRepositories;
import com.lcwd.electronic.store.repositories.OrderRepository;
import com.lcwd.electronic.store.repositories.UserRepositories;
import com.lcwd.electronic.store.services.OrderService;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Setter;
import org.apache.catalina.mapper.Mapper;
import org.aspectj.weaver.ast.Or;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class OrdersServiceImpl implements OrderService {

    @Autowired
    private UserRepositories userRepositories;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CartRepositories cartRepositories;
    @Autowired
    private ModelMapper mapper;

    @Override
    public OrderDto createOrder(CreateOrderRequest orderDto) {
        String userId=orderDto.getUserId();
        String cartId=orderDto.getCartId();
        


        User user=userRepositories.findById(userId).orElseThrow(()->new ResourceNotFoundException("User with given UserId does not exist"));
        Cart cart=cartRepositories.findById(cartId).orElseThrow(()-> new ResourceNotFoundException("Cart with given Cart ID does not exist"));
        List<CartItem> cartItems=cart.getCartItemList();

        if(cartItems.size()==0){
            throw new BadApiRequestException("Request Invalid. Cart is Empty !!");
        }

        Order order=Order.builder()
                .orderId(UUID.randomUUID().toString())
                .billingName(orderDto.getBillingName())
                .billingAddress(orderDto.getBillingAddress())
                .billingPhone(orderDto.getBillingPhone())
                .orderStatus(orderDto.getOrderStatus())
                .paymentStatus(orderDto.getPaymentStatus())
                .orderDate(new Date())
                .deliveredDate(null)
                .user(user)
                .build();

        AtomicReference<Integer> orderAmount=new AtomicReference<>(0);
        List<OrderItem> orderItems=cartItems.stream().map(cartItem -> {
            OrderItem orderItem=OrderItem.builder()
                    .quantity(cartItem.getQuantity())
                    .product(cartItem.getProduct())
                    .totalPrice(cartItem.getTotalPrice())
                    .order(order)
                    .build();
            orderAmount.set(orderAmount.get()+orderItem.getTotalPrice());
            return orderItem;

        }).collect(Collectors.toList());

        order.setOrderItemList(orderItems);
        order.setOrderAmount(orderAmount.get());
//        System.out.println("Hey------------------"+order.getOrderItemList());
        cart.getCartItemList().clear();
        cartRepositories.save(cart);
        Order savedOrder=orderRepository.save(order);

        return mapper.map(savedOrder,OrderDto.class);
    }

    @Override
    public void removeOrder(String orderId) {
        Order order=orderRepository.findById(orderId).orElseThrow(()-> new ResourceNotFoundException("Order with given Order Id does not exist !!"));
        orderRepository.delete(order);
    }

    @Override
    public List<OrderDto> getOrdersofUser(String userId) {

        User user =userRepositories.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User with given user ID does not exist !!"));
        List<Order> order=orderRepository.findByUser(user);

        List<OrderDto> orderDtoList=order.stream().map(currOrder->{
          return mapper.map(currOrder,OrderDto.class);
        }).toList();
        return orderDtoList;
    }

    @Override
    public PageableResponse<OrderDto> getOrders(int pageNumber, int pageSize, String sortBy, String sortDir) {

        Sort sort=Sort.by(sortBy);
        if(sortDir.equalsIgnoreCase("desc"))
            sort=sort.descending();

        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);
        Page<Order> page= orderRepository.findAll(pageable);

        PageableResponse<OrderDto> resp= ToPageableResponse.getPageableResponse(page,OrderDto.class);
        return resp;
    }

    @Override
    public OrderDto updateOrder(String orderId, String orderStatus, String paymentStatus, Date delivereddate) {
        Order order=orderRepository.findById(orderId).orElseThrow(()->new ResourceNotFoundException("Invalid Order ID given !!"));
        if(orderStatus.equalsIgnoreCase("null"))
            orderStatus=order.getOrderStatus();
        if(paymentStatus.equalsIgnoreCase("null"))
            paymentStatus=order.getPaymentStatus();
        if(delivereddate==null)
            delivereddate=order.getDeliveredDate();

        order.setOrderStatus(orderStatus);
        order.setPaymentStatus(paymentStatus);
        order.setDeliveredDate(delivereddate);

        Order updatedOrder=orderRepository.save(order);
        return mapper.map(updatedOrder,OrderDto.class);
    }
}
