package com.zzc.micro.stat;

import com.zzc.micro.stat.core.query.StatQueryService;
import com.zzc.micro.stat.utils.DateTimeUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Administrator
 */
@RestController
@RequestMapping("manage/stat")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StatController {

    private final StatQueryService statQueryService;

    @RequestMapping("query")
    @ResponseBody
    public List<StatQueryResult> query(@RequestParam("ids") List<String> taskIds,
            @RequestParam(value = "startTime", required = false) String startTime,
            @RequestParam(value = "endTime", required = false) String endTime,
            @RequestParam(value = "group", required = false) String group,
            @RequestParam(value = "key", required = false) String key,
            @RequestParam(value = "limit", required = false, defaultValue = "50") long limit) {
        final StatQueryRequest request = StatQueryRequest.builder()
                .ids(taskIds)
                .startTimeInclusive(DateTimeUtils.parse(startTime))
                .endTimeInclusive(DateTimeUtils.parse(endTime))
                .key(key)
                .groups(group)
                .limit(limit)
                .build();
        return statQueryService.query(request);
    }

}
