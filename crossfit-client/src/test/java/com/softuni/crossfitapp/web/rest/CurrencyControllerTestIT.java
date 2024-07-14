package com.softuni.crossfitapp.web.rest;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.math.BigDecimal;

import com.softuni.crossfitapp.testUtils.TestData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


@SpringBootTest
@AutoConfigureMockMvc
class CurrencyControllerTestIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestData testData;

    @Test
    public void testConvert() throws Exception {

        // Set up test data for valid conversion
        testData.createExchangeRate("EUR", BigDecimal.valueOf(0.93));
        testData.createExchangeRate("BGN", BigDecimal.valueOf(1.81));

        // Perform the conversion request
        this.mockMvc.perform(MockMvcRequestBuilders.get("/crossfit/api/convert")
                        .param("from", "BGN")
                        .param("to", "EUR")
                        .param("amount", "1000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result", is(510.0)))
                .andExpect(jsonPath("$.from", is("BGN")))
                .andExpect(jsonPath("$.to", is("EUR")))
                .andExpect(jsonPath("$.amount", is(1000)));

    }
    @Test
    public void testConvertObjectNotFoundException() throws Exception {
        // Perform the conversion request with invalid 'from' currency
        this.mockMvc.perform(MockMvcRequestBuilders.get("/crossfit/api/convert")
                        .param("from", "INVALID")
                        .param("to", "BGN")
                        .param("amount", "1000"))
                .andExpect(jsonPath("$.result").doesNotExist());

        // Perform the conversion request with invalid 'to' currency
        this.mockMvc.perform(MockMvcRequestBuilders.get("/crossfit/api/convert")
                        .param("from", "EUR")
                        .param("to", "INVALID")
                        .param("amount", "1000"))
                .andExpect(jsonPath("$.result").doesNotExist());
    }

}