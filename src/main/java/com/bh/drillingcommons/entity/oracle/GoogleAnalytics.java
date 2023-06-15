package com.bh.drillingcommons.entity.oracle;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * GoogleAnalytics Entity Object
 * 
 * @author BH
 */
@Entity
@Table(name = "OG_DRILL_GOOGLE_ANALYTICS_NEW")
@Data
public class GoogleAnalytics {

  @Id
  @Column(name="EVENT_LABEL")
  private String id;

  @Column(name="EVENT_DATE")
  private LocalDate eventDate;

  @Column(name="EVENT_CATEGORY")
  private String eventCategory;

  @Column(name="EVENT_ACTION")
  private String eventAction;

  @Column(name="TOTAL_EVENTS")
  private Integer totalEvents;

}
