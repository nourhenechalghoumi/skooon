package tn.esprit.pibakcend.dto;

import lombok.Data;
import tn.esprit.pibakcend.entities.Order;
import tn.esprit.pibakcend.entities.OrderItem;
import tn.esprit.pibakcend.entities.User;

import java.util.Set;

@Data
public class Purchase {
    private User user;
    //private Address shippingAddress;
    private Order order;
    private Set<OrderItem> orderItems;
}
