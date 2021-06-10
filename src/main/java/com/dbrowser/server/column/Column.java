package com.dbrowser.server.column;

import java.util.Arrays;
import java.util.Optional;

public class Column {

    private String name;
    private String datatype;
    private ColumnIndexType indexType;
    private String defaultValue;
    private Boolean nullable;
    private String charset;
    private String collation;
    private String comment;
    private String additionalInfo;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public Boolean getNullable() {
        return nullable;
    }

    public void setNullable(Boolean nullable) {
        this.nullable = nullable;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getCollation() {
        return collation;
    }

    public void setCollation(String collation) {
        this.collation = collation;
    }

    public ColumnIndexType getIndexType() {
        return indexType;
    }

    public void setIndexType(ColumnIndexType indexType) {
        this.indexType = indexType;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public String getDatatype() {
        return datatype;
    }

    public void setDatatype(String datatype) {
        this.datatype = datatype;
    }

    enum ColumnIndexType {

        PRIMARY("PRI"),
        UNIQUE("UNI"),
        NON_UNIQUE("MUL");

        private final String dbCode;

        ColumnIndexType(String dbCode) {
            this.dbCode = dbCode;
        }

        static Optional<ColumnIndexType> fromDbCode(String dbCode) {
            return Arrays.stream(ColumnIndexType.values())
                    .filter(index -> index.dbCode.equalsIgnoreCase(dbCode))
                    .findAny();
        }

    }
}
