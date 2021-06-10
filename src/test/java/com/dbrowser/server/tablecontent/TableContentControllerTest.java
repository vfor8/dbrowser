package com.dbrowser.server.tablecontent;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TableContentController.class)
@AutoConfigureMockMvc
class TableContentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TableContentService service;

    @Test
    void whenGetTableContent_returnDataRows() throws Exception {
        var tableContent = new TableContent();

        List<String> row1 = List.of("111", "value1", "value2");
        tableContent.addRow(row1);

        List<String> row2 = List.of("222", "value3", "value4");
        tableContent.addRow(row2);

        when(service.getTableContent(1, "test_table")).thenReturn(tableContent);

        mockMvc.perform(get("/connections/1/tables/test_table/data-preview"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.rows").isArray())
                .andExpect(jsonPath("$.rows", hasSize(2)))

                // 1st row
                .andExpect(jsonPath("$.rows[0]", hasSize(3)))
                .andExpect(jsonPath("$.rows[0].[0]").value(row1.get(0)))
                .andExpect(jsonPath("$.rows[0].[1]").value(row1.get(1)))
                .andExpect(jsonPath("$.rows[0].[2]").value(row1.get(2)))

                // 2nd row
                .andExpect(jsonPath("$.rows[1]", hasSize(3)))
                .andExpect(jsonPath("$.rows[1].[0]").value(row2.get(0)))
                .andExpect(jsonPath("$.rows[1].[1]").value(row2.get(1)))
                .andExpect(jsonPath("$.rows[1].[2]").value(row2.get(2)));

    }

}
