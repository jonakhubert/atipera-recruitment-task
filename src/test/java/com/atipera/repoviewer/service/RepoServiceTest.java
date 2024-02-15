package com.atipera.repoviewer.service;

import com.atipera.repoviewer.model.RepoResponse;
import com.atipera.repoviewer.repository.IRepoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RepoServiceTest {

    @Mock
    private IRepoRepository repoRepository;

    @InjectMocks
    private RepoService underTest;

    @Test
    public void getUserRepositories_ExistingUser_ListOfRepoResponse() {
        // given
        var username = "user";
        var expected = List.of(
            new RepoResponse("repo1", "user1", null),
            new RepoResponse("repo2", "user1", null)
        );

        when(repoRepository.getUserRepositories(username)).thenReturn(expected);

        // when
        var result = underTest.getUserRepositories(username);

        // then
        assertEquals(expected, result);
        verify(repoRepository, times(1)).getUserRepositories(anyString());
    }

    @Test
    public void getUserRepositories_UserWithNoRepositories_EmptyList() {
        // given
        var username = "user";
        when(repoRepository.getUserRepositories(username)).thenReturn(Collections.emptyList());

        // when
        var result = underTest.getUserRepositories(username);

        // then
        assertEquals(Collections.emptyList(), result);
        verify(repoRepository, times(1)).getUserRepositories(anyString());
    }

    @Test
    public void getUserRepositories_UserWithOnlyForkRepositories_EmptyList() {
        // given
        var username = "forkUser";
        when(repoRepository.getUserRepositories(anyString())).thenReturn(Collections.emptyList());

        // when
        var result = underTest.getUserRepositories(username);

        // then
        assertEquals(Collections.emptyList(), result);
        verify(repoRepository, times(1)).getUserRepositories(anyString());
    }

    @Test
    public void getUserRepositories_NonExistingUser_HttpClientErrorException() {
        // given
        var username = "nonExistingUser";
        when(repoRepository.getUserRepositories(anyString()))
            .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND, "User not found."));

        // then
        assertThrows(HttpClientErrorException.class, () -> underTest.getUserRepositories(username));
        verify(repoRepository, times(1)).getUserRepositories(anyString());
    }
}
