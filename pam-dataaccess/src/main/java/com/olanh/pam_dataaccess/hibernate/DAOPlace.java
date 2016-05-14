package com.olanh.pam_dataaccess.hibernate;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.olanh.olanh_entities.*;
import com.olanh.olanh_entities.list.Places;
import com.olanh.olanh_entities.status.StatusAdd;
import com.olanh.olanh_entities.status.StatusDelete;
import com.olanh.olanh_entities.status.StatusGet;
import com.olanh.olanh_entities.status.StatusUpdate;
import com.olanh.pam_dataaccess.util.DAOResponseUtil;
import com.olanh.pam_dataaccess.util.HibernateUtil;

public class DAOPlace {
	/*
	 * Place - fields: int id, string city, string name, string image, string
	 * category, string type, string phonenumber, int scheduleid, date
	 * datemodified
	 */

	// CREATE
	public DAOResponseUtil<StatusAdd, Place> addPlace(Place place) {
		DAOResponseUtil<StatusAdd, Place> response = new DAOResponseUtil<StatusAdd, Place>();
		Long placeId = null;
		long placeIdOffline = 2;
		try {
			Transaction tx = null;
			Session session = HibernateUtil.getSession();
			try {
				tx = session.beginTransaction();
				placeId = (Long) session.save(place);
				if (placeId == null)
					response.setStatus(StatusAdd.ALREADY_EXISTS);
				else{
					place.setId(placeId);
					response.setStatus(StatusAdd.OK);
				}
				session.flush();
				tx.commit();
			} catch (HibernateException e) {
				e.printStackTrace();
				response.setStatus(StatusAdd.DB_ERROR);
				if (tx != null)
					tx.rollback();
			} finally {
				session.close();
			}
		} catch (Throwable e) {
			e.printStackTrace();
			response.setStatus(StatusAdd.DB_ERROR);
		}
		if (place.getId() == null) {
			place.setId(placeIdOffline);
		}
		return response.setResponse(place);
	}

	// READ
	public DAOResponseUtil<StatusGet, Places> allPlaces() {
		DAOResponseUtil<StatusGet, Places> response = new DAOResponseUtil<StatusGet, Places>();
		Places places = new Places();
		try {
			Session session = HibernateUtil.getSession();
			Transaction tx = null;
			try {
				tx = session.beginTransaction();
				List<?> sourcePlaces = session.createQuery("FROM PLACE").list();
				for (Iterator<?> iterator = sourcePlaces.iterator(); iterator.hasNext();) {
					Place place = (Place) iterator.next();
					places.add(place);
				}
				tx.commit();
				response.setStatus(StatusGet.OK);
			} catch (HibernateException e) {
				e.printStackTrace();
				response.setStatus(StatusGet.DB_ERROR);
				if (tx != null)
					tx.rollback();
			} finally {
				session.close();
			}
		} catch (Throwable e) {
			e.printStackTrace();
			response.setStatus(StatusGet.DB_ERROR);
			places = this.offlineDBExampleList();
		}
		// This if is temporal while testing/developing
		if (places.size() == 0)
			places = this.offlineDBExampleList();
		return response.setResponse(places);
	}

	// GET
	public DAOResponseUtil<StatusGet, Place> getPlace(long placeId) {
		DAOResponseUtil<StatusGet, Place> response = new DAOResponseUtil<StatusGet, Place>();
		Place place = null;
		try {
			Session session = HibernateUtil.getSession();
			try {
				place = (Place) session.get(Place.class, placeId);
				if (place.getName() != null && !place.getName().isEmpty())
					response.setStatus(StatusGet.OK);
				else
					response.setStatus(StatusGet.NOT_FOUND);
			} catch (HibernateException e) {
				e.printStackTrace();
				response.setStatus(StatusGet.DB_ERROR);
			} finally {
				session.close();
			}
		} catch (Throwable e) {
			e.printStackTrace();
			response.setStatus(StatusGet.DB_ERROR);
		}
		if (place == null){
			place = this.offlineDBExampleObject();
			place.setId(placeId);
		}
		return response.setResponse(place);
	}

	// GET Places by City
	public DAOResponseUtil<StatusGet, Places> allPlacesByCity(String city) {
		DAOResponseUtil<StatusGet, Places> response = new DAOResponseUtil<StatusGet, Places>();
		Places places = new Places();
		try {
			Session session = HibernateUtil.getSession();
			Transaction tx = null;
			try {
				tx = session.beginTransaction();
				Query query = session.createQuery("FROM PLACE P WHERE P.CITY = :city");
				query.setParameter("city", city);
				List<?> sourcePlaces = query.list();
				for (Iterator<?> iterator = sourcePlaces.iterator(); iterator.hasNext();) {
					Place place = (Place) iterator.next();
					places.add(place);
				}
				tx.commit();
				if (places.size() > 0) {
					response.setStatus(StatusGet.OK);
				} else {
					response.setStatus(StatusGet.NOT_FOUND);
				}
			} catch (HibernateException e) {
				if (tx != null)
					tx.rollback();
				e.printStackTrace();
				response.setStatus(StatusGet.DB_ERROR);
			} finally {
				session.close();
			}
		} catch (Throwable e) {
			e.printStackTrace();
			response.setStatus(StatusGet.DB_ERROR);
			places = this.offlineDBExampleList();
		}
		// This if is temporal while testing/developing
		if (places.size() == 0)
			places = this.offlineDBExampleList();
		return response.setResponse(places);
	}

	// UPDATE
	public DAOResponseUtil<StatusUpdate, Place> updatePlace(Place place) {
		DAOResponseUtil<StatusUpdate, Place> response = new DAOResponseUtil<StatusUpdate, Place>();
		Place placeFromDB = null;
		try {
			Session session = HibernateUtil.getSession();
			Transaction tx = null;
			try {
				tx = session.beginTransaction();
				placeFromDB = (Place) session.get(Place.class, place.getId());
				placeFromDB.setCategory(place.getCategory());
				placeFromDB.setCity(place.getCity());
				placeFromDB.setImage(place.getImage());
				placeFromDB.setName(place.getName());
				placeFromDB.setPhoneNumber(place.getPhoneNumber());
				placeFromDB.setUserId(place.getUserId());
				placeFromDB.setType(place.getType());
				session.update(placeFromDB);
				tx.commit();
				response.setStatus(StatusUpdate.UPDATED);
			} catch (HibernateException e) {
				e.printStackTrace();
				response.setStatus(StatusUpdate.DB_ERROR);
				if (tx != null)
					tx.rollback();
			} finally {
				session.close();
			}
		} catch (Throwable e) {
			e.printStackTrace();
			response.setStatus(StatusUpdate.DB_ERROR);
		}
		if (placeFromDB == null){
			placeFromDB = this.offlineDBExampleObject();
		}
		return response.setResponse(placeFromDB);
	}

	// DELETE
	public DAOResponseUtil<StatusDelete, Place> deletePlace(long placeId) {
		DAOResponseUtil<StatusDelete, Place> response = new DAOResponseUtil<StatusDelete, Place>();
		try {
			Session session = HibernateUtil.getSession();
			Transaction tx = null;
			try {
				tx = session.beginTransaction();
				Place place = (Place) session.get(Place.class, placeId);
				session.delete(place);
				tx.commit();
				response.setStatus(StatusDelete.DELETED);
			} catch (HibernateException e) {
				e.printStackTrace();
				session.close();
				response.setStatus(StatusDelete.DB_ERROR);
				if (tx != null)
					tx.rollback();
			} finally {
				session.close();
			}
		} catch (Throwable e) {
			e.printStackTrace();
			response.setStatus(StatusDelete.DB_ERROR);
		}
		return response;
	}

	// For TEST and Offline uses
	private Places offlineDBExampleList() {
		Places places = new Places();
		for (int i = 0; i <= 1; i++) {
			String[] name = { "offlineDB", "El Bar" };
			String[] city = { "offlineCity", "San Juan" };
			String[] category = { "offlineCategory", "Barra" };
			String[] type = { "offlineType", "Cafe" };
			String[] phoneNumber = { "offlinePhoneNumber", "787-123-4321" };
			Place place = new Place(name[i], city[i], (long) (i + 1), "", category[i], type[i], phoneNumber[i]);
			place.setId((long) (i + 1));
			places.add(place);
		}
		return places;
	}

	private Place offlineDBExampleObject() {
		Place place = new Place("El Bar", "San Juan", (long) 2, "", "Barra", "Cafe", "787-123-4321");
		place.setId((long) 2);
		return place;
	}

}
