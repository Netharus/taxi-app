package com.example.rides.controller;

import com.example.rides.service.RidesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1/rides")
@RequiredArgsConstructor
public class RidesController {

    private final RidesService ridesService;
}
