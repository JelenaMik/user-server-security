package com.example.security.repository;

import com.example.security.responseBodyModel.AppointmentDetailDto;
import com.example.security.responseBodyModel.AppointmentDto;
import com.example.security.responseBodyModel.AppointmentRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class AppointmentRepository {

    private final RestTemplate restTemplate;

    public List<AppointmentDto> getUsersWeekAppointment(Integer week, String role, Long userId){
        return restTemplate.getForObject("http://appointment-service/api/v1/appointments/week-appointments/"+week+"?role="+role+"&userId="+userId, List.class);
    }

    public AppointmentDto getAppointmentById(Long id){
        return restTemplate.getForObject("http://appointment-service/api/v1/appointments/"+id, AppointmentDto .class);
    }

    public AppointmentDetailDto getAppointmentDetailByAppId(Long id){
        return restTemplate.getForObject("http://appointment-service/api/v1/appointment-details/"+id, AppointmentDetailDto .class);
    }

    public AppointmentDto createAppointment(AppointmentRequest appointmentRequest){
        return restTemplate.postForObject("http://appointment-service/api/v1/appointments/create-appointment", appointmentRequest, AppointmentDto.class);
    }

    public void deleteAppointment(Long id){
        restTemplate.delete("http://appointment-service/api/v1/appointments/delete?appointmentId="+id);
    }

    public void cancelAppointment(Long id){
        restTemplate.put("http://appointment-service/api/v1/appointments/cancel?appointmentId="+id, AppointmentDto.class);
    }

    public void changeAppointmentStatus(Long id){
        restTemplate.put("http://appointment-service/api/v1/appointment-details/change-status/"+id, HttpStatus.class);
    }

    public void changeAppointmentType(String appointmentType, Long id){
        restTemplate.put("http://appointment-service/api/v1/appointments/change-type?type="+appointmentType+"&appointmentId="+id, AppointmentDto.class);
    }

    public void bookAppointment(Long clientId, Long appointmentId, String details){
        restTemplate.put("http://appointment-service/api/v1/appointments/book-appointment?clientId="+ clientId+ "&appointmentId=" + appointmentId + "&details="+ details, AppointmentDto.class);
    }
}
