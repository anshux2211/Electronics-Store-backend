package com.example.electronic.store.services.implementations;

import com.example.electronic.store.dtos.CreateOrderRequest;
import com.example.electronic.store.dtos.OrderDto;
import com.example.electronic.store.dtos.PageableResponse;
import com.example.electronic.store.dtos.ProductDto;
import com.example.electronic.store.entities.*;
import com.example.electronic.store.exception.BadApiRequestException;
import com.example.electronic.store.exception.ResourceNotFoundException;
import com.example.electronic.store.helper.ToPageableResponse;
import com.example.electronic.store.repositories.CartRepositories;
import com.example.electronic.store.repositories.OrderRepository;
import com.example.electronic.store.repositories.UserRepositories;
import com.example.electronic.store.services.OrderService;
import com.example.electronic.store.services.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
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
    private ProductService product_service;
    @Autowired
    private ModelMapper mapper;

    @Override
    public OrderDto createOrder(CreateOrderRequest orderDto) {
        String userId=orderDto.getUserId();
        String cartId=orderDto.getCartId();


        User user=userRepositories.findById(userId).orElseThrow(()->new ResourceNotFoundException("User with given UserId does not exist"));
        Cart cart=cartRepositories.findById(cartId).orElseThrow(()-> new ResourceNotFoundException("Cart with given Cart ID does not exist"));
        List<CartItem> cartItems=cart.getCartItemList();

        List<CartItem> invalidItems=new ArrayList<>();
        cartItems.forEach(cartItem->{
            if(cartItem.getProduct().getQuantity()<cartItem.getQuantity()){
                invalidItems.add(cartItem);
            }
        });

        String items="";
        for(CartItem item:invalidItems){
            items+=item.getProduct().getProductName();
            items+=" ";
        }

        if(!invalidItems.isEmpty()){
            throw new BadApiRequestException(String.format("Stocks available for some products has reduced. Please reduce their quantity to proceed further. Those products are : %s",items));
        }

        if(cartItems.isEmpty()){
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
        Order savedOrder=orderRepository.save(order);

        cart.getCartItemList().forEach(cartItem->{
            ProductDto productDto=mapper.map(cartItem.getProduct(),ProductDto.class);
            productDto.setQuantity(productDto.getQuantity()-cartItem.getQuantity());
            product_service.updateProduct(cartItem.getProduct().getProductId(),productDto);
        });

        cart.getCartItemList().clear();
        cartRepositories.save(cart);

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
