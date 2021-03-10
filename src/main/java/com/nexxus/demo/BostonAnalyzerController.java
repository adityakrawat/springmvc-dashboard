package com.nexxus.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@Controller
public class BostonAnalyzerController {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @RequestMapping()
    public String getBostonAnalyzer(Map<String, Object> model) {
        model.put("offenceCodeGroup", getOffenceCodeGroups());
        return "index";
    }

    @GetMapping("/data")
    public ResponseEntity<List<GroupDataVO>> getBostonAnalyzerData() {
        List<GroupDataVO> groupDataVO = getOffenceCodeGroupsData(null);
        if(groupDataVO.size() > 0)
            return new ResponseEntity<List<GroupDataVO>>(groupDataVO, HttpStatus.OK);
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/data/{id}")
    public ResponseEntity<List<GroupDataVO>> getBostonAnalyzerData(@PathVariable("id") Long id) {
        List<GroupDataVO> groupDataVO = getOffenceCodeGroupsData(id);
        if(groupDataVO.size() > 0)
            return new ResponseEntity<List<GroupDataVO>>(groupDataVO, HttpStatus.OK);
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    private Map<Integer, String> getOffenceCodeGroups() {
        OffenceCodeGroupCallbackHandler offenceCodeGroupCallbackHandler = new OffenceCodeGroupCallbackHandler();
        jdbcTemplate.query("SELECT DISTINCT OFFENSE_CODE, " +
                        "OFFENSE_DESCRIPTION FROM" +
                        " CRIMES_IN_BOSTON WHERE " +
                        "OFFENSE_DESCRIPTION IS NOT NULL ORDER BY OFFENSE_DESCRIPTION",
                offenceCodeGroupCallbackHandler
        );
        return offenceCodeGroupCallbackHandler.getOffenceCodeGroupMap();
    }

    private List<GroupDataVO> getOffenceCodeGroupsData(Long codeGroupId) {
        StringBuilder queryBuilder = new StringBuilder("SELECT OFFENCE_COUNT, VARCHAR_FORMAT(OCCURRED_ON_DATE, 'MM-DD-YYYY') AS OCCURRED_ON_DATE FROM (SELECT COUNT(*) AS OFFENCE_COUNT, TO_DATE(OCCURRED_ON_DATE, 'MM-DD-YYYY') AS OCCURRED_ON_DATE FROM (SELECT VARCHAR_FORMAT(OCCURRED_ON_DATE, 'MM-DD-YYYY') AS OCCURRED_ON_DATE FROM CRIMES_IN_BOSTON");
        if(codeGroupId != null) {
            queryBuilder.append(" WHERE OFFENSE_CODE=").append(codeGroupId.toString());
        }
        queryBuilder.append(") GROUP BY OCCURRED_ON_DATE ORDER BY OCCURRED_ON_DATE)");
        OffenceCodeGroupDataCallbackHandler offenceCodeGroupDataCallbackHandler =
                new OffenceCodeGroupDataCallbackHandler();
        jdbcTemplate.query(queryBuilder.toString(),
                offenceCodeGroupDataCallbackHandler
        );
        return offenceCodeGroupDataCallbackHandler.getOffenceCodeGroupData();
    }
}
