package com.task.atipera.controller;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@WireMockTest(httpPort = 8081)
class GitHubRepoControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("github.api-base-url", () -> "http://localhost:8081");
    }

    @Test
    void shouldReturnNonForkRepositoriesWithBranchesForExistingUser() throws Exception {
        // Given
        String username = "testuser";

        stubFor(get(urlEqualTo("/users/" + username + "/repos"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                                [
                                    {
                                        "name": "hello-world",
                                        "owner": {
                                            "login": "testuser"
                                        },
                                        "fork": false
                                    },
                                    {
                                        "name": "spring-boot-demo",
                                        "owner": {
                                            "login": "testuser"
                                        },
                                        "fork": false
                                    },
                                    {
                                        "name": "forked-project",
                                        "owner": {
                                            "login": "testuser"
                                        },
                                        "fork": true
                                    }
                                ]
                                """)));

        stubFor(get(urlEqualTo("/repos/" + username + "/hello-world/branches"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                                [
                                    {
                                        "name": "main",
                                        "commit": {
                                            "sha": "6dcb09b5b57875f334f61aebed695e2e4193db5e"
                                        }
                                    },
                                    {
                                        "name": "feature-branch",
                                        "commit": {
                                            "sha": "a8f9b12c3d4e5f6a7b8c9d0e1f2a3b4c5d6e7f8a"
                                        }
                                    }
                                ]
                                """)));

        stubFor(get(urlEqualTo("/repos/" + username + "/spring-boot-demo/branches"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                                [
                                    {
                                        "name": "master",
                                        "commit": {
                                            "sha": "1a2b3c4d5e6f7a8b9c0d1e2f3a4b5c6d7e8f9a0b"
                                        }
                                    }
                                ]
                                """)));

        // When & Then
        mockMvc.perform(get("/api/users/{username}/repositories", username))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].repositoryName", is("hello-world")))
                .andExpect(jsonPath("$[0].ownerLogin", is("testuser")))
                .andExpect(jsonPath("$[0].branches", hasSize(2)))
                .andExpect(jsonPath("$[0].branches[0].name", is("main")))
                .andExpect(jsonPath("$[0].branches[0].lastCommitSha", is("6dcb09b5b57875f334f61aebed695e2e4193db5e")))
                .andExpect(jsonPath("$[0].branches[1].name", is("feature-branch")))
                .andExpect(jsonPath("$[0].branches[1].lastCommitSha", is("a8f9b12c3d4e5f6a7b8c9d0e1f2a3b4c5d6e7f8a")))

                .andExpect(jsonPath("$[1].repositoryName", is("spring-boot-demo")))
                .andExpect(jsonPath("$[1].ownerLogin", is("testuser")))
                .andExpect(jsonPath("$[1].branches", hasSize(1)))
                .andExpect(jsonPath("$[1].branches[0].name", is("master")))
                .andExpect(jsonPath("$[1].branches[0].lastCommitSha", is("1a2b3c4d5e6f7a8b9c0d1e2f3a4b5c6d7e8f9a0b")));

        verify(0, getRequestedFor(urlEqualTo("/repos/" + username + "/forked-project/branches")));
    }

    @Test
    void shouldReturn404WhenUserDoesNotExist() throws Exception {
        String username = "nonexistent";

        stubFor(get(urlEqualTo("/users/" + username + "/repos"))
                .willReturn(aResponse()
                        .withStatus(404)
                        .withBody("""
                        {
                            "message": "Not Found"
                        }
                    """)));

        mockMvc.perform(get("/api/users/{username}/repositories", username))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.message", containsString("User not found: " + username)));
    }
}