/*
 * Copyright 2019 - 2020 Blazebit.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.blazebit.domain.runtime.model;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Objects;

/**
 * A positive temporal interval.
 *
 * @author Christian Beikov
 * @since 1.0.0
 */
public class TemporalInterval {

    private final int years;
    private final int months;
    private final int days;
    private final int hours;
    private final int minutes;
    private final int seconds;

    /**
     * Constructs a temporal interval with the given temporal amounts.
     *
     * @param years The years
     * @param months The months
     * @param days The days
     * @param hours The hours
     * @param minutes The minutes
     * @param seconds The seconds
     */
    public TemporalInterval(int years, int months, int days, int hours, int minutes, int seconds) {
        if (years < 0) {
            throw new IllegalArgumentException("Invalid negative years: " + years);
        }
        if (months < 0) {
            throw new IllegalArgumentException("Invalid negative months: " + months);
        }
        if (days < 0) {
            throw new IllegalArgumentException("Invalid negative days: " + days);
        }
        if (hours < 0) {
            throw new IllegalArgumentException("Invalid negative hours: " + hours);
        }
        if (minutes < 0) {
            throw new IllegalArgumentException("Invalid negative minutes: " + minutes);
        }
        if (seconds < 0) {
            throw new IllegalArgumentException("Invalid negative seconds: " + seconds);
        }
        this.years = years;
        this.months = months;
        this.days = days;
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
    }

    /**
     * Returns the years.
     *
     * @return the years
     */
    public int getYears() {
        return years;
    }

    /**
     * Returns the months.
     *
     * @return the months
     */
    public int getMonths() {
        return months;
    }

    /**
     * Returns the days.
     *
     * @return the days
     */
    public int getDays() {
        return days;
    }

    /**
     * Returns the hours.
     *
     * @return the hours
     */
    public int getHours() {
        return hours;
    }

    /**
     * Returns the minutes.
     *
     * @return the minutes
     */
    public int getMinutes() {
        return minutes;
    }

    /**
     * Returns the seconds.
     *
     * @return the seconds
     */
    public int getSeconds() {
        return seconds;
    }

    /**
     * Returns <code>true</code> if all amounts are equal to the amounts of the other interval.
     *
     * @param o The other interval
     * @return <code>true</code> if equal, <code>false</code> otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TemporalInterval interval = (TemporalInterval) o;
        return years == interval.years &&
                months == interval.months &&
                days == interval.days &&
                hours == interval.hours &&
                minutes == interval.minutes &&
                seconds == interval.seconds;
    }

    /**
     * Returns the hash code based on the individual amounts.
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(years, months, days, hours, minutes, seconds);
    }

    /**
     * The format is <code>(Y YEARS )?(m MONTHS )?(d DAYS )?(h HOURS )?(m MINUTES )?s SECONDS</code>.
     *
     * @return the string representation of the interval
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (years != 0) {
            sb.append(years).append(" YEARS ");
        }
        if (months != 0) {
            sb.append(months).append(" MONTHS ");
        }
        if (days != 0) {
            sb.append(days).append(" DAYS ");
        }
        if (hours != 0) {
            sb.append(hours).append(" HOURS ");
        }
        if (minutes != 0) {
            sb.append(minutes).append(" MINUTES ");
        }
        sb.append(seconds).append(" SECONDS");
        return sb.toString();
    }

    /**
     * Adds the given interval to this and returns a new interval.
     *
     * @param interval2 The interval to add
     * @return A new interval representing the sum of this and the other interval
     */
    public TemporalInterval add(TemporalInterval interval2) {
        return new TemporalInterval(
                this.years + interval2.years,
                this.months + interval2.months,
                this.days + interval2.days,
                this.hours + interval2.hours,
                this.minutes + interval2.minutes,
                this.seconds + interval2.seconds
        );
    }

    /**
     * Subtracts the given interval from this and returns a new interval.
     *
     * @param interval2 The interval to subtract
     * @return A new interval representing the sum of this subtracted by the other interval
     */
    public TemporalInterval subtract(TemporalInterval interval2) {
        return new TemporalInterval(
                this.years - interval2.years,
                this.months - interval2.months,
                this.days - interval2.days,
                this.hours - interval2.hours,
                this.minutes - interval2.minutes,
                this.seconds - interval2.seconds
        );
    }

    /**
     * Adds this interval to the given instant producing a new instant.
     *
     * @param instant The instant to which to add this interval.
     * @return A new instant representing the sum of the given instant plus this interval
     */
    public Instant add(Instant instant) {
        return instant.atZone(ZoneOffset.UTC)
                .plusYears(years)
                .plusMonths(months)
                .plusDays(days)
                .plusHours(hours)
                .plusMinutes(minutes)
                .plusSeconds(seconds)
                .toInstant();
    }

    /**
     * Subtract this interval from the given instant producing a new instant.
     *
     * @param instant The instant from which to subtract this interval.
     * @return A new instant representing the sum of the given instant subtracted by this interval
     */
    public Instant subtract(Instant instant) {
        return instant.atZone(ZoneOffset.UTC)
                .minusYears(years)
                .minusMonths(months)
                .minusDays(days)
                .minusHours(hours)
                .minusMinutes(minutes)
                .minusSeconds(seconds)
                .toInstant();
    }
}
