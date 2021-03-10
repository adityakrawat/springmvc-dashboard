package com.nexxus.demo;

import org.springframework.jdbc.core.RowCallbackHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class OffenceCodeGroupDataCallbackHandler implements RowCallbackHandler {
    private List<GroupDataVO> groupDataVOLs;

    public OffenceCodeGroupDataCallbackHandler() {
        groupDataVOLs = new LinkedList<>();
    }

    @Override
    public void processRow(ResultSet resultSet) throws SQLException {
        GroupDataVO groupDataVO = new GroupDataVO();
        groupDataVO.setDate(resultSet.getString("OCCURRED_ON_DATE").substring(0,10));
        groupDataVO.setCount(resultSet.getInt("OFFENCE_COUNT"));
        groupDataVOLs.add(groupDataVO);
    }

    public List<GroupDataVO> getOffenceCodeGroupData() {
        return groupDataVOLs;
    }
}
