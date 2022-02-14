package com.zzc.micro.stat.utils;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

/**
 * @author Administrator
 */
@UtilityClass
@SuppressWarnings({"unused", "WeakerAccess"})
public class DateTimeUtils {

    public final String DEFAULT_DAY_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";

    public final String DEFAULT_DAY_FORMAT2 = "yyyy-MM-dd HH:mm:ss";

    public final String DATE_FORMAT = "yyyy-MM-dd";

    public final String DATE_FORMAT2 = "yyyyMMdd";

    public long daysBetween(@Nonnull final Date fromTime, @Nonnull final Date endTime) {
        Objects.requireNonNull(fromTime, "fromTime");
        Objects.requireNonNull(endTime, "endTime");
        return ChronoUnit.DAYS.between(Objects.requireNonNull(toLocalDate(fromTime)), toLocalDate(endTime));
    }

    public long secondsBetween(@Nonnull final Date fromTime, @Nonnull final Date endTime) {
        Objects.requireNonNull(fromTime, "fromTime");
        Objects.requireNonNull(endTime, "endTime");
        return ChronoUnit.SECONDS.between(Objects.requireNonNull(toLocalDateTime(fromTime)), toLocalDateTime(endTime));
    }

    public long millSecondsBetween(@Nonnull final Date fromTime, @Nonnull final Date endTime) {
        Objects.requireNonNull(fromTime, "fromTime");
        Objects.requireNonNull(endTime, "endTime");
        return ChronoUnit.MILLIS.between(Objects.requireNonNull(toLocalDateTime(fromTime)), toLocalDateTime(endTime));
    }

    public LocalDateTime toLocalDateTime(@Nullable final Date time) {
        return Optional.ofNullable(time).map(t -> t.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()).orElse(null);
    }

    public LocalDateTime toLocalDateTime(@Nullable final LocalDate time) {
        return toLocalDateTime(toDate(time));
    }

    public LocalDateTime toLocalDateTime(@Nullable final Instant time) {
        return Optional.ofNullable(time).map(t -> LocalDateTime.ofInstant(t, ZoneId.systemDefault())).orElse(null);
    }

    public LocalDate toLocalDate(@Nullable final Date time) {
        return Optional.ofNullable(time).map(t -> t.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()).orElse(null);
    }

    public LocalDate toLocalDate(@Nullable final LocalDateTime time) {
        return Optional.ofNullable(time).map(LocalDateTime::toLocalDate).orElse(null);
    }

    public LocalDate toLocalDate(@Nullable final Instant time) {
        return toLocalDate(toLocalDateTime(time));
    }

    public Date toDate(@Nullable final LocalDateTime localDateTime) {
        return Optional.ofNullable(localDateTime).map(t -> Date.from(t.atZone(ZoneId.systemDefault()).toInstant())).orElse(null);
    }

    public Date toDate(@Nullable final LocalDate localDate) {
        return Optional.ofNullable(localDate).map(t -> Date.from(t.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant())).orElse(null);
    }

    @Nonnull
    public Date toDate(@Nonnull final LocalTime localTime, @Nonnull final LocalDate day) {
        return Date.from(localTime.atDate(day).atZone(ZoneId.systemDefault()).toInstant());
    }

    @Nonnull
    public LocalDate yesterday() {
        return LocalDate.now().minusDays(1);
    }

    @Nonnull
    public LocalDate lastMonthStartDay() {
        return toLocalDate(atStartOfMonth(LocalDate.now().minusMonths(1)));
    }

    @Nonnull
    public LocalDate lastMonthEndDay() {
        return toLocalDate(atEndOfMonth(LocalDate.now().minusMonths(1)));
    }

    @Nonnull
    public LocalDateTime lastMonthStartTime() {
        return atStartOfMonth(LocalDate.now().minusMonths(1));
    }

    @Nonnull
    public LocalDateTime lastMonthEndTime() {
        return atEndOfMonth(LocalDate.now().minusMonths(1));
    }

    @Nonnull
    public LocalDateTime atStartOfDay() {
        return atStartOfDay(LocalDate.now());
    }

    @Nonnull
    public LocalDateTime atStartOfDay(@Nonnull final Date date) {
        return atStartOfDay(toLocalDate(date));
    }

    @Nonnull
    public LocalDateTime atStartOfDay(@Nonnull final LocalDate date) {
        return toLocalDateTime(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    @Nonnull
    public LocalDateTime atStartOfDay(@Nonnull final LocalDateTime date) {
        return atStartOfDay(date.toLocalDate());
    }

    @Nonnull
    public LocalDateTime atEndOfDay() {
        return atEndOfDay(LocalDate.now());
    }

    @Nonnull
    public LocalDateTime atEndOfDay(@Nonnull final Date date) {
        return atEndOfDay(toLocalDate(date));
    }

    @Nonnull
    public LocalDateTime atEndOfDay(@Nonnull final LocalDate date) {
        return date.atTime(LocalTime.MAX);
    }

    @Nonnull
    public LocalDateTime atEndOfDay(@Nonnull final LocalDateTime date) {
        return atEndOfDay(date.toLocalDate());
    }

    @Nonnull
    public LocalDateTime atStartOfMonth(@Nonnull final LocalDate date) {
        return toLocalDateTime(date.with(TemporalAdjusters.firstDayOfMonth()));
    }

    @Nonnull
    public LocalDateTime atStartOfMonth(@Nonnull final LocalDateTime date) {
        return atStartOfMonth(date.toLocalDate());
    }

    @Nonnull
    public LocalDateTime atEndOfMonth(@Nonnull final LocalDate date) {
        return atEndOfDay(date.with(TemporalAdjusters.lastDayOfMonth()));
    }

    @Nonnull
    public LocalDateTime atEndOfMonth(@Nonnull final LocalDateTime date) {
        return atEndOfMonth(date.toLocalDate());
    }

    @Nonnull
    public LocalDate plusDays(@Nonnull final LocalDate date, final int days) {
        return date.plusDays(days);
    }

    @Nonnull
    public LocalDate minusDays(@Nonnull final LocalDate date, final int days) {
        return date.minusDays(days);
    }

    @Nonnull
    public LocalDate minusMonths(@Nonnull final LocalDate date, final int months) {
        return date.minusMonths(months);
    }

    public String parse(final Date time, @Nonnull final String format) {
        return parse(toLocalDateTime(time), format);
    }

    public String parse(@Nullable final LocalDateTime time, @Nonnull final String format) {
        return Optional.ofNullable(time).map(t -> t.format(DateTimeFormatter.ofPattern(format))).orElse(null);
    }

    public String parse(@Nullable final LocalTime time, @Nonnull final String format) {
        return Optional.ofNullable(time).map(t -> t.format(DateTimeFormatter.ofPattern(format))).orElse(null);
    }

    public Date parse(@Nullable final String dateStr, @Nonnull final String format) {
        try {
            return StringUtils.isNotBlank(dateStr) ? new SimpleDateFormat(format).parse(dateStr) : null;
        } catch (ParseException e) {
            throw new IllegalArgumentException("时间格式不正确，要求格式: " + format);
        }
    }

    public Date parse(@Nullable final String dateStr) {
        return parse(dateStr, false);
    }

    /**
     * @param dateStr   时间字符串。必传，长度必须至少2位。
     *                  支持格式：
     *                  1、yy格式，如19，表示2019-01-01 00:00:00.000
     *                  2、满足yyyy MM dd HH mm ss SSS顺序及格式的任意时间，中间允许带任意非数字分隔符。
     *                  如: 2019、2019-05-20、2019/05/20 12:30、2019/01/02 10:01:20.000
     * @param endClosed 是否临近结束时间。
     *                  true表示需要取所属时间的最后一刻时间，如 2019-05-20 ==> 2019-05-20 23:59:59.999
     *                  false取开始时间，如 2019-05-20 ==> 2019-05-20 00:00:00.000
     */
    public Date parse(@Nullable final String dateStr, final boolean endClosed) {
        return DateTimeUtils.toDate(parseTime(dateStr, endClosed));
    }

    public LocalDateTime parseTime(@Nullable final String dateStr) {
        return parseTime(dateStr, false);
    }

    /**
     * @param dateStr   时间字符串。必传，长度必须至少2位。
     *                  支持格式：
     *                  1、yy格式，如19，表示2019-01-01 00:00:00.000
     *                  2、满足yyyy MM dd HH mm ss SSS顺序及格式的任意时间，中间允许带任意非数字分隔符。
     *                  如: 2019、2019-05-20、2019/05/20 12:30、2019/01/02 10:01:20.000
     * @param endClosed 是否临近结束时间。
     *                  true表示需要取所属时间的最后一刻时间，如 2019-05-20 ==> 2019-05-20 23:59:59.999
     *                  false取开始时间，如 2019-05-20 ==> 2019-05-20 00:00:00.000
     */
    public LocalDateTime parseTime(@Nullable final String dateStr, final boolean endClosed) {
        if (StringUtils.isBlank(dateStr)) {
            return null;
        }
        String datetimeStrUse = clearNotDigit(dateStr);
        if (2 == StringUtils.length(datetimeStrUse)) { // 年, 19 -> 2019
            datetimeStrUse = "20" + datetimeStrUse;
        }

        final int[] supportLens = {4, 6, 8, 10, 12, 14, 17}; // 年、月、日、时、分、秒、毫秒
        final TemporalUnit[] supportUnits = {ChronoUnit.YEARS, ChronoUnit.MONTHS, ChronoUnit.DAYS, ChronoUnit.HOURS, ChronoUnit.MINUTES, ChronoUnit.SECONDS, ChronoUnit.MILLIS};
        final int len = StringUtils.length(datetimeStrUse);
        if (!ArrayUtils.contains(supportLens, len) && len < 14) {
            throw new DateTimeException("时间格式不正确");
        }
        if (len > 14 && len < 17) {
            datetimeStrUse = StringUtils.rightPad(datetimeStrUse, 17, "0");
        }

        final TemporalUnit timeUnit = supportUnits[ArrayUtils.indexOf(supportLens, StringUtils.length(datetimeStrUse))];
        if (4 == len) { // 年, 2019 -> 20190101
            datetimeStrUse += "0101";
        } else if (6 == len) { // 月, 201901 --> 20190101
            datetimeStrUse += "01";
        }
        datetimeStrUse = StringUtils.rightPad(datetimeStrUse, 17, "0");
        LocalDateTime time = toLocalDateTime(parse(datetimeStrUse, "yyyyMMddHHmmssSSS"));
        if (endClosed && Objects.nonNull(time)) {
            time = time.plus(1, timeUnit).minus(1, ChronoUnit.NANOS);
        }
        return time;
    }

    private String clearNotDigit(String str) {
        final StringBuilder rtnSb = new StringBuilder(str.length());
        for (int i = 0; i < str.length(); i++) {
            if (Character.isDigit(str.charAt(i))) {
                rtnSb.append(str.charAt(i));
            }
        }
        return rtnSb.toString();
    }

    public String humanizeRange(final Date start, final Date end) {
        return humanizeRange(start, end, " 至 ");
    }

    public String humanizeRange(final Date start, final Date end, final String split) {
        return StringUtils.defaultString(humanize(start), "2000-01-01") + split + StringUtils.defaultString(humanize(end), "今");
    }

    public String humanize(final Date time) {
        if (Objects.isNull(time)) {
            return null;
        }
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        String format = "yyyy-MM-dd HH:mm:ss";
        if (calendar.get(Calendar.SECOND) == 0) {
            format = "yyyy-MM-dd HH:mm";
            if (calendar.get(Calendar.MINUTE) == 0) {
                format = "yyyy-MM-dd HH点";
                if (calendar.get(Calendar.HOUR_OF_DAY) == 0) {
                    format = "yyyy-MM-dd";
                }
            }
        }
        return DateTimeUtils.parse(time, format);
    }

    /**
     * 获取指定时间
     */
    public String getDateString(Date aDate, String format) {
        if (aDate != null) {
            SimpleDateFormat df = new SimpleDateFormat(format);
            return df.format(aDate);
        }
        return null;
    }

    /**
     * 指定时间,增加天数.即:2013年11月28日 4天后 => 2013年12月2日
     *
     * @param date 指定时间
     * @param days 增加天数
     */
    public Date addDays(Date date, int days) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_MONTH, days);
        return c.getTime();
    }

    /**
     * 获取指定时间,精确到天(年-月-日)
     *
     * @param aDate 指定时间
     */
    public String getDateOther(Date aDate) {
        SimpleDateFormat df;
        String returnValue = "";

        if (aDate != null) {
            df = new SimpleDateFormat(DATE_FORMAT);
            returnValue = df.format(aDate);
        }

        return (returnValue);
    }

    /**
     * 获取字符串时间转换成时间对象
     */
    public Date getDateFromString(String sDate) {
        Date outDate = null;
        if (StringUtils.isBlank(sDate)) {
            outDate = new Date();
        }

        SimpleDateFormat df = new SimpleDateFormat(DEFAULT_DAY_FORMAT2);
        try {
            outDate = df.parse(sDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return outDate;
    }

    /**
     * 获取小时
     */
    public int getHourOfDate(Date date) {
        if (date == null) {
            return -1;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }
}
