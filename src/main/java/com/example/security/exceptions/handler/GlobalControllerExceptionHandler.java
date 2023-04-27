package com.example.security.exceptions.handler;

import com.example.security.exceptions.AppointmentNotFoundException;
import com.example.security.exceptions.UserDataNotFoundException;
import com.example.security.exceptions.UserIsNotClientException;
import com.example.security.exceptions.UserIsNotProviderException;
import com.example.security.exceptions.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GlobalControllerExceptionHandler {
//    @ExceptionHandler(AppointmentNotFoundException.class)
//    protected ResponseEntity handleConflict(AppointmentNotFoundException e, HttpServletRequest request){
//        ErrorModel errorModel = new ErrorModel(LocalDate.now(), HttpStatus.BAD_REQUEST,
//                "Bad Request", "No appointment was found", request.getRequestURI());
//        return new ResponseEntity<>(errorModel, HttpStatus.BAD_REQUEST);
//    }
//    @ExceptionHandler(BookingTimeOverlapping.class)
//    protected ResponseEntity handleConflict(BookingTimeOverlapping e, HttpServletRequest request){
//        ErrorModel errorModel = new ErrorModel(LocalDate.now(), HttpStatus.BAD_REQUEST,
//                "Bad Request", "You already have an appointment at selected time", request.getRequestURI());
//        return new ResponseEntity<>(errorModel, HttpStatus.BAD_REQUEST);
//    }
//
//    @ExceptionHandler(AppointmentHasAlreadyBooked.class)
//    protected ResponseEntity handleConflict(AppointmentHasAlreadyBooked e, HttpServletRequest request){
//        ErrorModel errorModel = new ErrorModel(LocalDate.now(), HttpStatus.BAD_REQUEST,
//                "Bad Request", "Appointment has already been booked", request.getRequestURI());
//        return new ResponseEntity<>(errorModel, HttpStatus.BAD_REQUEST);
//    }

    @ExceptionHandler(ConstraintViolationException.class)
    ResponseEntity<ErrorModel> handleBindErrors(ConstraintViolationException exception, HttpServletRequest request, HttpServletResponse response) throws IOException {

        List errorList = exception.getConstraintViolations().stream()
                .map(fieldError -> {
                    Map<String, String > errorMap = new HashMap<>();
                    errorMap.put(fieldError.getPropertyPath().toString(), fieldError.getMessage());
                    return errorMap;
                }).toList();
        response.sendError(HttpStatus.BAD_REQUEST.value(), errorList.toString());

        ErrorModel errorModel = ErrorModel.builder()
                .timestamp(LocalDate.now())
                .status(HttpStatus.BAD_REQUEST)
                .message(errorList.toString())
                .path(request.getRequestURI())
                .build();

        return new ResponseEntity<>(errorModel, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(UserDataNotFoundException.class)
    ResponseEntity<ErrorModel> handleBindErrors(UserDataNotFoundException exception, HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.NOT_FOUND.value(),"User data was not found");

        ErrorModel errorModel = ErrorModel.builder()
                .timestamp(LocalDate.now())
                .status(HttpStatus.NOT_FOUND)
                .message("User data was not found")
                .path(request.getRequestURI())
                .build();

        return new ResponseEntity<>(errorModel, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserNotFoundException.class)
    ResponseEntity<ErrorModel> handleBindErrors(UserNotFoundException exception, HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.NOT_FOUND.value(),"User was not found");

        ErrorModel errorModel = ErrorModel.builder()
                .timestamp(LocalDate.now())
                .status(HttpStatus.NOT_FOUND)
                .message("User was not found")
                .path(request.getRequestURI())
                .build();

        return new ResponseEntity<>(errorModel, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({UserIsNotClientException.class})
    ResponseEntity<ErrorModel> handleBindErrors(UserIsNotClientException exception, HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value(),"User is not a client");

        ErrorModel errorModel = ErrorModel.builder()
                .timestamp(LocalDate.now())
                .status(HttpStatus.BAD_REQUEST)
                .message("User is not a client")
                .path(request.getRequestURI())
                .build();

        return new ResponseEntity<>(errorModel, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler({UserIsNotProviderException.class})
    ResponseEntity<ErrorModel> handleBindErrors(UserIsNotProviderException exception, HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value(),"User is not a provider");

        ErrorModel errorModel = ErrorModel.builder()
                .timestamp(LocalDate.now())
                .status(HttpStatus.BAD_REQUEST)
                .message("User is not a provider")
                .path(request.getRequestURI())
                .build();

        return new ResponseEntity<>(errorModel, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AppointmentNotFoundException.class)
    protected ResponseEntity handleConflict(AppointmentNotFoundException e, HttpServletRequest request){
        ErrorModel errorModel = new ErrorModel(LocalDate.now(), HttpStatus.NOT_FOUND,
                "Bad Request", "No appointment was found", request.getRequestURI());
        return new ResponseEntity<>(errorModel, HttpStatus.NOT_FOUND);
    }

}
