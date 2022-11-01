package com.example.movie_service.Controllers;

import com.example.movie_service.Entities.DTO.UserDTO;
import com.example.movie_service.Entities.UserEntities.User;
import com.example.movie_service.Entities.UserEntities.UserRole;
import com.example.movie_service.Repositories.PurchaseRepository;
import com.example.movie_service.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;
import java.util.Objects;

@Controller
public class MVCController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PurchaseRepository purchaseRepository;

    private final ModelAndView modelAndView = new ModelAndView();

    @GetMapping("/registration")
    public ModelAndView registrationPage(){
        modelAndView.setViewName("User/signUp.html");
        return modelAndView;
    }
    @PostMapping("/registration")
    public ModelAndView registrationFunction(UserDTO userDTO,Map<String,Object> model){
        User user = userRepository.findUserByNickName(userDTO.getNickname());
        if(user == null) {
            userRepository.addUser(userDTO.getFirstName(), userDTO.getLastName(), userDTO.getNickname(), userDTO.getBirthday(), userDTO.getPassword());
            userRepository.addUserRole(userRepository.findUserByNickName(userDTO.getNickname()).getId(), UserRole.CLIENT.toString());
        }else {
            model.put("error","Sorry,this nickname is booked");
        }
        modelAndView.setViewName("User/signUp.html");
        return modelAndView;
    }

    @GetMapping("/authentication")
    public ModelAndView authenticationPage(){
        modelAndView.setViewName("User/login.html");
        return modelAndView;
    }
  @PostMapping("/authentication")
    public ModelAndView authenticationFunction(String nickname, String password){
        User user = userRepository.findUserByNickName(nickname);
        if(user == null) {
            modelAndView.setViewName("User/signUp.html");
        }else if(Objects.equals(user.getPassword(), password) && user.getRole().contains(UserRole.CLIENT)){
                        modelAndView.setViewName("index.html");
        }else {
            modelAndView.setViewName("Admin/home.html");
        }
        return modelAndView;
    }


}
