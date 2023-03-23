package com.example.security.service.impl;

import com.example.security.service.WebService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@RequiredArgsConstructor
@Service
public class WebServiceImpl implements WebService {

    @Override
    public List<Integer> getListOfDates(){
        LocalDate now = new LocalDate();
        LocalDate monday = now.withDayOfWeek(DateTimeConstants.MONDAY);

        List<LocalDate> currentWeek = new ArrayList<>();
        for(int i=0; i<7; i++){
            currentWeek.add(monday.plusDays(i));
        }

        List<Integer> days = currentWeek.stream()
                .map( date -> date.getDayOfMonth() )
                .collect(Collectors.toList());
        return days;
    }
}
