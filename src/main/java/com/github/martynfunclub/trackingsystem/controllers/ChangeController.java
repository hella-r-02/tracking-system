package com.github.martynfunclub.trackingsystem.controllers;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.martynfunclub.trackingsystem.models.Change;
import com.github.martynfunclub.trackingsystem.models.Production;
import com.github.martynfunclub.trackingsystem.models.User;
import com.github.martynfunclub.trackingsystem.models.WorkersPlace;
import com.github.martynfunclub.trackingsystem.repositories.ProductionRepository;
import com.github.martynfunclub.trackingsystem.repositories.UserRepository;
import com.github.martynfunclub.trackingsystem.services.PlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.github.martynfunclub.trackingsystem.repositories.ChangeRepository;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Controller
@RequestMapping("/change")
public class ChangeController {

    @Autowired
    PlaceService placeService;

    @Autowired
    ChangeRepository changeRepository;

    @Autowired
    ProductionRepository productionRepository;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/on")
    public String changeOn(HttpServletRequest request, HttpServletResponse response, Model model) {
        LocalDateTime time = LocalDateTime.now().withNano(0); //taking current time
        Change change = new Change(); //creating new note in DB

        //getting some info for Production from cookies
        List<WorkersPlace> places = placeService.getCurrentPlaces(request.getCookies());
        Cookie[] cookies = request.getCookies();
        Set<Production> productions = new HashSet<Production>();
        for (WorkersPlace place : places) {
            System.out.println(place);
            productions.add(productionRepository.getProductionByPlace(place));
            place.setCurrentProduction(productionRepository.getProductionByPlaceAndEndTimeIsNull(place));
        }


        String currentUserName = "";
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            currentUserName = authentication.getName();
        }
        User user = userRepository.findByUsername(currentUserName);

        //set necessary info
        change.setStartTime(time);
        change.setProductions(productions);
        change.setUser(user);

        //save changes
        changeRepository.save(change);
        return "redirect:/";  //TODO return the necessary page
    }

    @GetMapping("/off")
    public String changeOff(HttpServletRequest request, HttpServletResponse response, Model model) {
        LocalDateTime time = LocalDateTime.now().withNano(0);  //taking current time

        //getting some info for Production from cookies
        List<WorkersPlace> places = placeService.getCurrentPlaces(request.getCookies());
        Cookie[] cookies = request.getCookies();
        Set<Production> productions = new HashSet<Production>();
        for (WorkersPlace place : places) {
            System.out.println(place);
            productions.add(productionRepository.getProductionByPlace(place));
            place.setCurrentProduction(productionRepository.getProductionByPlaceAndEndTimeIsNull(place));

        }
        //select user
        String currentUserName = "";
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            currentUserName = authentication.getName();
        }
        User user = userRepository.findByUsername(currentUserName);

        //find note with no endtime in DB with this userid
        Change change = changeRepository.findByUserAndEndTimeIsNull(user);

        //set info
        change.setProductions(productions);
        change.setEndTime(time);

        //save changes
        changeRepository.save(change);
        return "redirect:/"; //TODO return the necessary page
    }
}
