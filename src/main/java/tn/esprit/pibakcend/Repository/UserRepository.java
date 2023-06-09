package tn.esprit.pibakcend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.esprit.pibakcend.entities.ERole;
import tn.esprit.pibakcend.entities.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username); // User findByUsername
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
    List<User> findByAddress(String address);
    List<User> findByStateUser(boolean stateUser);
    @Query("SELECT u.roles FROM User u WHERE u = :user")
    List<ERole> getRoles(@Param("user") User user);

    @Query("SELECT SUM(p.amount) FROM Purchase p WHERE p.user = :user")
    Double getPurchasesAmountByUser(@Param("user") User user);

    User getUserById(Long userId);
}

