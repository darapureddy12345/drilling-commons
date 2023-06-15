package com.bh.drillingcommons.ras.impl;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.bh.drillingcommons.config.EmailUtil;
import com.bh.drillingcommons.entity.oracle.GALookup;
import com.bh.drillingcommons.entity.oracle.GEServiceLocation;
import com.bh.drillingcommons.entity.oracle.GoogleAnalytics;
import com.bh.drillingcommons.entity.oracle.Lookup;
import com.bh.drillingcommons.enumerators.Constants;
import com.bh.drillingcommons.exceptions.SystemException;
import com.bh.drillingcommons.ras.ICommonResourceAccessor;
import com.bh.drillingcommons.util.DCQueries;

@Component("commonResourceAccessor")
public class CommonResourceAccessor implements ICommonResourceAccessor {

	private static final Logger LOGGER = LoggerFactory.getLogger(CommonResourceAccessor.class);

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	EmailUtil emailUtil;

	@Autowired
	public JavaMailSender emailSender;

	/**
	 * @see {@link ICommonResourceAccessor#getServiceLocations(String)}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<GEServiceLocation> getServiceLocations(String source) {
		List<GEServiceLocation> serviceLocations = new ArrayList<>();
		try {
			if ("equipment".equalsIgnoreCase(source))
				serviceLocations = entityManager
						.createNativeQuery(DCQueries.QUERY_SERVICE_LOC_EQUIP, GEServiceLocation.class).getResultList();
			else if ("dashboard".equalsIgnoreCase(source))
				serviceLocations = entityManager
						.createNativeQuery(DCQueries.QUERY_SERVICE_LOC_DASH, GEServiceLocation.class).getResultList();

			return serviceLocations;
		} catch (Exception e) {
			LOGGER.error("Exception while fetching preferences, Exception : {}", e);
			throw new SystemException(e.getMessage(), e);
		}
	}

	/**
	 * @see {@link ICommonResourceAccessor#getGoogleAnalyticsLkp()}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getRegionList() {
		try {
			Query query = entityManager.createNativeQuery(DCQueries.GET_REGIONS);
			return query.getResultList();
		} catch (Exception e) {
			LOGGER.error("Exception in Get Region List : {} ", e);
			throw new SystemException(e.getMessage(), e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<GALookup> getGoogleAnalyticsLkp() {
		try {
			List<GALookup> gaLookups = new ArrayList<>();
			Query query = entityManager.createNativeQuery(DCQueries.QUERY_GA_LOOKUP);
			query.setParameter(1, 'Y');
			List<Object[]> result = query.getResultList();
			for (Object[] gaLookup : result)
				gaLookups.add(getGALookup(gaLookup));
			return gaLookups;
		} catch (Exception e) {
			LOGGER.error("Exception while fetching ga lookups, Exception : {}", e);
			throw new SystemException(e.getMessage(), e);
		}
	}

	/**
	 * @see {@link ICommonResourceAccessor#getLookupValues(String)}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Lookup> getLookupValues(String lookupType) {
		try {
			Query query = entityManager.createNativeQuery(DCQueries.QRY_LOOKUP_BY_TYPE, Lookup.class);
			query.setParameter(1, lookupType);
			return query.getResultList();
		} catch (Exception e) {
			LOGGER.error("Exception while fetching lookups, Exception : {}", e);
			throw new SystemException(e.getMessage(), e);
		}
	}

	/**
	 * @see {@link ICommonResourceAccessor#getLookupValues(List<String>)}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Lookup> getLookupValues(List<String> lookupTypes) {
		try {
			StringBuilder inClause = new StringBuilder();
			for (int i = 0; i < lookupTypes.size(); i++) {
				inClause.append("?" + (i + 1));
				if (i != lookupTypes.size() - 1) {
					inClause.append(",");
				}
			}
			String lookupQuery = String.format("%s(%s)", DCQueries.QRY_MULTIPLE_LOOKUP_BY_TYPE, inClause.toString());
			Query query = entityManager.createNativeQuery(lookupQuery, Lookup.class);
			for (int i = 0; i < lookupTypes.size(); i++) {
				query.setParameter(i + 1, lookupTypes.get(i));
			}
			return query.getResultList();
		} catch (Exception e) {
			LOGGER.error("Exception while fetching multiple lookups, Exception : {}", e);
			throw new SystemException(e.getMessage(), e);
		}
	}

	/**
	 * @see {@link ICommonResourceAccessor#emailFeedback(String[], String, String,
	 *      InputStream, String, String fileName)}
	 */
	@Override
	public String emailFeedback(String[] toList, String emailContent, String emailSubject, InputStream inputStream,
			String fromList, String fileName) {

		try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
			MimeBodyPart textBodyPart = new MimeBodyPart();
			textBodyPart.setText(emailContent);

			byte[] bytes = outputStream.toByteArray();
			MimeMultipart mimeMultipart = new MimeMultipart();
			mimeMultipart.addBodyPart(textBodyPart);
			if (inputStream != null) {
				bytes = IOUtils.toByteArray(inputStream);
				String fileExt = fileName.substring(fileName.lastIndexOf('.') + 1, fileName.length());
				String mimeType = null;
				Constants mimeTypeEnum = Arrays.stream(Constants.values())
						.filter(e -> e.name().equalsIgnoreCase(fileExt)).findAny().orElse(null);
				if (mimeTypeEnum != null)
					mimeType = mimeTypeEnum.getValue();

				DataSource dataSource = new ByteArrayDataSource(bytes, mimeType);
				MimeBodyPart pdfBodyPart = new MimeBodyPart();
				pdfBodyPart = new MimeBodyPart();
				pdfBodyPart.setDataHandler(new DataHandler(dataSource));
				pdfBodyPart.setFileName(fileName);
				mimeMultipart.addBodyPart(textBodyPart);
				mimeMultipart.addBodyPart(pdfBodyPart);
			}

			InternetAddress iaSender = new InternetAddress(fromList);
			InternetAddress[] addressTo = new InternetAddress[toList.length];
			if (toList.length > 0) {
				for (int i = 0; i < toList.length; i++) {
					addressTo[i] = new InternetAddress(toList[i]);
				}
			}
			MimeMessage mimeMessage = emailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);

			helper.setFrom(iaSender);
			helper.setSubject(emailSubject);
			helper.setTo(addressTo);
			mimeMessage.setContent(mimeMultipart);

			this.emailSender.send(mimeMessage);
			LOGGER.info("Mail triggered Sucessfully");

		} catch (Exception exception) {
			LOGGER.error("Exception while sending email feedback  {}", exception);
			throw new SystemException(exception.getMessage(), exception);
		}
		return "success";
	}

	/**
	 * @see {@link ICommonResourceAccessor#getViewCount(String, Date, Date)}
	 */
	@Override
	public List<GoogleAnalytics> getViewCount(String eventCategory, Date startDate, Date endDate) {
		try {
			CriteriaBuilder builder = entityManager.getCriteriaBuilder();
			CriteriaQuery<GoogleAnalytics> query = builder.createQuery(GoogleAnalytics.class);
			Root<GoogleAnalytics> root = query.from(GoogleAnalytics.class);
			query.select(root).where(builder.equal(root.get("eventCategory"), eventCategory),
					builder.between(root.<LocalDate>get("eventDate"),
							startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
							endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()));
			return entityManager.createQuery(query).getResultList();
		} catch (Exception e) {
			LOGGER.error("Exception while fetching google analytics, Exception : {}, StackTrace : {}", e.getMessage(),
					e);
			throw new SystemException(e.getMessage(), e);
		}
	}

	/**
	 * @see {@link ICommonResourceAccessor#getAllViewCount(String)}
	 */
	@Override
	public List<Object[]> getAllViewCount(String eventCategory) {
		try {
			CriteriaBuilder builder = entityManager.getCriteriaBuilder();
			CriteriaQuery<Object[]> query = builder.createQuery(Object[].class);
			Root<GoogleAnalytics> root = query.from(GoogleAnalytics.class);
			query.multiselect(root.get("id"), builder.sum(root.get("totalEvents"))).groupBy(root.get("id"));
			return entityManager.createQuery(query).getResultList();
		} catch (Exception e) {
			LOGGER.error("Exception while fetching total google analytics count, Exception : {}, StackTrace : {}",
					e.getMessage(), e);
			throw new SystemException(e.getMessage(), e);
		}
	}

	/**
	 * @see {@link ICommonResourceAccessor#getNextSeqNumber(String)}
	 */
	@Override
	public long getNextSeqNumber(String sequenceName) {
		Query query = null;
		StringBuilder nativeQuery = new StringBuilder();
		BigDecimal nxtSequenceNo = null;
		try {
			nativeQuery.append("select ").append(sequenceName).append(".nextval from dual");
			query = entityManager.createNativeQuery(nativeQuery.toString());
			nxtSequenceNo = (BigDecimal) query.getSingleResult();
			if (nxtSequenceNo == null)
				nxtSequenceNo = BigDecimal.valueOf(0);
		} catch (Exception e) {
			LOGGER.error("Exception in CommonResourceAccessor.getNextSeqNumber(), Exception : {}, StackTrace : {}",
					e.getMessage(), e);
			throw new SystemException(e.getMessage(), e);
		}
		return nxtSequenceNo.longValue();
	}

	/**
	 * @see {@link ICommonResourceAccessor#getLookupValues(String, String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Lookup getLookupValues(String lookupType, String lookupValue) {
		Query query = null;
		try {
			query = entityManager.createNativeQuery(DCQueries.GET_LOOKUP, Lookup.class);
			query.setParameter(1, lookupType);
			query.setParameter(2, lookupValue);
			List<Lookup> lookupValues = query.getResultList();
			if (lookupValues.isEmpty())
				return new Lookup();
			return lookupValues.get(0);
		} catch (Exception e) {
			LOGGER.error("Exception while getting lookup by type and value, Exception : {}, StackTrace : {}",
					e.getMessage(), e);
			throw new SystemException(e.getMessage(), e);
		}
	}

	/*************************** Private Methods ***********************/

	/**
	 * Get {@link GALookup} object from {@link Object[]}
	 * 
	 * @param feature {@link Object[]}
	 * 
	 * @return Returns a {@link GALookup}
	 */
	private GALookup getGALookup(Object[] object) {
		GALookup gaLookup = new GALookup();
		gaLookup.setEventCode(object[0] == null ? "" : object[0].toString());
		gaLookup.setEventCategory(object[1] == null ? "" : object[1].toString());
		gaLookup.setEventAction(object[2] == null ? "" : object[2].toString());
		gaLookup.setEventLabel(object[3] == null ? null : object[3].toString());
		return gaLookup;
	}
}
