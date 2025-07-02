package com.demo.pteam.schedule;

import com.demo.pteam.AuthenticatedTest;
import com.demo.pteam.security.exception.AuthenticationErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ScheduleIntegrationTest extends AuthenticatedTest {
    private static final String SCHEDULE_URL = "/api/schedules";

    // year, month 생략시 현재 날짜 기준으로 조회
    @DisplayName("일정 조회 성공")
    @CsvSource(value = {
            "2025, 5",
            "2025,",    // month 생략
            ",5",       // year 생략
            ","         // month, year 생략
    })
    @ParameterizedTest
    void readScheduleByUser(String year, String month) throws Exception {
        // given
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("roleType", "user");
        params.add("year", year);
        params.add("month", month);

        // when
        ResultActions resultActions = mockMvc.perform(
                get(SCHEDULE_URL).params(params).headers(getHttpHeaders())
        );

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("message").value("회원정보 조회 성공"))
                .andExpect(jsonPath("data[*].userId", everyItem(equalTo(1))))
                .andExpect(jsonPath("data[*].startTime", everyItem(startsWith("2025-05"))));
    }

    /*
    // year, month 생략시 현재 날짜 기준으로 조회
    @DisplayName("일정 조회 성공")
    @CsvSource(value = {
            "2025, 5",
            "2025,",    // month 생략
            ",5",       // year 생략
            ","         // month, year 생략
    })
    @ParameterizedTest
    void readScheduleByTrainer(String year, String month) throws Exception {
        // given
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("roleType", "trainer");
        params.add("year", year);
        params.add("month", month);

        // when
        ResultActions resultActions = mockMvc.perform(
                get(SCHEDULE_URL).params(params).headers(getHttpHeaders())
        );

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("message").value("회원정보 조회 성공"))
                .andExpect(jsonPath("data[*].trainerId", everyItem(equalTo(1))))
                .andExpect(jsonPath("data[*].startTime", everyItem(startsWith("2025-05"))));
    }
    */

    @DisplayName("로그인 x")
    @Test
    void notLogin() throws Exception {
        // given
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("roleType", "user");
        params.add("year", "2025");
        params.add("month", "5");

        // when
        ResultActions resultActions = mockMvc.perform(
                get(SCHEDULE_URL).params(params)
        );

        // then
        AuthenticationErrorCode errorCode = AuthenticationErrorCode.NOT_AUTHENTICATED;

        resultActions.andExpect(status().isUnauthorized())
                .andExpect(jsonPath("code").value(errorCode.getCode()))
                .andExpect(jsonPath("message").value(errorCode.getMessage()));
    }

    @DisplayName("유효하지 않은 파라미터")
    @CsvSource(value = {
            ",2025,5",      // roleType 생략
            "role,2025,5",  // 유효하지 않는 roleType
            "user,-2025,5", // 유효하지 않는 year
            "user,1899,5", // 유효하지 않는 year
            "user,2036,5", // 유효하지 않는 year
            "user,2025,0",  // 유효하지 않는 month
            "user,2025,13"  // 유효하지 않는 month
    })
    @ParameterizedTest
    void invalidParams(String roleType, String year, String month) throws Exception {
        // given
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("roleType", roleType);
        params.add("year", year);
        params.add("month", month);

        // when
        ResultActions resultActions = mockMvc.perform(
                get(SCHEDULE_URL).params(params).headers(getHttpHeaders())
        );

        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @DisplayName("유효한 파라미터")
    @CsvSource(value = {
            "user,1900,5",
            "trainer,1900,5",
            "user,2035,5",
            "trainer,2035,5",
            "user,2025,1",
            "trainer,2025,1",
            "user,2025,12",
            "trainer,2025,12"
    })
    @ParameterizedTest
    void validParams(String roleType, String year, String month) throws Exception {
        // given
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("roleType", roleType);
        params.add("year", year);
        params.add("month", month);

        // when
        ResultActions resultActions = mockMvc.perform(
                get(SCHEDULE_URL).params(params).headers(getHttpHeaders())
        );

        // then
        resultActions.andExpect(status().isOk());
    }
}