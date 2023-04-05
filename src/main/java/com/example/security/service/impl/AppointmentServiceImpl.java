package com.example.security.service.impl;

import com.example.security.repository.AppointmentRepository;
import com.example.security.responseBodyModel.AppointmentDetailDto;
import com.example.security.responseBodyModel.AppointmentDto;
import com.example.security.responseBodyModel.AppointmentRequest;
import com.example.security.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    @Override
    public List<AppointmentDto> getUserWeekAppointments(Integer week, String role, Long userId){
        return appointmentRepository.getUsersWeekAppointment(week, role, userId);
    }

    @Override
    public AppointmentDto getAppointmentById(Long id){
        return appointmentRepository.getAppointmentById(id);
    }

    @Override
    public AppointmentDetailDto getAppointmentDetailById(Long id){
        return appointmentRepository.getAppointmentDetailByAppId(id);
    }

    @Override
    public AppointmentDto createAppointment(AppointmentRequest appointmentRequest){
        return appointmentRepository.createAppointment(appointmentRequest);
    }

    @Override
    public void deleteAppointment(Long id){
        appointmentRepository.deleteAppointment(id);
    }

    @Override
    public void cancelAppointment(Long id){
        appointmentRepository.cancelAppointment(id);
    }

    @Override
    public void changeAppointmentStatus(Long id){
        appointmentRepository.changeAppointmentStatus(id);
    }

    @Override
    public void changeAppointmentType(String appointmentType, Long id){
        appointmentRepository.changeAppointmentType(appointmentType, id);
    }

    @Override
    public  void bookAnAppointment(Long clientId, Long appointmentId, String details){
        appointmentRepository.bookAppointment(clientId, appointmentId, details);
    }
}
