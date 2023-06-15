package com.bh.drillingcommons.models;

import java.time.LocalDate;

import lombok.Data;

@Data
public class GoogleAnalyticsModel {

	private String id;

	private LocalDate eventDate;

	private String eventCategory;

	private String eventAction;

	private Integer totalEvents;

}
