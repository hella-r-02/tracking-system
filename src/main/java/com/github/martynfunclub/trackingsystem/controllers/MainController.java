package com.github.martynfunclub.trackingsystem.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.github.martynfunclub.trackingsystem.models.WorkersPlace;
import com.github.martynfunclub.trackingsystem.repositories.ProductionRepository;
import com.github.martynfunclub.trackingsystem.services.PlaceService;

@Controller
public class MainController {

    PlaceService placeService;
    ProductionRepository productionRepository;

    @Autowired
    public MainController(PlaceService placeService, ProductionRepository productionRepository) {
        this.placeService = placeService;
        this.productionRepository = productionRepository;
    }

    @GetMapping("/")
    public String main(HttpServletRequest request, Model model) {
        List<WorkersPlace> places = placeService.getCurrentPlaces(request.getCookies());
        for (WorkersPlace place : places) {
            place.setCurrentProduction(productionRepository.getProductionByPlaceAndEndTimeIsNull(place));
        }
        model.addAttribute("places", places);
        model.addAttribute("title", "Станки");
        return "main";
    }
}
