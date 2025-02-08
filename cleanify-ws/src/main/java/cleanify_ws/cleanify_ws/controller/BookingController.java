package cleanify_ws.cleanify_ws.controller;

import java.util.ArrayList;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;


import cleanify_ws.cleanify_ws.dto.CategoryDTO;
import cleanify_ws.cleanify_ws.dbaccess_share_service.ShareServiceDAO;


@RestController
public class BookingController {
	
	//getting all bookings
	@RequestMapping(
			method=RequestMethod.GET,
			path="/getAllBookings")
	public ArrayList<CategoryDTO> getAvailabeBookings() {
		
		ArrayList<CategoryDTO> bookingList = new ArrayList<>();
		try {
			ShareServiceDAO db = new ShareServiceDAO();
			bookingList = db.getAvailableBookings();
		}catch(Exception e) {
			System.out.println(e);
		}
		
		
		return bookingList;
		
	}
	
	
	
	     
}
