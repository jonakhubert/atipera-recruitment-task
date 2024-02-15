package com.atipera.repoviewer.controller;

import com.atipera.repoviewer.model.RepoResponse;
import com.atipera.repoviewer.service.RepoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RepoController.class)
public class RepoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RepoService repoService;

    @Test
    public void getUserRepositories_ExistingUser_ListOfRepoResponse() throws Exception {
        // given
        var username = "user";
        var repoResponses = List.of(
            new RepoResponse("repo1", "user1", null),
            new RepoResponse("repo2", "user1", null)
        );

        when(repoService.getUserRepositories(username)).thenReturn(repoResponses);

        // then
        mockMvc.perform(get("/api/repository-management/" + username)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].name").value("repo1"))
            .andExpect(jsonPath("$[0].owner_login").value("user1"))
            .andExpect(jsonPath("$[1].name").value("repo2"))
            .andExpect(jsonPath("$[1].owner_login").value("user1"));
    }

    @Test
    public void getUserRepositories_UserWithNoRepositories_EmptyList() throws Exception {
        // given
        var username = "userWithNoRepos";

        when(repoService.getUserRepositories(username)).thenReturn(Collections.emptyList());

        // then
        mockMvc.perform(get("/api/repository-management/" + username)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    public void getUserRepositories_NoAcceptHeader_ApplicationJsonAsDefault() throws Exception {
        // given
        var username = "userWithNoRepos";

        // then
        mockMvc.perform(get("/api/repository-management/" + username))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void getUserRepositories_XmlAcceptHeader_Status406() throws Exception {
        // given
        var username = "user";

        // then
        mockMvc.perform(get("/api/repository-management/" + username)
            .accept(MediaType.APPLICATION_XML))
            .andExpect(status().isNotAcceptable())
            .andExpect(jsonPath("$.message").value("application/xml not acceptable."));
    }
}
