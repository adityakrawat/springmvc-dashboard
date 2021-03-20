package com.demo.app;

import org.springframework.jdbc.core.RowCallbackHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

public class OffenceCodeGroupCallbackHandler implements RowCallbackHandler {

    private Map<Integer, String> offenceCodeGroupMap;

    public OffenceCodeGroupCallbackHandler() {
        offenceCodeGroupMap = new LinkedHashMap<>();
    }

    @Override
    public void processRow(ResultSet resultSet) throws SQLException {
            offenceCodeGroupMap.put(resultSet.getInt("OFFENSE_CODE"), resultSet.getString("OFFENSE_DESCRIPTION"));
    }

    public Map<Integer, String> getOffenceCodeGroupMap() {
        return offenceCodeGroupMap;
    }

}
