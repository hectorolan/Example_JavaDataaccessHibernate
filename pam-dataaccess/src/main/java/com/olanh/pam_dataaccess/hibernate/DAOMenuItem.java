package com.olanh.pam_dataaccess.hibernate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.olanh.olanh_entities.MenuItem;
import com.olanh.olanh_entities.Place;
import com.olanh.olanh_entities.list.MenuItems;
import com.olanh.olanh_entities.list.Places;
import com.olanh.olanh_entities.status.StatusAdd;
import com.olanh.olanh_entities.status.StatusDelete;
import com.olanh.olanh_entities.status.StatusGet;
import com.olanh.olanh_entities.status.StatusUpdate;
import com.olanh.pam_dataaccess.util.DAOResponseUtil;
import com.olanh.pam_dataaccess.util.HibernateUtil;

public class DAOMenuItem {
	/*
	 * int Id;int PlaceId;String Name; List<String> Content;double Price;String
	 * Image; String Category;String Remark;String Calories;
	 */

	// CREATE
	public DAOResponseUtil<StatusAdd, MenuItem> addMenuItem(MenuItem menuItem) {
		DAOResponseUtil<StatusAdd, MenuItem> response = new DAOResponseUtil<StatusAdd, MenuItem>();
		Long menuItemID = null;
		try {
			Transaction tx = null;
			Session session = HibernateUtil.getSession();
			try {
				tx = session.beginTransaction();
				menuItemID = (Long) session.save(menuItem);
				if (menuItemID == null)
					response.setStatus(StatusAdd.ALREADY_EXISTS);
				else {
					menuItem.setId(menuItemID);
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
		if (menuItem.getId() == null) {
			menuItem.setId((long) 2);
		}
		return response.setResponse(menuItem);
	}

	// READ
	public DAOResponseUtil<StatusGet, MenuItems> allMenuItems(long placeId) {
		DAOResponseUtil<StatusGet, MenuItems> response = new DAOResponseUtil<StatusGet, MenuItems>();
		MenuItems menuItems = new MenuItems();
		try {
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
			menuItems = this.offlineDBExampleList();
		}
		// This if is temporal while testing/developing
		if (menuItems.size() == 0)
			menuItems = this.offlineDBExampleList();
		return response.setResponse(menuItems);
	}

	// UPDATE
	public DAOResponseUtil<StatusUpdate, MenuItem> updateMenuItem(MenuItem menuItem) {
		DAOResponseUtil<StatusUpdate, MenuItem> response = new DAOResponseUtil<StatusUpdate, MenuItem>();
		MenuItem menuItemFromDB = null;
		try {
			Session session = HibernateUtil.getSession();
			Transaction tx = null;
			try {
				tx = session.beginTransaction();
				menuItemFromDB = (MenuItem) session.get(MenuItem.class, menuItem.getId());
				menuItemFromDB.setCalories(menuItem.getCalories());
				menuItemFromDB.setCategory(menuItem.getCategory());
				menuItemFromDB.setContent(menuItem.getContent().toString());
				menuItemFromDB.setId(menuItem.getId());
				menuItemFromDB.setImage(menuItem.getImage());
				menuItemFromDB.setName(menuItem.getName());
				menuItemFromDB.setPlaceId(menuItem.getPlaceId());
				menuItemFromDB.setPrice(menuItem.getPrice());
				menuItemFromDB.setRemark(menuItem.getRemark());
				// menuItemFromDB.(menuItem);
				session.update(menuItem);
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
		if (menuItemFromDB == null) {
			menuItemFromDB = this.offlineDBExampleObject();
		}
		return response.setResponse(menuItemFromDB);
	}

	// DELETE
	public DAOResponseUtil<StatusDelete, MenuItem> deleteMenuItem(long menuItemID) {
		DAOResponseUtil<StatusDelete, MenuItem> response = new DAOResponseUtil<StatusDelete, MenuItem>();
		try {
			Session session = HibernateUtil.getSession();
			Transaction tx = null;
			try {
				tx = session.beginTransaction();
				MenuItem menuItem = (MenuItem) session.get(MenuItem.class, menuItemID);
				session.delete(menuItem);
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

	// GET
	// not implemented
	public MenuItem getMenuItem(long menuItemId) {
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

	// For TEST and Offline uses
	private MenuItems offlineDBExampleList() {
		MenuItems menuItems = new MenuItems();
		for (int i = 0; i <= 5; i++){
			MenuItem menuItem = offlineDBExampleObject();
			menuItem.setContent("Tomates,Beans,Cheese,Guacamole"+i+"");
			menuItem.addIngrediet("Tomatoe");
			menuItem.addIngrediet("Beans");
			menuItem.addIngrediet("Cheese");
			menuItem.addIngrediet("Guacamole");
			menuItem.setId(i+1);
			menuItems.add(menuItem);
			if (i == 5){
				menuItem.setCategory("Main");
			}
		}
		return menuItems;
	}

	private MenuItem offlineDBExampleObject() {
		MenuItem menuItem = new MenuItem();
		menuItem.setId(1);
		menuItem.setAvailable(true);
		menuItem.setCalories("125");
		menuItem.setContent("['Tomates','Beans','Cheese','Guacamole']");
		menuItem.setId(1);
		menuItem.setImage("");
		menuItem.setName("La Comidita2");
		menuItem.setPlaceId(1);
		menuItem.setPrice(4.98);
		menuItem.setRemark("The chef top pics");
		menuItem.setCategory("Burger");
		return menuItem;
	}

}
