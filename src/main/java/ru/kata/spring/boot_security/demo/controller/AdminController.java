package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleServiceImpl;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserServiceImpl userService;
    private final RoleServiceImpl roleService;

    @Autowired
    public AdminController(UserServiceImpl userService, RoleServiceImpl roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }


    @GetMapping()
    public String getAllUsers(ModelMap model) {
        List<User> users = userService.findAll();
        model.addAttribute("listUsers", users);
        model.addAttribute("listRoles", roleService.findAll());
        String currentUsername = userService.getCurrentUserName();
        Optional<User> user = userService.findByEmail(currentUsername);
        model.addAttribute("currentUser", user.get());
        return "admin_page";
    }



    @PostMapping(value = "/new")
    public String saveNewUser(@ModelAttribute User user,
                              @RequestParam(value = "roless") String[] roles) {
        Set<Role> rolesSet = new HashSet<>();
        for (String role : roles) {
            rolesSet.add(roleService.findByName(role).get());
        }
        user.setRoles(rolesSet);
        userService.save(user);
        return "redirect:/admin";
    }



    @PostMapping(value = "/edit/{id}")
    public String editUser(@ModelAttribute User user,  @RequestParam(value = "roless") String[] roles) {
        Set<Role> rolesSet = new HashSet<>();
        for (String role : roles) {
            rolesSet.add(roleService.findByName(role).get());
        }
        user.setRoles(rolesSet);

        userService.update(user);
        return "redirect:/admin";
    }

    @PostMapping(value = "/delete/{id}")
    public String deleteUser(@ModelAttribute User user) {
        userService.delete(user.getId());
        return "redirect:/admin";
    }
}
