package com.example.security.web;

import com.example.security.responseBodyModel.AppointmentDetailDto;
import com.example.security.responseBodyModel.AppointmentDto;
import com.example.security.responseBodyModel.UserData;
import com.example.security.service.UserService;
import com.example.security.service.WebService;
import com.example.security.repository.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AppointmentController {
    private final UserService userService;
    private final WebService webService;
    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/my-appointments/{role}/{userId}/{week}")
    public String myAppointmentPage(@PathVariable Integer week,
                                    @PathVariable String role,
                                    @PathVariable Long userId,
                                    Model model){

        List<Integer> daysOfWeek = webService.getListOfDates(week);
        List<AppointmentDto> appointmentList = restTemplate.getForObject("http://appointment-service/api/v1/appointments/week-appointments/"+week+"?role="+role+"&userId="+userId, List.class);
        log.info("Appointment list {}", appointmentList);
        model.addAttribute("daysOfWeek", daysOfWeek);
        model.addAttribute("appointmentList", appointmentList);
        model.addAttribute("week", week);
        model.addAttribute("weekNr", webService.getMonthNumber(week));
        model.addAttribute("role", role);
        model.addAttribute("userId", userId);
        model.addAttribute("month", webService.getMonth(week));
        model.addAttribute("year", webService.getYear(week));
        return "my-appointments";
    }
//    @GetMapping("/my-appointments")
//    public String myAppointmentPage(Model model){
//        List<Integer> daysOfWeek = webService.getListOfDates(new LocalDate().getWeekOfWeekyear());
//        List<AppointmentDto> appointmentList = restTemplate.getForObject("http://appointment-service/api/v1/appointments/week-appointments/"+new LocalDate().getWeekOfWeekyear(), List.class);
//        model.addAttribute("daysOfWeek", daysOfWeek);
//        model.addAttribute("month", new LocalDate().getWeekOfWeekyear());
//        model.addAttribute("year", new LocalDate().getWeekOfWeekyear());
//        return "my-appointments";
//    }

    @PostMapping("/collect-appointment-data")
    public String collectAppointmentData(String role, Long userId){
        Integer week = new LocalDate().getWeekOfWeekyear();
        log.info(String.valueOf(week));
        return "redirect:my-appointments/"+role+"/"+userId+"/"+week;
    }

    @PostMapping("/view-appointment")
    public String viewAppointmentPage(Long appointmentId){
        return "redirect:appointment/"+appointmentId;
    }

    @GetMapping("/appointment/{id}")
    public String displayAppointmentPage(@PathVariable Long id, Model model){
        AppointmentDto appointmentDto = restTemplate.getForObject("http://appointment-service/api/v1/appointments/"+id, AppointmentDto.class);
        AppointmentDetailDto appointmentDetails = restTemplate.getForObject("http://appointment-service/api/v1/appointment-details/"+id, AppointmentDetailDto.class);
        UserData clientData = new UserData();
        UserData providerData = new UserData();
        assert appointmentDto != null;
        if(appointmentDto.getClientId() !=null) clientData = restTemplate.getForObject("http://user-data-service/api/v1/userdata/get-data/"+appointmentDto.getClientId(),  UserData.class);
        if(appointmentDto.getProviderId() != null) providerData = restTemplate.getForObject("http://user-data-service/api/v1/userdata/get-data/"+appointmentDto.getProviderId(),  UserData.class);
        model.addAttribute("appointment", appointmentDto);
        model.addAttribute("appointmentDetails", appointmentDetails);
        model.addAttribute("clientData", clientData);
        model.addAttribute("providerData", providerData);
        return "appointment";
    }
}
