package com.olanh.pam_dataaccess.hibernate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.olanh.olanh_entities.MenuItem;
import com.olanh.olanh_entities.Place;
import com.olanh.olanh_entities.Schedule;
import com.olanh.olanh_entities.list.Places;
import com.olanh.olanh_entities.list.Schedules;
import com.olanh.olanh_entities.status.StatusAdd;
import com.olanh.olanh_entities.status.StatusDelete;
import com.olanh.olanh_entities.status.StatusGet;
import com.olanh.olanh_entities.status.StatusUpdate;
import com.olanh.pam_dataaccess.util.DAOResponseUtil;
import com.olanh.pam_dataaccess.util.HibernateUtil;

public class DAOSchedule {
	/*
	 *  int Id;String MondayOpen;String MondayClose;
	 *  String TuesdayOpen;String TuesdayClose;
	 *  String WednesdayOpen;String WednesdayClose;
	 *  String ThursdayOpen;String ThursdayClose;
	 *  String FridayOpen;String FridayClose;
	 *  String SaturdayOpen;String SaturdayClose;
	 *  String SundayOpen;String SundayClose;
	 */

	//CREATE
	public DAOResponseUtil<StatusAdd, Schedule>  addSchedule(Schedule schedule) {
		DAOResponseUtil<StatusAdd, Schedule> response = new DAOResponseUtil<StatusAdd, Schedule>();
		Long scheduleId = null;
		try {
			Transaction tx = null;
			Session session = HibernateUtil.getSession();
			try {
				tx = session.beginTransaction();
				scheduleId = (Long) session.save(schedule);
				if (scheduleId == null)
					response.setStatus(StatusAdd.ALREADY_EXISTS);
				else{
					schedule.setId(scheduleId);
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
		if (schedule.getId() == null) {
			schedule = this.offlineDBExampleObject();
		}
		return response.setResponse(schedule);
	}

	//READ
	//not implemented
	public DAOResponseUtil<StatusGet, Schedule> allSchedules() {
		ArrayList<Schedule> schedules = new ArrayList<Schedule>();

		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			List<?> sourceSchedules = session.createQuery("FROM SCHEDULE").list();
			for (Iterator<?> iterator = sourceSchedules.iterator(); iterator.hasNext();) {
				Schedule schedule = (Schedule) iterator.next();
				schedules.add(schedule);
			}
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}

		return null;
	}

	//GET
	public DAOResponseUtil<StatusGet, Schedule> getSchedule(long scheduleId){
		DAOResponseUtil<StatusGet, Schedule> response = new DAOResponseUtil<StatusGet, Schedule>();
		Schedule schedule = null;
		try {
			Session session = HibernateUtil.getSession();
			try{
				schedule = (Schedule) session.get(Schedule.class, scheduleId);
				if (schedule.getId() != null)
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
		if (schedule == null)
			schedule = this.offlineDBExampleObject();
		return response.setResponse(schedule);
	}

	//UPDATE
	//public Schedule updateSchedule(Schedule schedule) {
	public DAOResponseUtil<StatusUpdate, Schedule> updateSchedule(Schedule schedule) {
		DAOResponseUtil<StatusUpdate, Schedule> response = new DAOResponseUtil<StatusUpdate, Schedule>();
		Schedule scheduleFromDB = null;
		try {
			Session session = HibernateUtil.getSession();
			Transaction tx = null;
			try {
				tx = session.beginTransaction();
				scheduleFromDB = (Schedule) session.get(Schedule.class, schedule.getId());
				scheduleFromDB.setFridayClose(schedule.getFridayClose());
				scheduleFromDB.setFridayOpen(schedule.getFridayOpen());
				scheduleFromDB.setId(schedule.getId());
				scheduleFromDB.setPlaceId(schedule.getPlaceId());
				scheduleFromDB.setMondayClose(schedule.getMondayClose());
				scheduleFromDB.setMondayOpen(schedule.getMondayOpen());
				scheduleFromDB.setSaturdayClose(schedule.getSaturdayClose());
				scheduleFromDB.setSaturdayOpen(schedule.getSaturdayOpen());
				scheduleFromDB.setSundayClose(schedule.getSundayClose());
				scheduleFromDB.setSundayOpen(schedule.getSundayOpen());
				scheduleFromDB.setThursdayClose(schedule.getThursdayClose());
				scheduleFromDB.setThursdayOpen(schedule.getThursdayOpen());
				scheduleFromDB.setTuesdayClose(schedule.getTuesdayClose());
				scheduleFromDB.setTuesdayOpen(schedule.getTuesdayOpen());
				scheduleFromDB.setWednesdayClose(schedule.getWednesdayClose());
				scheduleFromDB.setWednesdayOpen(schedule.getWednesdayOpen());
				session.update(schedule);
				tx.commit();
				response.setStatus(StatusUpdate.UPDATED);
			} catch (HibernateException e) {
				if (tx != null)
					tx.rollback();
				e.printStackTrace();
				response.setStatus(StatusUpdate.DB_ERROR);
			} finally {
				session.close();
			}
		} catch (Throwable e) {
			e.printStackTrace();
			response.setStatus(StatusUpdate.DB_ERROR);
		}
		if (scheduleFromDB == null){
			scheduleFromDB = this.offlineDBExampleObject();
		}
		return response.setResponse(scheduleFromDB);
	}

	//DELETE
	//public boolean deleteSchedule(long id) {
	public DAOResponseUtil<StatusDelete, Schedule> deleteSchedule(long id) {
		DAOResponseUtil<StatusDelete, Schedule> response = new DAOResponseUtil<StatusDelete, Schedule>();
		try{
			Session session = HibernateUtil.getSession();
			Transaction tx = null;
			try {
				tx = session.beginTransaction();
				Schedule schedule = (Schedule) session.get(Schedule.class, id);
				session.delete(schedule);
				tx.commit();
				response.setStatus(StatusDelete.DELETED);
			} catch (HibernateException e) {
				if (tx != null)
					tx.rollback();
				e.printStackTrace();
				session.close();
				response.setStatus(StatusDelete.DB_ERROR);
			} finally {
				session.close();
			}
		} catch (Throwable e){
			e.printStackTrace();
			response.setStatus(StatusDelete.DB_ERROR);
		}
		return response;
	}

	// For TEST and Offline uses
	private Schedules offlineDBExampleList(){
		return null;
	}
	
	private Schedule offlineDBExampleObject(){
		Schedule schedule = new Schedule(
				"08:00","20:00",
				"","",
				"","",
				"","",
				"","",
				"","",
				"","");
		schedule.setId((long) 1);
		schedule.setPlaceId((long)2);
		return schedule;
	}
}
