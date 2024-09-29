package com.example.rides.service;

import com.example.rides.repository.RidesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RidesService {

    private final RidesRepository ridesRepository;
}
