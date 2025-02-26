package com.lcwd.electronic.store.services.implementations;

import com.lcwd.electronic.store.dtos.CartDto;
import com.lcwd.electronic.store.dtos.CartItemDto;
import com.lcwd.electronic.store.entities.Cart;
import com.lcwd.electronic.store.entities.CartItem;
import com.lcwd.electronic.store.entities.Product;
import com.lcwd.electronic.store.entities.User;
import com.lcwd.electronic.store.exception.BadApiRequestException;
import com.lcwd.electronic.store.exception.ResourceNotFoundException;
import com.lcwd.electronic.store.repositories.CartItemRepository;
import com.lcwd.electronic.store.repositories.CartRepositories;
import com.lcwd.electronic.store.repositories.ProductRepositories;
import com.lcwd.electronic.store.repositories.UserRepositories;
import com.lcwd.electronic.store.services.CartService;
import jakarta.persistence.Id;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static java.util.stream.Collectors.toList;

@Service
public class CartServiceImpl implements CartService {


    @Autowired
    private UserRepositories user_repo;
    @Autowired
    private ProductRepositories product_repo;
    @Autowired
    private CartRepositories cart_repo;
    @Autowired
    private CartItemRepository cart_item_repo;
    @Autowired
    private ModelMapper mapper;

    @Override
    public CartDto addItem(String userId, String productId, int quantity) {
        if(quantity<0){
            throw new BadApiRequestException("Quantity can't be negative");
        }
        User user=user_repo.findById(userId).orElseThrow(()->new ResourceNotFoundException("User with given User ID doesn't exist"));
        Product product=product_repo.findById(productId).orElseThrow(()->new ResourceNotFoundException("Product with given ID is not present"));
        Cart cart;

        try{
            cart=cart_repo.findByUser(user).get();
        }catch(NoSuchElementException ex){
            cart=new Cart();
            cart.setCartId(UUID.randomUUID().toString());
            cart.setCreatedAt(new Date());
            cart.setCartItemList(new ArrayList<>());
        }

//        System.out.println(cart);
        AtomicReference<Boolean> flag= new AtomicReference<>(false);
        List<CartItem> lst=cart.getCartItemList();

        List<CartItem> new_lst=lst.stream().map(val->{
            if(val.getProduct().getProductId().equals(productId)){
                val.setQuantity(quantity);
                val.setTotalPrice(quantity*val.getProduct().getDiscountedPrice());
                flag.set(true);
            }
            return val;
        }).toList();

        if(!flag.get()) {
            CartItem cart_item = CartItem.builder()
                    .product(product)
                    .quantity(quantity)
                    .totalPrice(product.getDiscountedPrice() * quantity)
                    .cart(cart)
                    .build();
            cart.getCartItemList().add(cart_item);
        }
        cart.setUser(user);

        Cart savedCart=cart_repo.save(cart);
        CartDto resp=mapper.map(savedCart,CartDto.class);
        return resp;
    }

    @Override
    public void removeItem(String userId, int cartItemId) {
        CartItem item=cart_item_repo.findById(cartItemId).orElseThrow(()->new ResourceNotFoundException("Item with given ItemID is not present"));
        cart_item_repo.delete(item);
    }

    @Override
    public void clearCart(String userId) {
        User user=user_repo.findById(userId).orElseThrow(()->new ResourceNotFoundException("User with given Id doesn't exist"));
        Cart cart=cart_repo.findByUser(user).orElseThrow(()->new ResourceNotFoundException("The provided Item is not present in the cart"));

        cart.getCartItemList().clear();
        cart_repo.save(cart);
    }

    @Override
    public CartDto fetchCart(String userId) {
        User user=user_repo.findById(userId).orElseThrow(()->new ResourceNotFoundException("User with given Id doesn't exist"));
        Cart cart=cart_repo.findByUser(user).orElseThrow(()->new ResourceNotFoundException("The provided Item is not present in the cart"));
        return mapper.map(cart,CartDto.class);

    }

}
