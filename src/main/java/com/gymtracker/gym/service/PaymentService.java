package com.gymtracker.gym.service;

import com.gymtracker.gym.model.MembershipType;
import com.gymtracker.gym.model.Payment;
import com.gymtracker.gym.model.User;
import com.gymtracker.gym.repository.PaymentRepository;
import com.gymtracker.gym.repository.UserRepository;
import com.stripe.Stripe;
import com.stripe.param.billingportal.SessionCreateParams;
import com.stripe.model.checkout.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.stripe.exception.StripeException;
import java.time.LocalDate;
import java.util.List;

@Service
public class PaymentService {

    @Autowired
    private UserRepository userRepository;

    @Value("${stripe.public.key}")
    private String stripePublicKey;

    @Autowired
    private PaymentRepository paymentRepository;
    public void savePayment(Payment payment) {
        paymentRepository.save(payment);
    }

    /*
    public String createStripeCheckoutSession(Long userId, MembershipType membershipType) throws StripeException {
        Stripe.apiKey = "sk_test_51RJDJK4KUnOQM8TrB2C4lES1Lh9m1WP1prP7qeQC8n3WRm3DhwsKoaT71YjUWWpq3zrW2DyOVvQeNklQB63U8z0R00cUMJmGOj";

        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("https://yourdomain.com/payment-success?session_id={CHECKOUT_SESSION_ID}")
                .setCancelUrl("https://yourdomain.com/payment-cancel")
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setQuantity(1L)
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency("usd")
                                                .setUnitAmount(membershipType.getPriceInCents())
                                                .setProductData(
                                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                .setName("Gym Membership - " + membershipType.name())
                                                                .build()
                                                )
                                                .build()
                                )
                                .build()
                )
                .build();

        Session session = Session.create(params);

        Payment payment = new Payment();
        payment.setStripePaymentIntentId(session.getId());
        payment.setUser(userRepository.findById(userId).orElseThrow());
        payment.setPaymentStatus("Pending");
        payment.setMembershipType(membershipType.name());
        paymentRepository.save(payment);

        return session.getUrl();
    }
    */

    public void confirmPayment(String sessionId) throws StripeException {
        Session session = Session.retrieve(sessionId);

        Payment payment = paymentRepository.findByStripePaymentIntentId(sessionId);
        if (payment != null) {
            payment.setPaymentStatus("Completed");
            paymentRepository.save(payment);

            User user = payment.getUser();
            MembershipType membershipType = MembershipType.valueOf(payment.getMembershipType());
            user.setMembershipType(membershipType);
            user.setMembershipStartDate(LocalDate.now());
            user.setMembershipExpiryDate(LocalDate.now().plusDays(membershipType.getDurationDays()));
            userRepository.save(user);
        }
    }

    public List<Payment> getPaymentsByUser(Long userId) {
        return paymentRepository.findByUserId(userId);
    }

}
