package com.shepherdmoney.interviewproject.controller;

import com.shepherdmoney.interviewproject.model.BalanceHistory;
import com.shepherdmoney.interviewproject.model.CreditCard;
import com.shepherdmoney.interviewproject.model.User;
import com.shepherdmoney.interviewproject.repository.CreditCardRepository;
import com.shepherdmoney.interviewproject.repository.UserRepository;
import com.shepherdmoney.interviewproject.vo.request.AddCreditCardToUserPayload;
import com.shepherdmoney.interviewproject.vo.request.UpdateBalancePayload;
import com.shepherdmoney.interviewproject.vo.response.CreditCardView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;


@RestController
public class CreditCardController {

    // TODO: wire in CreditCard repository here (~1 line)

    @Autowired
    CreditCardRepository creditCardRepository;

    @Autowired
    UserRepository userRepository;

    @PostMapping("/credit-card")
    public ResponseEntity<Integer> addCreditCardToUser(@RequestBody AddCreditCardToUserPayload payload) {
        // TODO: Create a credit card entity, and then associate that credit card with user with given userId
        //       Return 200 OK with the credit card id if the user exists and credit card is successfully associated with the user
        //       Return other appropriate response code for other exception cases
        //       Do not worry about validating the card number, assume card number could be any arbitrary format and length
        ResponseEntity<Integer> response;
        CreditCard creditCard = new CreditCard();
        creditCard.setIssuanceBank(payload.getCardIssuanceBank());
        creditCard.setNumber(payload.getCardNumber());
        ResponseEntity<Integer> responseEntity;
        User user = userRepository.findById(payload.getUserId()).get();

        if (user != null) {
            CreditCard card = new CreditCard();
            card.setUser(user);
            card.setNumber(payload.getCardNumber());
            card.setIssuanceBank(payload.getCardIssuanceBank());
            creditCardRepository.save(card);
            responseEntity = new ResponseEntity<>(HttpStatus.OK);
        } else {
            responseEntity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return responseEntity;
    }

    @GetMapping("/credit-card:all")
    public ResponseEntity<List<CreditCardView>> getAllCardOfUser(@RequestParam int userId) {
        // TODO: return a list of all credit card associated with the given userId, using CreditCardView class
        //       if the user has no credit card, return empty list, never return
        System.out.println("user id:" + userId);
        List<CreditCard> creditCards = creditCardRepository.findCardsByUserId(userId);
        List<CreditCardView> creditCardViews = creditCards.stream().
                map(creditCard -> new CreditCardView(creditCard.getIssuanceBank(), creditCard.getNumber())).
                toList();
        ResponseEntity<List<CreditCardView>> response = new ResponseEntity(creditCardViews, HttpStatus.OK);
        return response;
    }

    @GetMapping("/credit-card:user-id")
    public ResponseEntity<Integer> getUserIdForCreditCard(@RequestParam String creditCardNumber) {
        // TODO: Given a credit card number, efficiently find whether there is a user associated with the credit card
        //       If so, return the user id in a 200 OK response. If no such user exists, return 400 Bad Request
        User user = creditCardRepository.findUserForCard(creditCardNumber);
        ResponseEntity<Integer> response;
        if (user != null) {
            response = new ResponseEntity<>(user.getId(), HttpStatus.OK);
        } else {
            response = new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        return response;
    }

    @PostMapping("/credit-card:update-balance")
    public ResponseEntity<Integer> postMethodName(@RequestBody UpdateBalancePayload[] payload) {
        //TODO: Given a list of transactions, update credit cards' balance history.
        //      For example: if today is 4/12, a credit card's balanceHistory is [{date: 4/12, balance: 110}, {date: 4/10, balance: 100}],
        //      Given a transaction of {date: 4/10, amount: 10}, the new balanceHistory is
        //      [{date: 4/12, balance: 120}, {date: 4/11, balance: 110}, {date: 4/10, balance: 110}]
        //      Return 200 OK if update is done and successful, 400 Bad Request if the given card number
        //        is not associated with a card.
        CreditCard card;
        ResponseEntity<Integer> response;
        BalanceHistory balanceHistory;
        for (UpdateBalancePayload updateBalancePayload : payload) {
            card = creditCardRepository.findByNumber(updateBalancePayload.getCreditCardNumber());
            if (card == null) {
                response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                return response;
            } else {
                balanceHistory = new BalanceHistory();
                balanceHistory.setBalance(updateBalancePayload.getTransactionAmount());
                balanceHistory.setDate(updateBalancePayload.getTransactionTime());
                List<BalanceHistory> balanceHistories = new ArrayList<>();
                balanceHistories.add(balanceHistory);
                card.setBalanceHistories(balanceHistories);
                creditCardRepository.save(card);
            }
        }
        response = new ResponseEntity<>(HttpStatus.OK);
        return response;
    }

}
