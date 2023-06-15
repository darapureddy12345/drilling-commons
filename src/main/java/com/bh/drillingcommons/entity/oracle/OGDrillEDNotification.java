package com.bh.drillingcommons.entity.oracle;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "OG_DRILL_ED_NOTIFICATION")
@Data
public class OGDrillEDNotification {

    @Id
    @Column(name = "DRILL_NOTIFICATION_ID")
    private Long drillNotificationId;

    @Column(name = "PARENT_ID")
    private String parentId;

    @Column(name = "NOTIFICATION_TYPE")
    private String notificationType;

    @Column(name = "USER_ID")
    private String userId;

    @Column(name = "MARK_AS_READ")
    private String markAsRead;

    @Column(name = "EVENT_TYPE")
    private String eventType;

    @Column(name = "NOTIFICATION_NAME")
    private String notificationName;

    @Column(name = "NOTIFICATION_DESCRIPTION")
    private String notificationDescription;

    @Column(name = "NOTIFICATION_DATE")
    private LocalDate notificationDate;

    @Column(name = "ACTIVE_FLAG")
    private String activeFlag;

    @Column(name = "SUB_PARENT_ID")
    private String subParentId;

    @Column(name = "CRTD_BY")
    private String createdBy;

    @Column(name = "CRTD_DT")
    private LocalDate createdDate;

    @Column(name = "LST_UPDT_BY")
    private String lastUpdatedBy;

    @Column(name = "LST_UPDT_DT")
    private LocalDate lastUpdatedDate;


}
