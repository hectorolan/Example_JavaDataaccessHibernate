package com.olanh.pam_dataaccess;

import java.util.List;

import com.google.gson.Gson;
import com.olanh.olanh_entities.Place;
import com.olanh.olanh_entities.list.Places;
import com.olanh.olanh_entities.status.StatusGet;
import com.olanh.pam_dataaccess.hibernate.DAOPlace;
import com.olanh.pam_dataaccess.util.DAOResponseUtil;
import com.thoughtworks.xstream.XStream;

public class PAMDataAccessMainTest {

	public static void main(String[] args) {
		DAOPlace daoPlace = new DAOPlace();
		DAOResponseUtil<StatusGet, Places> responsePlace = daoPlace.allPlaces();
		//JSON
		Gson gson = new Gson();
		System.out.println(gson.toJson(responsePlace.getResponse()));
		
		//XML
		XStream xstream = new XStream();
	    xstream.alias("Lugar", Place.class);
	    //xstream.alias("persons", PersonList.class);
	    //xstream.addImplicitCollection(PersonList.class, "list");
		System.out.println(xstream.toXML(responsePlace.getResponse()));
		//ArrayList<Place> placesDeserialize = (ArrayList<Place>) xstream.fromXML(xml);

	}

}
