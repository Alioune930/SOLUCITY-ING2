package esiag.back.controllers.Pollen;

import esiag.back.SolucityBackApplication;
import esiag.back.services.Pollen.ZonePollenService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ZonePollenController.class)
@ContextConfiguration(classes = SolucityBackApplication.class)
public class ZonePollenControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ZonePollenService zonePollenService;

    @Test
    void testerRetourFormatGeoJson() throws Exception {
        Map<String, Object> mockGeoJson = Map.of(
            "type", "FeatureCollection",
            "features", Collections.emptyList()
        );

        Mockito.when(zonePollenService.getZonesGeoJson()).thenReturn(mockGeoJson);

        mockMvc.perform(get("/zones-pollen")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.type").value("FeatureCollection"))
                .andExpect(jsonPath("$.features").isArray());
    }
}