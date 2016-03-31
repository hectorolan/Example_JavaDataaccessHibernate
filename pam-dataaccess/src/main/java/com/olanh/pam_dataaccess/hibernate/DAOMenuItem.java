package com.olanh.pam_dataaccess.hibernate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.olanh.olanh_entities.MenuItem;
import com.olanh.olanh_entities.Place;
import com.olanh.pam_dataaccess.util.HibernateUtil;

public class DAOMenuItem {
	/*	int Id;int PlaceId;String Name;
	 *  List<String> Content;double Price;String Image;
	 *  String Category;String Remark;String Calories;
	*/
	
	
	//CREATE
	public Integer addMenuItem(MenuItem menuItem) {
		Transaction tx = null;
		Session session = HibernateUtil.getSession();
		Integer menuItemID = null;
		try {
			tx = session.beginTransaction();

			menuItemID = (Integer) session.save(menuItem);
			session.flush();
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return menuItemID;
	}

	//READ
	public ArrayList<MenuItem> allMenuItems() {
		ArrayList<MenuItem> menuItems = new ArrayList<MenuItem>();

		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			List<?> sourceMenuItems = session.createQuery("FROM MENUITEM").list();
			for (Iterator<?> iterator = sourceMenuItems.iterator(); iterator.hasNext();) {
				MenuItem menuItem = (MenuItem) iterator.next();
				menuItems.add(menuItem);
			}
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}

		return menuItems;
	}

	//UPDATE
	public MenuItem updateMenuItem(MenuItem menuItem) {
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		MenuItem menuItemFromDB = null;
		try {
			tx = session.beginTransaction();
			menuItemFromDB = (MenuItem) session.get(MenuItem.class, menuItem.getId());
			menuItemFromDB.setCalories(menuItem.getCalories());
			menuItemFromDB.setCategory(menuItem.getCategory());
			menuItemFromDB.setContent(menuItem.getContent());
			menuItemFromDB.setId(menuItem.getId());
			menuItemFromDB.setImage(menuItem.getImage());
			menuItemFromDB.setName(menuItem.getName());
			menuItemFromDB.setPlaceId(menuItem.getPlaceId());
			menuItemFromDB.setPrice(menuItem.getPrice());
			menuItemFromDB.setRemark(menuItem.getRemark());
			//menuItemFromDB.(menuItem);
			
			session.update(menuItem);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		
		return menuItemFromDB;
	}

	//DELETE
	public boolean deleteMenuItem(long menuItemID) {
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			MenuItem menuItem = (MenuItem) session.get(MenuItem.class, menuItemID);
			session.delete(menuItem);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
			session.close();
			return false;
		} finally {
			session.close();
		}
		return true;
	}

	//GET
	public MenuItem getMenuItem(long menuItemId){
		Session session = HibernateUtil.getSession();
		MenuItem menuItem = null;
		try {
			menuItem = (MenuItem) session.get(MenuItem.class, menuItemId);
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		
		return menuItem;
	}


}
