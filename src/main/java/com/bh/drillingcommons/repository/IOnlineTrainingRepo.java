package com.bh.drillingcommons.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.bh.drillingcommons.entity.oracle.OnlineTraining;

@Repository
public interface IOnlineTrainingRepo extends JpaRepository<OnlineTraining, Long> {

	@Query(nativeQuery = true, value = "SELECT * FROM OG_DRILL_ONLINE_TRAINING_MST where ISACTIVE='true'  order by PUBLISH_DATE desc")
	List<OnlineTraining> getOnlineTraining();

}
