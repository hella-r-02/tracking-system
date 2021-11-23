package com.github.martynfunclub.trackingsystem.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.martynfunclub.trackingsystem.models.Change;
import com.github.martynfunclub.trackingsystem.models.User;
import com.github.martynfunclub.trackingsystem.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.github.martynfunclub.trackingsystem.repositories.ChangeRepository;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/change")
public class ChangeController {


    @Autowired
    ChangeRepository changeRepository;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/on") //TODO change to POST mapping
    public String changeOn(HttpServletRequest request, HttpServletResponse response, Model model) {
        LocalDateTime time = LocalDateTime.now().withNano(0); //taking current time
        Change change = new Change(); //creating new note in DB

        String userName = "";
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            userName = authentication.getName();
        } else {
            return "redirect:/login";
        }

        User user = userRepository.findByUsername(userName);
        //set necessary info
        change.setStartTime(time);
        change.setUser(user);

        //save changes
        changeRepository.save(change);
        return "redirect:/";  //TODO return the necessary page
    }

    @GetMapping("/off") //TODO change to POST mapping
    public String changeOff(HttpServletRequest request, HttpServletResponse response, Model model) {
        LocalDateTime time = LocalDateTime.now().withNano(0);  //taking current time

        //select user
        String userName = "";
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            userName = authentication.getName();
        } else {
            return "redirect:/login";
        }

        User user = userRepository.findByUsername(userName);
        //find note with no endtime in DB with this userid
        Change change = changeRepository.findByUserAndEndTimeIsNull(user);

        //set info
        change.setEndTime(time);

        //save changes
        changeRepository.save(change);
        return "redirect:/"; //TODO return the necessary page
    }
}
