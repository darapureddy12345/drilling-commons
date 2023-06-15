package com.bh.drillingcommons.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bh.drillingcommons.entity.oracle.LookupValue;

@Repository
public interface ILookupValueRepo extends JpaRepository<LookupValue, String> {

	public List<LookupValue> findByTypeIn(List<String> lookups);
	
	public List<LookupValue> findByType(String lookupType);
	
	public List<LookupValue> findByTypeAndActiveFlag(String lookupType, String activeFlag);
}
