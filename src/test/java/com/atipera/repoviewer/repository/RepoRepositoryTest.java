package com.atipera.repoviewer.repository;

import com.atipera.repoviewer.model.BranchResponse;
import com.atipera.repoviewer.model.RepoResponse;
import com.atipera.repoviewer.model.api.Branch;
import com.atipera.repoviewer.model.api.Commit;
import com.atipera.repoviewer.model.api.Owner;
import com.atipera.repoviewer.model.api.Repo;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest
public class RepoRepositoryTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private RepoRepository underTest;

    @Test
    public void getUserRepositories_ExistingUser_ListOfRepositories() {
        // given
        String username = "user";
        Repo[] repositories = {
            new Repo("repo1", new Owner("user1"), false),
            new Repo("repo2", new Owner("user2"), true)
        };

        Branch[] branches = {
            new Branch("branch1", new Commit("sha1")),
            new Branch("branch2", new Commit("sha2"))
        };

        List<RepoResponse> expected = List.of(
            new RepoResponse("repo1", "user1", List.of(
                new BranchResponse("branch1", "sha1"),
                new BranchResponse("branch2", "sha2")
            ))
        );

        when(restTemplate.getForObject(anyString(), eq(Repo[].class))).thenReturn(repositories);
        when(restTemplate.getForObject(anyString(), eq(Branch[].class))).thenReturn(branches);

        // when
        List<RepoResponse> result = underTest.getUserRepositories(username);

        // then
        assertEquals(1, result.size());
        assertEquals(expected, result);
        verify(restTemplate, times(1)).getForObject(anyString(), eq(Repo[].class));
        verify(restTemplate, times(1)).getForObject(anyString(), eq(Branch[].class));
    }

    @Test
    public void getUserRepositories_UserWithNoRepositories_EmptyList() {
        String username = "noReposUser";
        // given
        when(restTemplate.getForObject(anyString(), eq(Repo[].class))).thenReturn(new Repo[]{});

        // when
        List<RepoResponse> result = underTest.getUserRepositories(username);

        // then
        assertEquals(0, result.size());
        assertEquals(Collections.emptyList(), result);
        verify(restTemplate, times(1)).getForObject(anyString(), eq(Repo[].class));
    }

    @Test
    public void getUserRepositories_UserWithOnlyForkRepositories_EmptyList() {
        // given
        String username = "forkUser";
        Repo[] repositories = {
            new Repo("repo1", new Owner("user1"), true),
            new Repo("repo2", new Owner("user2"), true)
        };

        when(restTemplate.getForObject(anyString(), eq(Repo[].class))).thenReturn(repositories);

        // when
        List<RepoResponse> result = underTest.getUserRepositories(username);

        // then
        assertEquals(0, result.size());
        assertEquals(Collections.emptyList(), result);
        verify(restTemplate, times(1)).getForObject(anyString(), eq(Repo[].class));
    }

    @Test
    public void getUserRepositories_NonExistingUser_HttpClientErrorException() {
        // given
        String username = "nonExistingUser";
        when(restTemplate.getForObject(anyString(), eq(Repo[].class)))
            .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND, "User not found."));

        // then
        assertThrows(HttpClientErrorException.class, () -> underTest.getUserRepositories(username));
        verify(restTemplate, times(1)).getForObject(anyString(), eq(Repo[].class));
    }

    @Test
    public void getBranches_ExistingUserAndRepo_Branches() {
        // given
        String username = "user";
        String repoName = "repo";
        Branch[] branches = {
            new Branch("branch1", new Commit("sha1")),
            new Branch("branch2", new Commit("sha2"))
        };

        List<BranchResponse> expected = List.of(
            new BranchResponse("branch1", "sha1"),
            new BranchResponse("branch2", "sha2")
        );

        when(restTemplate.getForObject(anyString(), eq(Branch[].class))).thenReturn(branches);

        // when
        List<BranchResponse> result = underTest.getBranches(username, repoName);

        // then
        assertEquals(2, result.size());
        assertEquals(expected, result);
        verify(restTemplate, times(1)).getForObject(anyString(), eq(Branch[].class));
    }

    @Test
    public void getBranches_RepoWithNoBranches_EmptyBranchList() {
        // given
        String username = "user";
        String repoName = "repo";

        Branch[] branches = {};
        when(restTemplate.getForObject(anyString(), eq(Branch[].class))).thenReturn(branches);

        // when
        List<BranchResponse> result = underTest.getBranches(username, repoName);

        // then
        assertEquals(0, result.size());
        assertEquals(Collections.emptyList(), result);
        verify(restTemplate, times(1)).getForObject(anyString(), eq(Branch[].class));
    }
}