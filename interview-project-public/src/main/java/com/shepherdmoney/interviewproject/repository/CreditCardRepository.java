package com.shepherdmoney.interviewproject.repository;

import com.shepherdmoney.interviewproject.model.CreditCard;
import com.shepherdmoney.interviewproject.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Crud repository to store credit cards
 */
@Repository
public interface CreditCardRepository extends JpaRepository<CreditCard, Integer> {

    CreditCard findByNumber(String number);

    @Query("Select c from CreditCard c where c.user.id=:userId")
    List<CreditCard> findCardsByUserId(@Param("userId") int id);

    @Query("Select c.user from CreditCard c where c.number=:creditCardNumber")
    User findUserForCard(@Param("creditCardNumber") String creditCardNumber);

}
