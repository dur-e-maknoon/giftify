package com.perfume.store.controllers;

import com.perfume.store.models.*;
import com.perfume.store.models.payment.Payment;
import com.perfume.store.repositories.*;
import com.perfume.store.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.ArrayList;

@Controller
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    // Show checkout page
    @GetMapping("/checkout")
    public String showCheckout(Authentication auth, Model model,
                               @RequestParam(required = false) String error) {
        String email = auth.getName();
        Cart cart = cartService.getCartForUser(email);
        model.addAttribute("cart", cart);
        model.addAttribute("total", cartService.getCartTotal(email));
        if (error != null) {
            model.addAttribute("checkoutError", error);
        }
        return "pages/checkout";
    }

    // Place order
    @PostMapping("/place")
    public String placeOrder(Authentication auth,
                             @RequestParam String shippingAddress,
                             @RequestParam String paymentMethod,
                             @RequestParam(required = false) String cardAccountNumber,
                             @RequestParam(required = false) String onlineProvider,
                             @RequestParam(required = false) String walletAccountNumber) {
        String email = auth.getName();
        User user = userRepository.findByEmail(email);
        Cart cart = cartService.getCartForUser(email);

        if (cart.getItems().isEmpty()) {
            return "redirect:/cart";
        }

        // Create order
        Order order = new Order();
        order.setUser(user);
        order.setShippingAddress(shippingAddress);
        order.setStatus("PENDING");
        order.setTotalAmount(cartService.getCartTotal(email));

        // Add products to order
        java.util.List<Product> products = new ArrayList<>();
        for (CartItem item : cart.getItems()) {
            products.add(item.getProduct());
        }
        order.setProducts(products);
        orderRepository.save(order);

        // Create payment
        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setAmount(order.getTotalAmount());
        payment.setPaymentMethod(paymentMethod);
        payment.setStatus("PENDING");

        if ("CARD".equals(paymentMethod)) {
            if (cardAccountNumber == null || cardAccountNumber.trim().isEmpty()) {
                return "redirect:/orders/checkout?error=card";
            }
            payment.setAccountNumber(cardAccountNumber.trim());
        } else if ("ONLINE".equals(paymentMethod)) {
            if (onlineProvider == null || onlineProvider.isBlank()
                    || walletAccountNumber == null || walletAccountNumber.trim().isEmpty()) {
                return "redirect:/orders/checkout?error=online";
            }
            payment.setOnlineProvider(onlineProvider.trim());
            payment.setAccountNumber(walletAccountNumber.trim());
        }

        paymentRepository.save(payment);

        // Clear cart
        cartService.clearCart(email);

        return "redirect:/orders/confirmation/" + order.getId();
    }

    // Order confirmation
    @GetMapping("/confirmation/{id}")
    public String orderConfirmation(@PathVariable Long id, Model model) {
        model.addAttribute("order", orderRepository.findById(id).orElse(null));
        model.addAttribute("payment", paymentRepository.findByOrderId(id));
        return "pages/confirmation";
    }

    // My orders
    @GetMapping("/my-orders")
    public String myOrders(Authentication auth, Model model) {
        User user = userRepository.findByEmail(auth.getName());
        model.addAttribute("orders", orderRepository.findByUserId(user.getId()));
        return "pages/myorders";
    }
}