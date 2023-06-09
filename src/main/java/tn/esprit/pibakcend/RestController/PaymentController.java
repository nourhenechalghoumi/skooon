package tn.esprit.pibakcend.RestController;

import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;
import tn.esprit.pibakcend.Repository.CouponRepo;
import tn.esprit.pibakcend.entities.Coupon;
import tn.esprit.pibakcend.entities.EOrderStatus;
import tn.esprit.pibakcend.entities.Order;
import tn.esprit.pibakcend.entities.User;
import tn.esprit.pibakcend.security.services.OrderServiceImplement;

import java.util.Date;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/payment")
@CrossOrigin(origins = "*")

public class PaymentController {

    @Autowired
    OrderServiceImplement orderServiceImplement;
    @Autowired
    CouponRepo couponRepo;
    @Autowired
    private JavaMailSender mailSender;
    private User user;



@GetMapping("/success")
public ResponseEntity<Double> payOrder(@RequestParam("sessionId") String checkoutSessionId) {
    try {
        Session session = Session.retrieve(checkoutSessionId);
        Map<String, String> metadata = session.getMetadata();
        String orderId = (String) metadata.get("order_id");
        System.out.println("Received order ID: " + orderId);
        Order order = orderServiceImplement.getOrderById(Long.valueOf(orderId));
        order.setStatus(EOrderStatus.PAID);
        Double total = order.getTotalPrice();
        User user = order.getUser();
        if (orderServiceImplement.isEligibleForCoupon(user, total)) {
            Coupon coupon = Coupon.builder()
                    .user(user)
                    .discount(Double.valueOf(0.15))
                    .expirationDate(DateUtils.addDays(new Date(), 15))
                    .code(generateRandomString())
                    .build();
            couponRepo.save(coupon);
            sendCouponEmail(user, coupon);
            total = orderServiceImplement.applyCoupon(total, coupon);
            order.setTotalPrice(total);
        }
        orderServiceImplement.save(order);
        return new ResponseEntity<Double>(total, HttpStatus.OK);
    } catch (StripeException e) {
        e.printStackTrace();
        return new ResponseEntity<Double>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
//still not working
    private void sendCouponEmail(User user, Coupon coupon) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Your Coupon Code");
        message.setText("Hello " + user.getNom() + ",\n\n"
                + "Thank you for your recent purchase. As a token of our appreciation, please use the following coupon code to receive a 15% discount on your next purchase:\n\n"
                + coupon.getCode() + "\n\n"
                + "Please note that this coupon code will expire on " + coupon.getExpirationDate() + ".\n\n"
                + "Thank you for your business!\n\n"
                + "Best regards,\n"
                + "The Project Management Team");
        mailSender.send(message);
    }

    public static String generateRandomString() {
        String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        int LENGTH = 16;
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < LENGTH; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(randomIndex);
            sb.append(randomChar);
        }
        return sb.toString();
    }

}
