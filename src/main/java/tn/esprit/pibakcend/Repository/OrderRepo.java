package tn.esprit.pibakcend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.pibakcend.entities.Order;
import tn.esprit.pibakcend.entities.User;

import java.util.List;
@Repository
public interface OrderRepo  extends JpaRepository<Order,Long> {
    List<Order> findByUser(User user);


}
