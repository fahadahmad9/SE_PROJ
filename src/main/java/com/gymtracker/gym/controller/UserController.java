package com.gymtracker.gym.controller;

import com.gymtracker.gym.model.*;
import com.gymtracker.gym.service.DietPlanService;
import com.gymtracker.gym.service.PaymentService;
import com.gymtracker.gym.service.TrainerBookingService;
import com.gymtracker.gym.service.TrainerService;
import com.gymtracker.gym.service.UserService;
import com.gymtracker.gym.service.WorkoutPlanService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.stripe.exception.StripeException;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private WorkoutPlanService workoutPlanService;

    @Autowired
    private DietPlanService dietPlanService;

    @Autowired
    private TrainerService trainerService;

    @Autowired
    private TrainerBookingService trainerBookingService;
    @PersistenceContext
    private EntityManager entityManager;

    // Show User Login Page
    @GetMapping("/login")
    public String showUserLoginForm() {
        return "user-login";
    }

    @GetMapping("/signup")
    public String showUserSignupForm() {
        return "user-signup";
    }

    @GetMapping("/backtoUser")
    public String showUserMainForm() {
        return "user";
    }
    // Inside your UserController.java

    @PostMapping("/login")
    public String loginUser(@RequestParam String email, @RequestParam String password, HttpSession session, Model model) {

        Optional<User> optionalUser = userService.authenticateUser(email, password);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get(); // Get the User from Optional
            session.setAttribute("loggedInUser", user);
            System.out.println("User in session: " + user);
            return "redirect:/user/dashboard";
        } else {
            model.addAttribute("error", "Invalid username or password");
            return "user-login";
        }
    }

    // Show User Dashboard
    @GetMapping("/dashboard")
    public String showUserDashboard(Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/user-page";  // Not logged in, send back to login
        }
        model.addAttribute("user", user);
        return "user-dashboard";  // Loads user-dashboard.html
    }

    @PostMapping("/renew-membership")
    public String renewMembership(@RequestParam Long userId, @RequestParam MembershipType newMembershipType) {
        userService.renewMembership(userId, newMembershipType);
        return "redirect:/user-dashboard";
    }

    @GetMapping("/user/available-schedules/{trainerId}")
    public String viewAvailableSchedules(@PathVariable Long trainerId, Model model) {
        List<TrainerSchedule> schedules = trainerService.getTrainerSchedules(trainerId);
        model.addAttribute("schedules", schedules);
        return "available-trainer-slots"; // New template
    }

    @PostMapping("/user/book-schedule")
    public String bookTrainerSchedule(@RequestParam Long userId, @RequestParam Long scheduleId) {
        trainerBookingService.bookTrainerSchedule(userId, scheduleId);
        return "redirect:/user-dashboard"; // Assume you have this page already
    }

    // View Payment History
    @GetMapping("/payments")
    public String viewPayments(Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/user/login";
        }
        List<Payment> payments = paymentService.getPaymentsByUser(user.getId());
        model.addAttribute("payments", payments);
        return "user-payment-history";
    }

    /*
    @GetMapping("/user-dashboard")
    public String userDashboard(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());

        // Ensure the service methods are returning proper data
        List<WorkoutPlan> workoutPlans = workoutPlanService.getWorkoutPlansForUser(user);
        List<DietPlan> dietPlans = dietPlanService.getDietPlansForUser(user);

        // Add attributes to the model to be used in the Thymeleaf template
        model.addAttribute("user", user);
        model.addAttribute("workoutPlans", workoutPlans);
        model.addAttribute("dietPlans", dietPlans);

        return "user-dashboard";  // Make sure this template exists
    } */

    @GetMapping("/user/dashboard")
    public String getUserDashboard(Model model, Principal principal) {
        String email = principal.getName(); // gets logged-in user's email (username)
        User loggedInUser = userService.findByEmail(email); // fetch your User entity from DB

        model.addAttribute("user", loggedInUser);
        model.addAttribute("workoutPlans", workoutPlanService.getPlansByUserId(loggedInUser.getId())); // optional
        //model.addAttribute("dietPlans", dietPlanService.getPlansByUserId(loggedInUser.getId()));       // optional

        return "user-dashboard";
    }

    @GetMapping("/injectDummyPayments")
    public String injectDummyPayments() {
        // Let's assume you have a User with ID = 1
        Long userId = 1L;

        Payment payment1 = new Payment();
        payment1.setAmount(49.99);
        payment1.setPaymentDate(java.time.LocalDate.now());
        payment1.setPaymentMethod("Credit Card");
        payment1.setUser(userService.getUserById(userId));

        Payment payment2 = new Payment();
        payment2.setAmount(29.99);
        payment2.setPaymentDate(java.time.LocalDate.now().minusDays(7));
        payment2.setPaymentMethod("PayPal");
        payment2.setUser(userService.getUserById(userId));

        paymentService.savePayment(payment1);
        paymentService.savePayment(payment2);

        return "redirect:/user/payments";  // Go view payments after adding
    }

    @PostMapping("/signup")
    public String registerUser(@RequestParam String name, @RequestParam String email, @RequestParam String password, Model model) {
        // Manually check if email already exists
        List<User> existingUsers = entityManager
                .createQuery("SELECT u FROM User u WHERE u.email = :email", User.class)
                .setParameter("email", email)
                .getResultList();

        if (!existingUsers.isEmpty()) {
            model.addAttribute("error", "Email already registered. Please login.");
            return "user-login"; // or redirect:/user/login?error
        }

        // Manually create and persist user
        User newUser = new User();
        newUser.setName(name);
        newUser.setEmail(email);
        newUser.setPassword(password); // Encrypt later in real apps

        entityManager.persist(newUser);

        return "redirect:/user/login?success";
    }

    /*
    @PostMapping("/pay-membership")
    public String payMembership(@RequestParam Long userId, @RequestParam MembershipType membershipType) {
        String checkoutUrl = paymentService.createStripeCheckoutSession(userId, membershipType);
        return "redirect:" + checkoutUrl;
    }

    @GetMapping("/payment-success")
    public String paymentSuccess(@RequestParam("session_id") String sessionId) throws StripeException {
        paymentService.confirmPayment(sessionId);
        return "redirect:/user-dashboard";
    } */

    // View Workout Plans
    @GetMapping("/workouts")
    public String viewWorkoutPlans(Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/user/login";
        }
        List<WorkoutPlan> workouts = workoutPlanService.getAllWorkoutPlans();
        model.addAttribute("workouts", workouts);
        return "user-workouts"; // You need a user-workouts.html
    }

    @GetMapping("/add-workout-plan")
    public String addWorkoutPlans(Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login"; // redirect to login or some appropriate page
        }
        // Optionally: pass predefined workouts if needed
        List<WorkoutPlan> workouts = workoutPlanService.getAllWorkoutPlans();
        model.addAttribute("workouts", workouts);

        return "add-workout"; // This should match add-workout.html
    }

    @PostMapping("/save-workout")
    public String saveUserWorkout(@RequestParam("name") String title, @RequestParam("description") String description, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login"; // or any route you use for login
        }

        WorkoutPlan workoutPlan = new WorkoutPlan();
        workoutPlan.setTitle(title);
        workoutPlan.setDescription(description);
        workoutPlan.setUser(user);
        workoutPlan.setCreatedAt(LocalDateTime.now());

        // Optional: Set a default or derived targetGoal based on title/description/bodyType if you want
        // workoutPlan.setTargetGoal(FitnessGoal.MUSCLE_GAIN); // Example only

        workoutPlanService.saveWorkoutPlan(workoutPlan);

        return "redirect:/user/workouts"; // or wherever you want to show the user's plans
    }

    // View Diet Plans
    @GetMapping("/diets")
    public String viewDietPlans(Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/user/login";
        }
        List<DietPlan> diets = dietPlanService.getAllDietPlans();
        model.addAttribute("diets", diets);
        return "user-diets"; // You need a user-diets.html
    }

    @GetMapping("/add-dietplan")
    public String showAddDietPlanForm() {
        return "add-diet-plan";
    }

    @PostMapping("/save-dietplan")
    public String saveUserDiet(@RequestParam(value = "predefinedPlan", required = false) String predefinedPlan,
                               @RequestParam(value = "name", required = false) String name,
                               @RequestParam(value = "description", required = false) String description,
                               HttpSession session) {

        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login"; // Redirect if not logged in
        }

        DietPlan dietPlan = new DietPlan();

        // If user selected a predefined plan, use it
        if (predefinedPlan != null && !predefinedPlan.isEmpty()) {
            dietPlan.setTitle(predefinedPlan);
            dietPlan.setDescription("Default description for " + predefinedPlan);
        } else {
            // Otherwise, use manually entered data
            dietPlan.setTitle(name);
            dietPlan.setDescription(description);
        }

        dietPlan.setUser(user);
        dietPlan.setCreatedAt(LocalDateTime.now()); // Optional: if DietPlan has a timestamp

        dietPlanService.saveDietPlan(dietPlan);

        return "redirect:/user/diets";
    }

    // View Trainer Bookings
    @GetMapping("/trainers")
    public String viewTrainerBookings(Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/user/login";
        }
        List<TrainerBooking> bookings = trainerBookingService.getBookingsByUser(user.getId());
        model.addAttribute("bookings", bookings);
        return "user-trainers"; // You need a user-trainers.html
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/user/workouts")
    public String showUserWorkouts() {
        return "user-workouts";  // loads user-workouts.html
    }

    @GetMapping("/user/diets")
    public String showUserDiets() {
        return "user-diets";  // loads user-diets.html
    }

    @GetMapping("/user/payment-history")
    public String showPaymentHistory() {
        return "user-payment-history";  // loads user-payment-history.html
    }

    @GetMapping("/user/trainers")
    public String showUserTrainers() {
        return "user-trainers";  // loads user-trainers.html
    }

    @GetMapping("/book-trainer")
    public String showTrainerSessForm() {
        return "add-trainer";
    }



}
