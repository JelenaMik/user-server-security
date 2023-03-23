package com.example.security.web;

import com.example.security.service.UserService;
import com.example.security.service.WebService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AppointmentController {
    private final UserService userService;
    private final WebService webService;
    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/my-appointments")
    public String myAppointmentPage(Model model){
        List<Integer> daysOfWeek = webService.getListOfDates();
        model.addAttribute("daysOfWeek", daysOfWeek);
        model.addAttribute("month", new LocalDate().monthOfYear().getAsText());
        model.addAttribute("year", new LocalDate().getYear());
        return "my-appointments";
    }
}
