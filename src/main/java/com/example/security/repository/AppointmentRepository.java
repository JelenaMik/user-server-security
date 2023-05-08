package com.example.security.repository;

import com.example.security.model.AppointmentDetailDto;
import com.example.security.model.AppointmentDto;
import com.example.security.model.AppointmentRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AppointmentRepository {

    private static final String APPOINTMENT_SERVICE_BASE_URL= "http://localhost:8103/api/v1/appointments/";
    private static final String APPOINTMENT_DETAIL_SERVICE_BASE_URL= "http://localhost:8103/api/v1/appointment-details/";

    @Autowired
    private RestTemplate restTemplate;


    public List<AppointmentDto> getUsersWeekAppointment(Integer week, String role, Long userId){
        return restTemplate.getForObject(APPOINTMENT_SERVICE_BASE_URL +"week-appointments/"+week+"?role="+role+"&userId="+userId, List.class);
    }

    public Optional<AppointmentDto> getAppointmentById(Long id){
        return Optional.ofNullable(restTemplate.getForObject(APPOINTMENT_SERVICE_BASE_URL+id, AppointmentDto .class));
    }

    public Optional<AppointmentDetailDto> getAppointmentDetailByAppId(Long id){
        return Optional.ofNullable(restTemplate.getForObject(APPOINTMENT_DETAIL_SERVICE_BASE_URL+id, AppointmentDetailDto .class));
    }

    public Optional<AppointmentDto> createAppointment(AppointmentRequest appointmentRequest){
        return Optional.ofNullable(restTemplate.postForObject(APPOINTMENT_SERVICE_BASE_URL+"create-appointment", appointmentRequest, AppointmentDto.class));
    }

    public void deleteAppointment(Long id){
        restTemplate.delete(APPOINTMENT_SERVICE_BASE_URL+"delete?appointmentId="+id);
    }

    public void cancelAppointment(Long id){
        restTemplate.put(APPOINTMENT_SERVICE_BASE_URL+"cancel?appointmentId="+id, AppointmentDto.class);
    }

    public void changeAppointmentStatus(Long id){
        restTemplate.put(APPOINTMENT_DETAIL_SERVICE_BASE_URL+"change-status/"+id, HttpStatus.class);
    }

    public void changeAppointmentType(String appointmentType, Long id){
        restTemplate.put(APPOINTMENT_SERVICE_BASE_URL+"/change-type?type="+appointmentType+"&appointmentId="+id, AppointmentDto.class);
    }

    public void bookAppointment(Long clientId, Long appointmentId, String details){
        restTemplate.put(APPOINTMENT_SERVICE_BASE_URL+"book-appointment?clientId="+ clientId+ "&appointmentId=" + appointmentId + "&details="+ details, AppointmentDto.class);
    }
}
