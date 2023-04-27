package com.example.security.service;

import com.example.security.responsebodymodel.AppointmentDetailDto;
import com.example.security.responsebodymodel.AppointmentDto;
import com.example.security.responsebodymodel.AppointmentRequest;

import java.util.List;


public interface AppointmentService {
    List<AppointmentDto> getUserWeekAppointments(Integer week, String role, Long userId);

    AppointmentDto getAppointmentById(Long id);

    AppointmentDetailDto getAppointmentDetailById(Long id);

    AppointmentDto createAppointment(AppointmentRequest appointmentRequest);

    void deleteAppointment(Long id);

    void cancelAppointment(Long id);

    void changeAppointmentStatus(Long id);

    void changeAppointmentType(String appointmentType, Long id);

    void bookAnAppointment(Long clientId, Long appointmentId, String details);

}
