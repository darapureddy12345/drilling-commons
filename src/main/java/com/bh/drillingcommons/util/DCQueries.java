package com.bh.drillingcommons.util;

public final class DCQueries {
	
	private DCQueries() {	
	}
	
	public static final String QUERY_SERVICE_LOC_EQUIP = "SELECT LOCATIONID, FACILITYNAME, REGION, LOCADDR1, LOCADDR2, LOCADDR3, CITY, STATE, " +
			"COUNTRY, ZIP, FIRSTNAME, LASTNAME, ROLE, TELEPHONENUMBER, MOBILENUMBER, EMAIL, FAX, GROUPCODE, LATITUDE, LONGITUDE, LOCALTIME, HRSOFOP " +
			"FROM " +
			"(select drill_service_loc_id as LOCATIONID, " +
			"facility_name as FACILITYNAME, " +
			"loc_region as REGION, " +
			"loc_addr_1 as LOCADDR1, " +
			"loc_addr_2 as LOCADDR2, " +
			"loc_addr_3 as LOCADDR3, " +
			"loc_city as CITY, " +
			"loc_state as STATE, " +
			"loc_country as COUNTRY, " +
			"loc_zip as ZIP, " +
			"cntct_person_first_nm as FIRSTNAME, " +
			"cntct_person_last_nm as LASTNAME, " +
			"cntct_person_role as ROLE, " +
			"cntct_telephone as TELEPHONENUMBER, " +
			"cntct_mobile as MOBILENUMBER, " +
			"cntct_e_mail as EMAIL, " +
			"cntct_fax as FAX, " +
			"loc_group as GROUPCODE, " +
			"lttud as LATITUDE, " +
			"lngitud as LONGITUDE, " +
			"LOC_TM as localTime, " +
			"HRS_OPRTN as hrsOfOp, " +
			"ROW_NUMBER() OVER(PARTITION BY og_drill_ge_service_loc.lttud,lngitud ORDER BY " +
			"og_drill_ge_service_loc.lttud,lngitud)NM " +
			"from og_drill_ge_service_loc ) OG_DGSL " +
			"WHERE OG_DGSL.NM = 1 " +
			"order by REGION, LOCATIONID";
	
	public static final String QUERY_SERVICE_LOC_DASH = "select drill_service_loc_id as LOCATIONID, " +
			" facility_name as FACILITYNAME, " +
			" loc_region as REGION, " +
			" loc_addr_1 as LOCADDR1, " +
			" loc_addr_2 as LOCADDR2, " +
			" loc_addr_3 as LOCADDR3, " +
			" loc_city as CITY, " +
			" loc_state as STATE, " +
			" loc_country as COUNTRY, " +
			" loc_zip as ZIP, " +
			" cntct_person_first_nm as FIRSTNAME, " +
			" cntct_person_last_nm as LASTNAME, " +
			" cntct_person_role as ROLE, " +
			" cntct_telephone as TELEPHONENUMBER, " +
			" cntct_mobile as MOBILENUMBER, " +
			" cntct_e_mail as EMAIL, " +
			" cntct_fax as FAX, " +
			" loc_group as GROUPCODE, " +
			" lttud as LATITUDE, " +
			" lngitud as LONGITUDE, " +
			" LOC_TM as localTime, " +
			" HRS_OPRTN as hrsOfOp " +
			" from og_drill_ge_service_loc " +
			" order by REGION, LOCATIONID";

	public static final String QUERY_GA_LOOKUP = "select EVENT_CODE, EVENT_CATEGORY, EVENT_ACTION, EVENT_LABEL from OG_DRILL_GOOGLE_ANALYTICS_LKP where ACTV_FLG = ?1";
	
	public static final String QRY_LOOKUP_BY_TYPE = "select LIST_VAL as VALUE, LIST_VAL_DESC as DESCRIPTION, LIST_CD as LOOKUP_TYPE, LIST_VAL_ID as LIST_VAL_ID from OG_DRILL_LIST_VAL_LKP where LIST_CD = ?1 AND ACTV_FLG='Y'";
	
	public static final String QRY_MULTIPLE_LOOKUP_BY_TYPE = "select LIST_VAL as VALUE, LIST_VAL_DESC as DESCRIPTION, LIST_CD as LOOKUP_TYPE, LIST_VAL_ID as LIST_VAL_ID from OG_DRILL_LIST_VAL_LKP where LIST_CD in ";
	
	public static final String QRY_GET_NOTIFICATION_LIST = "SELECT WEB_NOTIFICATION_ID , subject ,NOTIF_DETAIL ,   CASE  WHEN U.PROP_VALUE= 0 THEN 'N' ELSE 'Y' END  AS actv_flg"
    		+ " FROM OG_DRILL_USER_VIEW_PREFERENCE U,OG_DRILL_WEB_NOTIFICATION W WHERE w.Active_flag='A' AND "
                                      +"U.VIEW_OBJECT = TO_CHAR(W.WEB_NOTIFICATION_ID) AND U.PROP_KEY='Notification_Release' AND U.PROP_VALUE = 0  AND lower(U.USER_ID1) = ?1";
									  	  
	public static final String QRY_GET_NOTIFICATION_LIST_ALL = "SELECT WEB_NOTIFICATION_ID ,subject,NOTIF_DETAIL "
    		+ " FROM OG_DRILL_USER_VIEW_PREFERENCE U,OG_DRILL_WEB_NOTIFICATION W WHERE   w.Active_flag='A' AND "
                                      +"U.VIEW_OBJECT = TO_CHAR(W.WEB_NOTIFICATION_ID) AND U.PROP_KEY='Notification_Release' AND lower(U.USER_ID1) = ?1";
	
	public static final String QRY_UPDATE_NOTIFICATION_RELEASE = "UPDATE OG_DRILL_USER_VIEW_PREFERENCE SET PROP_VALUE ='1' WHERE PROP_KEY='Notification_Release' AND lower(USER_ID1) = ?1";
	
	public static final String QRY_UPDATE_NOTIFICATION_RELEASE_SPECIFICREAD = "UPDATE OG_DRILL_USER_VIEW_PREFERENCE SET PROP_VALUE ='1' WHERE PROP_KEY='Notification_Release' AND lower(USER_ID1) = ?1 AND VIEW_OBJECT = ?2";
	
	public static final String GET_REGIONS = "SELECT DRILL_REGION_ID AS regionId, REGION_CODE AS regionCode , REGION_NAME as regionDesc  FROM OG_DRILL_REGION";
	
	public static final String QUERY_UPDATE_NOTIF = "UPDATE OG_DRILL_ED_NOTIFICATION SET MARK_AS_READ = 'Y' WHERE DRILL_NOTIFICATION_ID = ?1";
	
	public static final String UPDATE_NOTIFICATIONS_BY_USER_ID = "UPDATE OG_DRILL_ED_NOTIFICATION SET MARK_AS_READ = 'Y' WHERE USER_ID = ?1";
	
	public static final String GET_ALERT_NOTIFICATIONS = "SELECT a.DRILL_NOTIFICATION_ID, a.NOTIFICATION_TYPE, a.MARK_AS_READ,"
			+ " a.EVENT_TYPE, a.NOTIFICATION_NAME, a.NOTIFICATION_DESCRIPTION, TO_CHAR(a.NOTIFICATION_DATE, 'MM/DD/YY HH:MM:SS') AS  NOTIFICATION_DATE,"
			+ " a.CRTD_BY, TO_CHAR(a.CRTD_DT, 'yyyy-mm-dd hh:mm:ss') AS CRTD_DT, a.PARENT_ID, a.SUB_PARENT_ID, c.rig_name as rigName, d.cust_name as custName"
			+ " FROM OG_DRILL_ED_NOTIFICATION a, OG_DRILL_DOCUMENT b, OG_DRILL_RIG c, OG_DRILL_CUSTOMER d"
			+ " WHERE a.user_id = ?1 AND a.ACTIVE_FLAG = 'Y' AND a.parent_id = b.doc_num(+) AND b.drill_cust_id = d.drill_cust_id(+)"
			+ " AND c.rig_id(+) = b.rig_id ORDER BY a.NOTIFICATION_DATE DESC";
	
	public static final String INSERT_OG_DRILL_SHARE_VIDEO = "INSERT INTO OG_DRILL_SHARED_VIDEO(SHARED_VIDEO_ID, DRILL_VID_ID, RECOMMENDED_BY, RECOMMENDED_TO, CRTD_BY, CRTD_DT, LST_UPDT_BY, LST_UPDT_DT ) VALUES (?1, ?2, ?3, ?4, ?5, ?6, ?7, ?8)";

	public static final String INSERT_OG_DRILL_VIDEO_REQ = "INSERT INTO OG_DRILL_VIDEO_REQ (DRILL_REQ_ID, REQ_FNAME, REQ_LNAME, "
			+ "REQ_USER_ID, REQ_EMAIL, REQ_CATEGORY, DESCRIPTION, REQ_STATUS,CRTD_BY, CRTD_DT, LST_UPDT_BY, LST_UPDT_DT) "
			+ "VALUES (?1, ?2, ?3, ?4, ?5, ?6, ?7, ?8, ?9, ?10, ?11, ?12)";
	
	public static final String GET_BULLETIN_MAILS_WITH_FLEET_LEVEL = " select USR.user_email AS userMailId from og_drill_user USR,"
			+ " og_drill_user_role USRROLE, og_drill_role ROL WHERE USRROLE.drill_user_id = USR.drill_user_id"
			+ " and rol.drill_role_id = USRROLE.drill_role_id and role_code not in ('DCEXP.NON_ED_USER', 'DCEXP.CONSULTANT', 'DCEXP.CONSULTANT') "
			+ " AND USR.BULL_NOTIFY_FLG = 'Y' and USR.user_id not in (select a.user_id from og_drill_user a, og_drill_user_role b, og_drill_role c"
			+ " where a.drill_user_id = b.drill_user_id and b.drill_role_id = c.drill_role_id and c.role_code = 'DCEXP.DRILLING_CONTRACTOR'"
			+ " and b.USER_PERMISSION = 'DCEXP.CUSTOMER.GE_OG') order by USR.user_email";
	
	public static final String GET_BULLETIN_MAILS_WITHOUT_FLEET_LEVEL = " SELECT USR.user_email AS userMailId FROM og_drill_user USR,"
			+ " og_drill_user_role USRROLE, og_drill_role rol WHERE USRROLE.drill_user_id = USR.drill_user_id"
			+ " and rol.drill_role_id = USRROLE.drill_role_id and role_code not in ('DCEXP.NON_ED_USER', 'DCEXP.CONSULTANT', 'DCEXP.GUEST', 'DCEXP.ARF',"
			+ " 'DCEXP.CHANNEL_PARTNER', 'DCEXP.END_USER')" 
			+ " AND USR.bull_notify_flg = 'Y' AND USR.user_id NOT IN(SELECT a.user_id FROM og_drill_user a, og_drill_user_role b, og_drill_role c"
			+ " WHERE a.drill_user_id = b.drill_user_id AND b.drill_role_id = c.drill_role_id AND c.role_code = 'DCEXP.DRILLING_CONTRACTOR'"
			+ " AND b.USER_PERMISSION = 'DCEXP.CUSTOMER.GE_OG') AND usrrole.user_permission IN(SELECT CUST_CODE FROM OG_DRILL_CUSTOMER WHERE" 
			+ " DRILL_CUST_ID IN(SELECT DISTINCT drill_cust_id FROM OG_DRILL_BULLETIN_RIG WHERE bulletin_num = ?1))"
			+ " union"
			+ " SELECT USR.user_email AS userMailId FROM og_drill_user USR, og_drill_user_role USRROLE, og_drill_role rol"
			+ " WHERE USRROLE.drill_user_id = USR.drill_user_id AND rol.drill_role_id = USRROLE.drill_role_id AND role_code"
			+ " IN('DCEXP.SUPER_ADMIN','DCEXP.ADMIN','DCEXP.GE_USER') AND USR.BULL_NOTIFY_FLG = 'Y'";
	
	public static final String DELETE_NOTIFICATION = "delete from og_drill_ed_notification where parent_id = :parentId";
	
	public static final String MAX_NOTIFICATION_ID = "select max(DRILL_NOTIFICATION_ID) from og_drill_ed_notification";
	
	public static final String GET_LOOKUP = "select LIST_VAL as VALUE, LIST_VAL_DESC as DESCRIPTION, LIST_CD as LOOKUP_TYPE, '' as LIST_VAL_ID from OG_DRILL_LIST_VAL_LKP where LIST_CD = ?1"
			+ " AND LIST_VAL = ?2";
		
	public static final String GET_OG_DRILL_DOCUMENT_ID = "select ID from OG_DRILL_DOCUMENT where BULLETIN_NUM = ?1";
	
	public static final String QRY_GET_CUSTOMERS= "SELECT A.DRILL_USER_ID AS DRILL_USER_ID, A.USER_ID AS USER_ID , A.FIRST_NAME ||'' || A.LAST_NAME AS NAME, A.USER_EMAIL AS EMAIL FROM OG_DRILL_USER A, OG_DRILL_ROLE B , OG_DRILL_USER_ROLE C"
            +",OG_DRILL_CUSTOMER D WHERE A.DRILL_USER_ID = C.DRILL_USER_ID AND A.BULL_NOTIFY_FLG='Y' "
            + "AND B.DRILL_ROLE_ID = C.DRILL_ROLE_ID AND C.USER_PERMISSION = D.CUST_CODE AND D.DRILL_CUST_ID IN :userIdList ";
	
	public static final String QRY_GET_GEUSERS ="SELECT A.DRILL_USER_ID AS DRILL_USER_ID,A.USER_ID AS USER_ID, A.FIRST_NAME ||'' || A.LAST_NAME AS NAME, A.USER_EMAIL AS EMAIL FROM OG_DRILL_USER A, OG_DRILL_ROLE B , OG_DRILL_USER_ROLE C "
			+" WHERE A.DRILL_USER_ID = C.DRILL_USER_ID AND A.BULL_NOTIFY_FLG='Y' AND B.DRILL_ROLE_ID = C.DRILL_ROLE_ID "
			+" AND ( (B.ROLE_CODE IN ('DCEXP.ADMIN', 'DCEXP.GE_USER', 'DCEXP.SUPER_ADMIN')) OR (C.USER_PERMISSION = 'DCEXP.CUSTOMER.GE_OG' "
			+ "AND B.ROLE_CODE ='DCEXP.DRILLING_CONTRACTOR') )";
	
	public static final String MAX_WEB_NOTIFICATION_ID = "SELECT MAX(web_notification_id) FROM og_drill_web_notification";
	
	public static final String INSERT_NOTIFICATION_BY_FLEETS = "INSERT INTO og_drill_ed_notification SELECT rownum+1+ :notificationId AS drill_notification_id,:bulletinNum as bulletin_num,'BULLETIN' AS notification_type,USR.user_id,'N' mark_as_read,:normalizedType event_type,:notificationName notification_name,:notificationDescription notification_description,sysdate notification_date,'Y' active_flag,:lastUpdatedBy CRTD_BY ,sysdate CRTD_DT ,:lastUpdatedBy LST_UPDT_BY ,sysdate LST_UPDT_DT ,:revision SUB_PARENT_ID	FROM og_drill_user USR ,og_drill_user_role USRROLE ,og_drill_role ROL WHERE USRROLE.drill_user_id = USR.drill_user_id AND rol.drill_role_id = USRROLE.drill_role_id	AND role_code NOT IN('DCEXP.CONSULTANT', 'DCEXP.GUEST')	AND USR.BULL_NOTIFY_FLG = 'Y' and USRROLE.user_permission in (select distinct b.cust_code from og_drill_bulletin_rig a inner join og_drill_bulletin c on a.bulletin_num = c.bulletin_num inner join og_drill_customer b on a.drill_cust_id = b.drill_cust_id where a.bulletin_num=:bulletinNum	and c.fleet_bulletin='N') AND USR.user_id NOT IN (SELECT a.user_id FROM og_drill_user a,og_drill_user_role b ,og_drill_role c WHERE a.drill_user_id = b.drill_user_id AND b.drill_role_id   = c.drill_role_id AND c.role_code = 'DCEXP.DRILLING_CONTRACTOR'\r\n" + 
			"AND B.USER_PERMISSION = 'DCEXP.CUSTOMER.GE_OG')";
	
	public static final String INSERT_NOTIFICATION = "INSERT INTO og_drill_ed_notification SELECT rownum+1+ :notificationId AS drill_notification_id,:bulletinNum as bulletin_num,'BULLETIN' AS notification_type,USR.user_id,'N' mark_as_read,:normalizedType event_type,:notificationName notification_name,:notificationDescription notification_description, sysdate notification_date,'Y' active_flag,:lastUpdatedBy CRTD_BY ,sysdate CRTD_DT , :lastUpdatedBy LST_UPDT_BY ,sysdate LST_UPDT_DT ,:revision SUB_PARENT_ID FROM og_drill_user USR ,og_drill_user_role USRROLE ,og_drill_role ROL WHERE USRROLE.drill_user_id = USR.drill_user_id AND rol.drill_role_id = USRROLE.drill_role_id AND role_code NOT IN('DCEXP.CONSULTANT','DCEXP.GUEST')	AND USR.BULL_NOTIFY_FLG = 'Y' AND USR.user_id NOT IN (SELECT a.user_id FROM og_drill_user a,og_drill_user_role b ,og_drill_role c WHERE a.drill_user_id = b.drill_user_id AND b.drill_role_id   = c.drill_role_id AND c.role_code = 'DCEXP.DRILLING_CONTRACTOR' AND B.USER_PERMISSION = 'DCEXP.CUSTOMER.GE_OG')";
	
	public static final String INSERT_NOTIFICATION_FLEETS_FALSE = "INSERT INTO og_drill_ed_notification SELECT rownum+1+:nextNotificationId AS drill_notification_id , :bulletinNum bulletin_num ,'BULLETIN' AS notification_type ,USR.user_id ,'N' mark_as_read ,:normalizedType event_type ,:notificationName notification_name ,:bulletinDesc notification_description ,sysdate notification_date ,'Y' active_flag , :lastUpdatedBy CRTD_BY ,sysdate CRTD_DT , :lastUpdatedBy LST_UPDT_BY ,sysdate LST_UPDT_DT ,:revision SUB_PARENT_ID	FROM og_drill_user USR,og_drill_user_role USRROLE,og_drill_role ROL	WHERE USRROLE.drill_user_id = USR.drill_user_id	AND rol.drill_role_id = USRROLE.drill_role_id AND role_code IN ('DCEXP.SUPER_ADMIN','DCEXP.ADMIN','DCEXP.GE_USER') AND USR.BULL_NOTIFY_FLG  = 'Y'";
	
	public static final String GET_RIG_NAME_BY_ID = "select RIG_NAME as RIGNAME from OG_DRILL_RIG where RIG_ID = :rigId";
	
	public static final String UPDATE_NOTIFICATION = "UPDATE OG_DRILL_ED_NOTIFICATION SET MARK_AS_READ = 'Y', ACTIVE_FLAG = 'N' WHERE USER_ID = :userId";
					
	public static final String GET_MCD_MAILS_WITH_FLEET_LEVEL = " select USR.user_email AS userMailId from og_drill_user USR,"
			+ " og_drill_user_role USRROLE, og_drill_role ROL WHERE USRROLE.drill_user_id = USR.drill_user_id"
			+ " and rol.drill_role_id = USRROLE.drill_role_id and role_code not in ('DCEXP.NON_ED_USER', 'DCEXP.CONSULTANT', 'DCEXP.CONSULTANT') ";
			
	public static final String GET_MCD_MAILS_WITH_FLEET_LEVEL2 = " and USR.user_id not in (select a.user_id from og_drill_user a, og_drill_user_role b, og_drill_role c"
			+ " where a.drill_user_id = b.drill_user_id and b.drill_role_id = c.drill_role_id and c.role_code = 'DCEXP.DRILLING_CONTRACTOR'"
			+ " and b.USER_PERMISSION = 'DCEXP.CUSTOMER.GE_OG') order by USR.user_email";
	
	public static final String MARK_FLAG_CONDTN = "AND USR.MARKETING_NOTIFY_FLG = 'Y'";
	
	public static final String COMP_FLAG_CONDTN = "AND USR.COMPLIANCE_NOTIFY_FLG = 'Y'";
	
	public static final String DATA_FLAG_CONDTN = "AND USR.DATASHEET_NOTIFY_FLG = 'Y'";
	
	public static final String GET_OG_DRILL_DOCUMENT_MCD_ID = "SELECT ID from OG_DRILL_DOCUMENT where DRILL_MARK_COMP_ID=?1 AND TYPE=?2";
	
}
