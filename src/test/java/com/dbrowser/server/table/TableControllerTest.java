package com.dbrowser.server.table;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TableController.class)
@AutoConfigureMockMvc
class TableControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TableService service;

    @Test
    void whenGetTables_returnAllTables() throws Exception {
        Table t1 = new Table();
        t1.setName("test_table");
        t1.setType("BASE TABLE");
        t1.setComment("Table comment");
        t1.setEngine("InnoDB");
        t1.setCollation("utf8_general_ci");

        Table t2 = new Table();
        t2.setName("test_table2");
        t2.setType("BASE TABLE");
        t2.setComment("A different comment");
        t2.setEngine("MyISAM");
        t2.setCollation("latin1_swedish_ci");

        when(service.getTables(1)).thenReturn(List.of(t1, t2));

        mockMvc.perform(get("/connections/1/tables/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)))

                // 1st table
                .andExpect(jsonPath("$[0].name", is(t1.getName())))
                .andExpect(jsonPath("$[0].type", is(t1.getType())))
                .andExpect(jsonPath("$[0].comment", is(t1.getComment())))
                .andExpect(jsonPath("$[0].engine", is(t1.getEngine())))
                .andExpect(jsonPath("$[0].collation", is(t1.getCollation())))

                // 2nd table
                .andExpect(jsonPath("$[1].name", is(t2.getName())))
                .andExpect(jsonPath("$[1].type", is(t2.getType())))
                .andExpect(jsonPath("$[1].comment", is(t2.getComment())))
                .andExpect(jsonPath("$[1].engine", is(t2.getEngine())))
                .andExpect(jsonPath("$[1].collation", is(t2.getCollation())));
    }

    @Test
    void whenGetOneTableByName_returnSingleTable() throws Exception {
        Table table = new Table();
        table.setName("test_table");
        table.setType("BASE TABLE");
        table.setComment("Table comment");
        table.setEngine("InnoDB");
        table.setCollation("utf8_general_ci");

        when(service.getOneTable(1, "test_table")).thenReturn(table);

        mockMvc.perform(get("/connections/1/tables/test_table"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("name", is(table.getName())))
                .andExpect(jsonPath("type", is(table.getType())))
                .andExpect(jsonPath("comment", is(table.getComment())))
                .andExpect(jsonPath("engine", is(table.getEngine())))
                .andExpect(jsonPath("collation", is(table.getCollation())));
    }
}
