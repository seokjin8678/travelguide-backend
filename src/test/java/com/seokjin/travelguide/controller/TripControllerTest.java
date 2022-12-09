package com.seokjin.travelguide.controller;

import static com.seokjin.travelguide.RestDocsHelper.constraint;
import static com.seokjin.travelguide.RestDocsHelper.customDocument;
import static java.util.Comparator.*;
import static java.util.stream.Collectors.toList;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seokjin.travelguide.WithMockCustomUser;
import com.seokjin.travelguide.dto.request.trip.TripCreateRequest;
import com.seokjin.travelguide.dto.request.trip.TripSearchRequest;
import com.seokjin.travelguide.dto.response.trip.TripCreateResponse;
import com.seokjin.travelguide.dto.response.trip.TripDetailResponse;
import com.seokjin.travelguide.dto.response.trip.TripPreviewResponse;
import com.seokjin.travelguide.service.trip.TripService;
import java.util.List;
import java.util.stream.LongStream;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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
    @DisplayName("/api/v1/trips로 POST 요청 시 HTTP 200 상태 코드와 생성된 ID를 반환한다.")
    @WithMockCustomUser
    void tripsPostRequestCouldBeReturn200StatusAndGeneratedId() throws Exception {
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
                        .contentType(APPLICATION_JSON)
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
    @DisplayName("/api/v1/trips로 POST 요청 시 위도는 -90 ~ 90 사이의 값이어야 한다.")
    @ValueSource(strings = {"-90.1", "-91", "90.1", "91"})
    void tripsPostRequestByLatitudeCouldBeMinus90ToPlus90(String latitude) throws Exception {
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
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @DisplayName("/api/v1/trips로 POST 요청 시 경도는 -180 ~ 180 사이의 값이어야 한다.")
    @ValueSource(strings = {"-180.1", "-181", "180.1", "181"})
    void tripsPostRequestByLongitudeCouldBeMinus180ToPlus180(String longitude) throws Exception {
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
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("/api/v1/trips로 POST 요청 시 모든 필드는 필수다.")
    void tripsPostRequestByAllFieldCouldBeNotEmpty() throws Exception {
        // given
        TripCreateRequest request = new TripCreateRequest();

        // expect
        mockMvc.perform(post("/api/v1/trips")
                        .contentType(APPLICATION_JSON)
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

    @Test
    @DisplayName("/api/v1/trips로 GET 요청 시 요청 시 HTTP 200 상태 코드와 TripPreviews가 반환되어야 한다.")
    void tripsGetRequestCouldBeReturn200StatusAndTripPreviews() throws Exception {
        // given
        TripSearchRequest tripSearchRequest = new TripSearchRequest();
        PageRequest pageable = PageRequest.of(tripSearchRequest.getPage(), tripSearchRequest.getSize());
        List<TripPreviewResponse> tripPreviews = LongStream.rangeClosed(1, 10)
                .mapToObj(i -> TripPreviewResponse.builder()
                        .id(i)
                        .title("title " + i)
                        .score(0)
                        .desc("desc " + i)
                        .country("county " + i)
                        .city("city " + i)
                        .author("author " + i)
                        .build())
                .sorted(comparing(TripPreviewResponse::getId).reversed())
                .collect(toList());
        Page<TripPreviewResponse> tripPreviewPage = new PageImpl<>(tripPreviews.subList(0, 8), pageable,
                tripPreviews.size());

        doReturn(tripPreviewPage).when(tripService)
                .getPreviews(any(TripSearchRequest.class));

        // expect
        mockMvc.perform(get("/api/v1/trips")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title").value("title 10"))
                .andExpect(jsonPath("$.content.size()").value(8))
                .andExpect(jsonPath("$.totalElements").value(10))
                .andDo(customDocument("trip-previews-inquiry",
                        requestParameters(
                                parameterWithName("page").description("기본값 1").optional(),
                                parameterWithName("size").description("기본값 8").optional()
                        )
                ));
    }

    @Test
    @DisplayName("/api/v1/trips/{tripId}로 GET 요청 시 요청 시 HTTP 200 상태 코드와 TripDetail이 반환되어야 한다.")
    void tripsWithTripIdGetRequestCouldBeReturn200StatusAndTripDetail() throws Exception {
        // given
        TripDetailResponse tripDetailResponse = TripDetailResponse.builder()
                .id(1L)
                .title("title")
                .desc("desc")
                .country("south korea")
                .city("seoul")
                .author("author")
                .latitude("37.532600")
                .longitude("127.024612")
                .score(0)
                .contents("contents")
                .build();
        doReturn(tripDetailResponse).when(tripService)
                .getDetail(any(Long.class));

        // expect
        mockMvc.perform(get("/api/v1/trips/{tripId}", 1L)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.title").value("title"))
                .andExpect(jsonPath("$.result.city").value("seoul"))
                .andDo(customDocument("trip-detail-inquiry",
                        pathParameters(
                                parameterWithName("tripId").description("여행 ID")
                        ),
                        responseFields(
                                fieldWithPath("code").description("HTTP Status"),
                                fieldWithPath("message").description("결과 메시지"),
                                fieldWithPath("result.id").description("ID"),
                                fieldWithPath("result.title").description("제목"),
                                fieldWithPath("result.desc").description("설명"),
                                fieldWithPath("result.country").description("나라"),
                                fieldWithPath("result.city").description("도시"),
                                fieldWithPath("result.author").description("작성자"),
                                fieldWithPath("result.latitude").description("위도"),
                                fieldWithPath("result.longitude").description("경도"),
                                fieldWithPath("result.score").description("평점"),
                                fieldWithPath("result.contents").description("내용")
                        )
                ));
    }
}