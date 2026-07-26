package src.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import src.model.User;
import src.model.dto.RegisterRequest;
import src.services.RegistrationService;

@Controller
@RequestMapping("/users")
public class UsersController {
    final RegistrationService regService;

    @ModelAttribute(name = "user")
    public RegisterRequest user(){
        return new RegisterRequest();
    }

    @Autowired
    public UsersController(RegistrationService registrationService){
        regService = registrationService;
    }

    @GetMapping("register")
    public String registerForm() {
        return "registration";
    }

    @PostMapping("register")
    public String processRegistration(@ModelAttribute(name="user") RegisterRequest regRequest, Errors errors){
        if (errors.hasErrors()){
            return "redirect:/registration";
        }
        regService.registerUser(regRequest);
        return "redirect:/login";
    }

//    @GetMapping
//    public String showUserBlog(Model model){
//
//    }
}
