package com.dbrowser.server.connection;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.StringEndsWith.endsWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@AutoConfigureMockMvc
class ConnectionDetailsControllerTest {

    public static final String API_PATH = "/connection-details";
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ConnectionDetailsService service;

    @Test
    void whenGetConnections_returnAllConnections() throws Exception {
        ConnectionDetails conn1 = new ConnectionDetails(1L, "connection1", "localhost", 3306, "myDb", "user", "pwd");
        ConnectionDetails conn2 = new ConnectionDetails(1L, "connection2", "remote", 3307, "theirDb", "aabbcc", "scrt");
        when(service.getAllConnections())
                .thenReturn(List.of(conn1, conn2));

        mockMvc.perform(get(API_PATH))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)))

                // 1st connection
                .andExpect(jsonPath("$[0].id", is(conn1.getId().intValue())))
                .andExpect(jsonPath("$[0].name", is(conn1.getName())))
                .andExpect(jsonPath("$[0].hostname", is(conn1.getHostname())))
                .andExpect(jsonPath("$[0].port", is(conn1.getPort())))
                .andExpect(jsonPath("$[0].databaseName", is(conn1.getDatabaseName())))
                .andExpect(jsonPath("$[0].username", is(conn1.getUsername())))
                .andExpect(jsonPath("$[0].password", is(conn1.getPassword())))

                // 2nd connection
                .andExpect(jsonPath("$[1].id", is(conn2.getId().intValue())))
                .andExpect(jsonPath("$[1].name", is(conn2.getName())))
                .andExpect(jsonPath("$[1].hostname", is(conn2.getHostname())))
                .andExpect(jsonPath("$[1].port", is(conn2.getPort())))
                .andExpect(jsonPath("$[1].databaseName", is(conn2.getDatabaseName())))
                .andExpect(jsonPath("$[1].username", is(conn2.getUsername())))
                .andExpect(jsonPath("$[1].password", is(conn2.getPassword())));
    }

    @Test
    void whenGetConnection_returnConnectionById() throws Exception {
        ConnectionDetails conn = new ConnectionDetails(1L, "connection1", "localhost", 3306, "myDb", "user", "pwd");
        when(service.getConnection(1L)).thenReturn(conn);

        mockMvc.perform(get(API_PATH + "/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                // 1st connection
                .andExpect(jsonPath("id", is(conn.getId().intValue())))
                .andExpect(jsonPath("name", is(conn.getName())))
                .andExpect(jsonPath("hostname", is(conn.getHostname())))
                .andExpect(jsonPath("port", is(conn.getPort())))
                .andExpect(jsonPath("databaseName", is(conn.getDatabaseName())))
                .andExpect(jsonPath("username", is(conn.getUsername())))
                .andExpect(jsonPath("password", is(conn.getPassword())));
    }

    @Test
    void whenPost_createNewConnection() throws Exception {
        when(service.createConnection(any(ConnectionDetails.class)))
                .thenReturn(new ConnectionDetails(1L, "db_connection", "localhost", 3306, "ourDb", "tstuser", "scrt"));

        ConnectionDetailsDto requestBody = new ConnectionDetailsDto(null, "db_connection", "localhost", 3306, "ourDb", "tstuser", "scrt");
        mockMvc.perform(post(API_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", endsWith(API_PATH + "/1")))
                .andExpect(jsonPath("id").value(is(1)));
    }

    @Test
    void whenPostEmptyName_returnBadRequest() throws Exception {
        String emptyConnectionName = "";
        ConnectionDetailsDto requestBody = new ConnectionDetailsDto(null, emptyConnectionName, "localhost", 3306, "ourDb", "tstuser", "scrt");

        mockMvc.perform(post(API_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenPut_updateConnection() throws Exception {
        ConnectionDetailsDto requestBody = new ConnectionDetailsDto(1L, "db_connection", "localhost", 3306, "ourDb", "tstuser", "scrt");

        mockMvc.perform(put(API_PATH + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isOk());

        ArgumentCaptor<ConnectionDetails> captor = ArgumentCaptor.forClass(ConnectionDetails.class);
        verify(service).updateConnection(eq(1L), captor.capture());

        ConnectionDetails captured = captor.getValue();

        assertThat(captured.getId(), is(requestBody.getId()));
        assertThat(captured.getName(), is(requestBody.getName()));
        assertThat(captured.getHostname(), is(requestBody.getHostname()));
        assertThat(captured.getPort(), is(requestBody.getPort()));
        assertThat(captured.getDatabaseName(), is(requestBody.getDatabaseName()));
        assertThat(captured.getUsername(), is(requestBody.getUsername()));
        assertThat(captured.getPassword(), is(requestBody.getPassword()));
    }

    @Test
    void whenPutEmptyBody_returnBadRequest() throws Exception {
        String emptyRequestBody = "";
        mockMvc.perform(put(API_PATH + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(emptyRequestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenDelete_removeConnection() throws Exception {
        mockMvc.perform(delete(API_PATH + "/1"))
                .andExpect(status().isOk());

        verify(service).deleteConnection(1L);
    }

}
