package com.zzc.micro.stat;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author Administrator
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatQueryResult implements Serializable {
    private static final long serialVersionUID = 626470031749783999L;

    private StatTaskIds id;
    @Deprecated
    private String periodTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date periodStartTime;
    private List<StatEntry> entries;

    public StatQueryResult(StatTaskIds id, Date periodTimeDate, List<StatEntry> entries) {
        this.id = id;
        this.periodTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(periodTimeDate);
        this.periodStartTime = periodTimeDate;
        this.entries = entries;
    }
}
