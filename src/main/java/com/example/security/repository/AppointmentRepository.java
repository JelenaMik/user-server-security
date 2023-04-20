package com.example.security.repository;

import com.example.security.responseBodyModel.AppointmentDetailDto;
import com.example.security.responseBodyModel.AppointmentDto;
import com.example.security.responseBodyModel.AppointmentRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class AppointmentRepository {

    private static final String APPOINTMENT_SERVICE_BASE_URL= "http://localhost:8103/api/v1/appointments/";
    private static final String APPOINTMENT_DETAIL_SERVICE_BASE_URL= "http://localhost:8103/api/v1/appointment-details/";

//    private final RestTemplateBuilder restTemplateBuilder;

    @Autowired
    private RestTemplate restTemplate;


    public List<AppointmentDto> getUsersWeekAppointment(Integer week, String role, Long userId){
//        RestTemplate restTemplate = restTemplateBuilder.build();
        return restTemplate.getForObject(APPOINTMENT_SERVICE_BASE_URL +"week-appointments/"+week+"?role="+role+"&userId="+userId, List.class);
    }

    public AppointmentDto getAppointmentById(Long id){
//        RestTemplate restTemplate = restTemplateBuilder.build();
        return restTemplate.getForObject(APPOINTMENT_SERVICE_BASE_URL+id, AppointmentDto .class);
    }

    public AppointmentDetailDto getAppointmentDetailByAppId(Long id){
//        RestTemplate restTemplate = restTemplateBuilder.build();
        return restTemplate.getForObject(APPOINTMENT_DETAIL_SERVICE_BASE_URL+id, AppointmentDetailDto .class);
    }

    public AppointmentDto createAppointment(AppointmentRequest appointmentRequest){
//        RestTemplate restTemplate = restTemplateBuilder.build();
        return restTemplate.postForObject(APPOINTMENT_SERVICE_BASE_URL+"create-appointment", appointmentRequest, AppointmentDto.class);
    }

    public void deleteAppointment(Long id){
//        RestTemplate restTemplate = restTemplateBuilder.build();
        restTemplate.delete(APPOINTMENT_SERVICE_BASE_URL+"delete?appointmentId="+id);
    }

    public void cancelAppointment(Long id){
//        RestTemplate restTemplate = restTemplateBuilder.build();
        restTemplate.put(APPOINTMENT_SERVICE_BASE_URL+"cancel?appointmentId="+id, AppointmentDto.class);
    }

    public void changeAppointmentStatus(Long id){
//        RestTemplate restTemplate = restTemplateBuilder.build();
        restTemplate.put(APPOINTMENT_DETAIL_SERVICE_BASE_URL+"change-status/"+id, HttpStatus.class);
    }

    public void changeAppointmentType(String appointmentType, Long id){
//        RestTemplate restTemplate = restTemplateBuilder.build();
        restTemplate.put(APPOINTMENT_SERVICE_BASE_URL+"/change-type?type="+appointmentType+"&appointmentId="+id, AppointmentDto.class);
    }

    public void bookAppointment(Long clientId, Long appointmentId, String details){
//        RestTemplate restTemplate = restTemplateBuilder.build();
        restTemplate.put(APPOINTMENT_SERVICE_BASE_URL+"book-appointment?clientId="+ clientId+ "&appointmentId=" + appointmentId + "&details="+ details, AppointmentDto.class);
    }
}
