package com.hr_management.hr_management.controller;

import com.hr_management.hr_management.mapper.CountryMapper;
import com.hr_management.hr_management.model.dto.ApiResponseDto;
import com.hr_management.hr_management.model.dto.country.CountryDTO;
import com.hr_management.hr_management.model.entity.Country;
import com.hr_management.hr_management.model.entity.Region;
import com.hr_management.hr_management.repository.CountryRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class CountryControllerTest {
    @Mock
    private CountryRepository countryRepo;

    @Mock
    private CountryMapper mapper;

    @InjectMocks
    private CountryController countryController;

    @Mock
    private HttpServletRequest request;

    private Country testCountry;
    private CountryDTO testCountryDTO;
    private Region testRegion;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Setting-up the test data
        testRegion = new Region();
        testRegion.setRegionId(BigDecimal.valueOf(10));
        testRegion.setRegionName("Europe");

        testCountry = new Country();
        testCountry.setCountryId("GR");
        testCountry.setCountryName("Greece");
        testCountry.setRegion(testRegion);

        testCountryDTO = new CountryDTO();
        testCountryDTO.setCountryId("GR");
        testCountryDTO.setCountryName("Greece");
        testCountryDTO.setRegion(testRegion);
    }

    @Test
    void testGetAllCountries() {
        List<Country> countryList = List.of(testCountry);
        when(countryRepo.findAll()).thenReturn(countryList);
        when(mapper.mapToCountryDTO(testCountry)).thenReturn(testCountryDTO);
        when(request.getRequestURI()).thenReturn("/api/countries");

        //When
        ResponseEntity<ApiResponseDto> response = countryController.findAll(request);

        //then
        assertEquals(200, response.getStatusCode().value());
        Assertions.assertNotNull(response.getBody());
        assertEquals("List of all countries ", response.getBody().getMessage());

        verify(countryRepo, times(1)).findAll() ;
    }

    @Test
    void testGetCountryById_Success() {
        Country country = testCountry;
        when(countryRepo.findByCountryId(country.getCountryId())).thenReturn(country);
        when(mapper.mapToCountryDTO(country)).thenReturn(testCountryDTO);
        when(request.getRequestURI()).thenReturn("/api/countries/{id}" + country.getCountryId());

        //When
        ResponseEntity<CountryDTO> response = countryController.findUsingId(country.getCountryId());


        assertEquals(200, response.getStatusCode().value());
        Assertions.assertNotNull(response.getBody());
        assertEquals("GR", response.getBody().getCountryId());
        assertEquals("Greece", response.getBody().getCountryName());

        verify(countryRepo, times(1)).findByCountryId(country.getCountryId());
        verify(mapper, times(1)).mapToCountryDTO(testCountry);
    }

    @Test
    void testGetCountryByRegion_Success() {
        List<Country> countryList = List.of(testCountry);
        when(countryRepo.findByRegion_RegionId(testRegion.getRegionId())).thenReturn(countryList);
        when(mapper.mapToCountryDTO(testCountry)).thenReturn(testCountryDTO);

        //When
        ResponseEntity<List<CountryDTO>> response = countryController.findByRegionId(testRegion.getRegionId());
        assertEquals(200, response.getStatusCode().value());
        Assertions.assertNotNull(response.getBody());

        CountryDTO resultDTO = response.getBody().get(0);
        System.out.println("resultDTO = " + resultDTO);

        assertEquals("GR", resultDTO.getCountryId());
        assertEquals("Greece", resultDTO.getCountryName());

        verify(countryRepo, times(1)).findByRegion_RegionId(testRegion.getRegionId());
        verify(mapper, times(1)).mapToCountryDTO(testCountry);
    }

    @Test
    void testSave_Success() {
        Country country = testCountry;
        when(countryRepo.save(country)).thenReturn(country);
        when(mapper.mapToCountryDTO(country)).thenReturn(testCountryDTO);

        //When
        ResponseEntity<CountryDTO> response = countryController.save(testCountryDTO);

        //Then
        assertEquals(200, response.getStatusCode().value());
        Assertions.assertNotNull(response.getBody());
        assertEquals("GR", response.getBody().getCountryId());
        assertEquals("Greece", response.getBody().getCountryName());
        assertEquals(new BigDecimal("10"), response.getBody().getRegion().getRegionId());
        assertEquals("Europe", response.getBody().getRegion().getRegionName());

        verify(countryRepo, times(1)).save(country);
        verify(mapper, times(1)).mapToCountryDTO(country);
    }

    @Test
    void testUpdate_Success() {
        Country country = testCountry;
        when(countryRepo.findByCountryId(country.getCountryId())).thenReturn(country);
        when(mapper.mapToCountryDTO(country)).thenReturn(testCountryDTO);
        when(countryRepo.save(any(Country.class))).thenReturn(country);

        ResponseEntity<CountryDTO> response = countryController.update(testCountryDTO, country.getCountryId()) ;
        assertEquals(200, response.getStatusCode().value());
        Assertions.assertNotNull(response.getBody());
        assertEquals("GR", response.getBody().getCountryId());
        assertEquals("Greece", response.getBody().getCountryName());
        assertEquals(new BigDecimal("10"), response.getBody().getRegion().getRegionId());
        assertEquals("Europe", response.getBody().getRegion().getRegionName());

        verify(countryRepo, times(1)).findByCountryId(country.getCountryId());
        verify(countryRepo, times(1)).save(country);
        verify(mapper, times(1)).mapToCountryDTO(country);
    }
}
