package tn.esprit.pibakcend.RestController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.pibakcend.Repository.UserRepository;
import tn.esprit.pibakcend.dto.CouponViewDto;
import tn.esprit.pibakcend.entities.User;
import tn.esprit.pibakcend.mapper.CouponMapper;

import java.util.List;

@RestController
@RequestMapping("/coupons")
@CrossOrigin(origins = "*")

public class CouponController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CouponMapper couponMapper;
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CouponViewDto>> getOrdersByUser(@PathVariable("userId") Long userId) {
        User user = userRepository.getUserById(userId);
        return ResponseEntity.ok().body(couponMapper.toDto(user.getCoupons()));
    }
    }
