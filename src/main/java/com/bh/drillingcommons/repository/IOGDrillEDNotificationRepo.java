package com.bh.drillingcommons.repository;

import com.bh.drillingcommons.entity.oracle.OGDrillEDNotification;
import com.bh.drillingcommons.util.DCQueries;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IOGDrillEDNotificationRepo extends JpaRepository<OGDrillEDNotification, Long> {

	OGDrillEDNotification findByDrillNotificationId(Long notificationId);

	@Modifying(clearAutomatically = true)
	@Query(value = DCQueries.INSERT_NOTIFICATION_BY_FLEETS, nativeQuery = true)
	int addDrillEdNotificationByFleetFalse(@Param("notificationId") Long notificationId,
			@Param("normalizedType") String normalizedType, @Param("notificationName") String notificationName,
			@Param("notificationDescription") String notificationDescription,
		    @Param("revision") String revision, @Param("lastUpdatedBy") String updatedBy, @Param("bulletinNum") String bulletinNumber);

	@Modifying(clearAutomatically = true)
	@Query(value = DCQueries.INSERT_NOTIFICATION, nativeQuery = true)
	int addDrillEdNotification(@Param("notificationId") Long notificationId,
			@Param("normalizedType") String normalizedType, @Param("notificationName") String notificationName,
			@Param("notificationDescription") String notificationDescription,
			@Param("revision") String revision,  @Param("lastUpdatedBy") String updatedBy, @Param("bulletinNum") String bulletinNumber);
	
	
	@Modifying(clearAutomatically = true)
	@Query(value = DCQueries.INSERT_NOTIFICATION_FLEETS_FALSE, nativeQuery = true)
	int addNotificationFleetFalse(@Param("nextNotificationId") Long nextNotificationId, @Param("bulletinNum") String bulletinNum,
			@Param("normalizedType") String normalizedType, @Param("notificationName") String notificationName,
			@Param("bulletinDesc") String bulletinDesc,
			@Param("revision") String revision, @Param("lastUpdatedBy") String updatedBy);
	
	@Modifying(clearAutomatically = true)
	@Query(value = DCQueries.UPDATE_NOTIFICATION, nativeQuery = true)
	int updateNotification(@Param("userId") String userId);
}
