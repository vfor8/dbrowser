package com.dbrowser.server.column;

import org.hamcrest.core.IsNull;
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

@WebMvcTest(controllers = ColumnController.class)
@AutoConfigureMockMvc
class ColumnControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ColumnService service;

    @Test
    void whenGetColumns_returnAllColumns() throws Exception {
        var c1 = new Column();
        c1.setName("column1");
        c1.setIndexType(null);
        c1.setDatatype("varchar(11)");
        c1.setDefaultValue("def");
        c1.setNullable(true);
        c1.setCharset("utf-8");
        c1.setCollation("utf8_general_ci");
        c1.setComment("Column comment");
        c1.setAdditionalInfo("");

        var c2 = new Column();
        c2.setName("id");
        c2.setIndexType(Column.ColumnIndexType.PRIMARY);
        c2.setDatatype("int(11)");
        c2.setDefaultValue(null);
        c2.setNullable(false);
        c2.setCharset(null);
        c2.setCollation(null);
        c2.setComment("Comment");
        c2.setAdditionalInfo("auto_increment");

        when(service.getColumns(1, "test_table")).thenReturn(List.of(c1, c2));

        mockMvc.perform(get("/connections/1/tables/test_table/columns"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)))

                // 1st column
                .andExpect(jsonPath("$[0].name", is(c1.getName())))
                .andExpect(jsonPath("$[0].indexType").value(IsNull.nullValue()))
                .andExpect(jsonPath("$[0].datatype", is(c1.getDatatype())))
                .andExpect(jsonPath("$[0].defaultValue", is(c1.getDefaultValue())))
                .andExpect(jsonPath("$[0].nullable", is(c1.getNullable())))
                .andExpect(jsonPath("$[0].charset", is(c1.getCharset())))
                .andExpect(jsonPath("$[0].collation", is(c1.getCollation())))
                .andExpect(jsonPath("$[0].comment", is(c1.getComment())))
                .andExpect(jsonPath("$[0].additionalInfo", is(c1.getAdditionalInfo())))

                // 2nd column
                .andExpect(jsonPath("$[1].name", is(c2.getName())))
                .andExpect(jsonPath("$[1].indexType", is(c2.getIndexType().toString())))
                .andExpect(jsonPath("$[1].datatype", is(c2.getDatatype())))
                .andExpect(jsonPath("$[1].defaultValue", is(c2.getDefaultValue())))
                .andExpect(jsonPath("$[1].nullable", is(c2.getNullable())))
                .andExpect(jsonPath("$[1].charset", is(c2.getCharset())))
                .andExpect(jsonPath("$[1].collation", is(c2.getCollation())))
                .andExpect(jsonPath("$[1].comment", is(c2.getComment())))
                .andExpect(jsonPath("$[1].additionalInfo", is(c2.getAdditionalInfo())));
    }

    @Test
    void whenGetOneColumnByName_returnSingleColumn() throws Exception {
        var column = new Column();
        column.setName("column1");
        column.setIndexType(Column.ColumnIndexType.UNIQUE);
        column.setDatatype("varchar(11)");
        column.setDefaultValue(null);
        column.setNullable(false);
        column.setCharset("utf-8");
        column.setCollation("utf8_general_ci");
        column.setComment(null);
        column.setAdditionalInfo("");

        when(service.getOneColumn(1, "test_table", "column1")).thenReturn(column);

        mockMvc.perform(get("/connections/1/tables/test_table/columns/column1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("name", is(column.getName())))
                .andExpect(jsonPath("indexType", is(column.getIndexType().toString())))
                .andExpect(jsonPath("datatype", is(column.getDatatype())))
                .andExpect(jsonPath("defaultValue", is(column.getDefaultValue())))
                .andExpect(jsonPath("nullable", is(column.getNullable())))
                .andExpect(jsonPath("charset", is(column.getCharset())))
                .andExpect(jsonPath("collation", is(column.getCollation())))
                .andExpect(jsonPath("comment", is(column.getComment())))
                .andExpect(jsonPath("additionalInfo", is(column.getAdditionalInfo())));
    }

}
