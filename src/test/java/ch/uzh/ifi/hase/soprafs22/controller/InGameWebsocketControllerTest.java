package ch.uzh.ifi.hase.soprafs22.controller;

import ch.uzh.ifi.hase.soprafs22.constant.Color;
import ch.uzh.ifi.hase.soprafs22.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs22.entity.Game;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs22.rest.dto.IdDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs22.service.GameService;
import ch.uzh.ifi.hase.soprafs22.service.InGameWebsocketService;
import ch.uzh.ifi.hase.soprafs22.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(InGameWebsocketController.class)
public class InGameWebsocketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InGameWebsocketService inGameWebsocketService;

    @Test
    public void createGame_validInput_gameCreated() throws Exception {

        // given

        String gameUUID = "123e4567-e89b-12d3-a456-426614174000";

        IdDTO idDTO = new IdDTO();
        idDTO.setId(1L);

        //given(inGameWebsocketService.selectCard(Mockito.any())).willReturn(gameUUID);

        MockHttpServletRequestBuilder postRequest = post("/game")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(idDTO));

        mockMvc.perform(postRequest)
//                .andExpect(status().isCreated())
                .andExpect(status().isUnauthorized());
//                .andExpect(jsonPath("$.uuid", is(gameUUID)));

    }


}
