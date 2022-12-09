package com.seokjin.travelguide.controller;

import static com.seokjin.travelguide.RestDocsHelper.constraint;
import static com.seokjin.travelguide.RestDocsHelper.customDocument;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seokjin.travelguide.WithMockCustomUser;
import com.seokjin.travelguide.dto.request.trip.TripCreateRequest;
import com.seokjin.travelguide.dto.response.trip.TripCreateResponse;
import com.seokjin.travelguide.service.trip.TripService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(TripController.class)
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "api.travelguide.com", uriPort = 443)
@ExtendWith(RestDocumentationExtension.class)
class TripControllerTest {

    @MockBean
    TripService tripService;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("tripCreate 요청 시 HTTP 200 상태 코드를 반환하고 생성된 ID를 반환한다.")
    @WithMockCustomUser
    void tripCreateRequestCouldBeReturn200StatusAndGeneratedId() throws Exception {
        // given
        TripCreateRequest request = new TripCreateRequest();
        request.setTitle("test title");
        request.setContents("test contents");
        request.setDesc("test desc");
        request.setCountry("south korea");
        request.setCity("seoul");
        request.setLatitude("37.532600");
        request.setLongitude("127.024612");

        TripCreateResponse response = new TripCreateResponse(1L);

        doReturn(response).when(tripService)
                .create(any(TripCreateRequest.class), any(String.class));

        // expect
        mockMvc.perform(post("/api/v1/trips")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.tripId").value(1))
                .andDo(customDocument("trip-create",
                        requestFields(
                                fieldWithPath("title").description("제목"),
                                fieldWithPath("desc").description("설명"),
                                fieldWithPath("country").description("나라"),
                                fieldWithPath("city").description("도시"),
                                fieldWithPath("contents").description("내용"),
                                fieldWithPath("latitude").description("위도")
                                        .attributes(constraint("-90~90 이내")),
                                fieldWithPath("longitude").description("경도")
                                        .attributes(constraint("-180~180 이내"))
                        )
                ));
    }

    @ParameterizedTest
    @DisplayName("tripCreate 요청 시 위도는 -90 ~ 90 사이의 값이어야 한다.")
    @ValueSource(strings = {"-90.1", "-91", "90.1", "91"})
    void tripCreateRequestByLatitudeCouldBeMinus90ToPlus90(String latitude) throws Exception {
        // given
        TripCreateRequest request = new TripCreateRequest();
        request.setTitle("test title");
        request.setContents("test contents");
        request.setDesc("test desc");
        request.setCountry("south korea");
        request.setCity("seoul");
        request.setLatitude(latitude);
        request.setLongitude("127.024612");

        // expect
        mockMvc.perform(post("/api/v1/trips")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @DisplayName("tripCreate 요청 시 경도는 -180 ~ 180 사이의 값이어야 한다.")
    @ValueSource(strings = {"-180.1", "-181", "180.1", "181"})
    void tripCreateRequestByLongitudeCouldBeMinus180ToPlus180(String longitude) throws Exception {
        // given
        TripCreateRequest request = new TripCreateRequest();
        request.setTitle("test title");
        request.setContents("test contents");
        request.setDesc("test desc");
        request.setCountry("south korea");
        request.setCity("seoul");
        request.setLatitude("37.532600");
        request.setLongitude(longitude);

        // expect
        mockMvc.perform(post("/api/v1/trips")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("tripCreate 요청 시 모든 필드는 필수다.")
    void tripCreateRequestByAllFieldCouldBeNotEmpty() throws Exception {
        // given
        TripCreateRequest request = new TripCreateRequest();

        // expect
        mockMvc.perform(post("/api/v1/trips")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validation.title").exists())
                .andExpect(jsonPath("$.validation.contents").exists())
                .andExpect(jsonPath("$.validation.desc").exists())
                .andExpect(jsonPath("$.validation.country").exists())
                .andExpect(jsonPath("$.validation.city").exists())
                .andExpect(jsonPath("$.validation.latitude").exists())
                .andExpect(jsonPath("$.validation.longitude").exists());
    }
}