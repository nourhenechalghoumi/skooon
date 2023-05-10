package tn.esprit.pibakcend.security.services;

import tn.esprit.pibakcend.configuration.CouponException;
import tn.esprit.pibakcend.entities.ERole;
import tn.esprit.pibakcend.entities.Order;
import tn.esprit.pibakcend.entities.OrderItem;
import tn.esprit.pibakcend.entities.User;

import java.util.List;

public interface IOrder {
    Order createOrder(User user, List<OrderItem> orderItems);
    List<Order> getOrdersByUser(User user);

    List<Order> getALLOrdersByUser(User user);

    Order getOrderById(Long id);
    Double calculateTotalPrice(List<OrderItem> orderItems);
    List<ERole> getUserRoles(User user);

    List<Order> findAll();

    //Purchase createPurchase(User user, Double amount);
    boolean isEligibleForCoupon(User user, Double amount);
    Order applyCoupon(User user, Order order, String couponCode) throws CouponException;

    Order save(Order order);

}

