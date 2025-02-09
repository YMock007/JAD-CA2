package cleanify_ws.cleanify_ws.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;

import cleanify_ws.cleanify_ws.dbaccess_dao.ShareServiceDAO;
import cleanify_ws.cleanify_ws.dbaccess_dao.WorkerDAO;
import cleanify_ws.cleanify_ws.dbaccess_dao.UpdateBookingDAO;
import cleanify_ws.cleanify_ws.dto.CategoryDTO;
import cleanify_ws.cleanify_ws.dto.CompletedBookingDTO;
import cleanify_ws.cleanify_ws.dto.UpdateBookingDTO;
import cleanify_ws.cleanify_ws.dto.WorkerDTO;


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
	
	
	@RequestMapping(
	        method = RequestMethod.PUT,
	        path = "/acceptBooking",
	        consumes = "application/json")
	public ResponseEntity<Map<String, Object>> acceptBooking(@RequestBody UpdateBookingDTO updateBookingDTO) throws SQLException {
	    System.out.println("Received JSON: " + updateBookingDTO.toString());
	    System.out.println(updateBookingDTO.getBookingId());
	    System.out.println(updateBookingDTO.getProviderId());
	    System.out.println(updateBookingDTO.getStatusId());
	    
	    
	    
	    boolean acceptSuccess = false;
	    Map<String, Object> response = new HashMap<>();
	    
	    try {
	        UpdateBookingDAO db = new UpdateBookingDAO();
	        acceptSuccess = db.updateBooking(updateBookingDTO.getBookingId(), updateBookingDTO.getProviderId(), updateBookingDTO.getStatusId());

	        if (acceptSuccess) {
	            response.put("success", true);
	            response.put("message", "Booking updated successfully");
	            response.put("bookingId", updateBookingDTO.getBookingId());
	            response.put("providerId", updateBookingDTO.getProviderId());
	            response.put("statusId", updateBookingDTO.getStatusId());

	            return ResponseEntity.ok(response);
	        } else {
	            response.put("success", false);
	            response.put("message", "Booking not found or already accepted by another worker");

	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	        }
	    } catch (Exception e) {
	        System.out.println("Error updating booking: " + e.getMessage());
	        response.put("success", false);
	        response.put("message", "Internal server error");

	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	    }
	}

	
	@RequestMapping(
		    method = RequestMethod.GET,
		    path = "/getAcceptedBookings/{workerId}"
		)
		public ArrayList<CategoryDTO> getAcceptedBookings(@PathVariable int workerId) {
		    ArrayList<CategoryDTO> bookingList = new ArrayList<>();
		    try {
		        ShareServiceDAO db = new ShareServiceDAO();
		        bookingList = db.getAcceptedBookings(workerId);
		    } catch (Exception e) {
		        System.out.println(e);
		    }
		    return bookingList;
		}

	
	
	@RequestMapping(
	        method = RequestMethod.PUT,
	        path = "/completeBooking",
	        consumes = "application/json")
	public ResponseEntity<Map<String, Object>> completeBooking(@RequestBody UpdateBookingDTO updateBookingDTO) throws SQLException {
	    
	    
	    
	    
	    boolean acceptSuccess = false;
	    Map<String, Object> response = new HashMap<>();
	    
	    try {
	        UpdateBookingDAO db = new UpdateBookingDAO();
	        System.out.println("in web service");
	        System.out.println(updateBookingDTO.getBookingId());
	        System.out.println(updateBookingDTO.getProviderId());
	        System.out.println(updateBookingDTO.getStatusId());
	        acceptSuccess = db.completeBooking(updateBookingDTO.getBookingId(), updateBookingDTO.getProviderId(), updateBookingDTO.getStatusId());

	        if (acceptSuccess) {
	            response.put("success", true);
	            response.put("message", "Booking updated successfully");
	            response.put("bookingId", updateBookingDTO.getBookingId());
	            response.put("providerId", updateBookingDTO.getProviderId());
	            response.put("statusId", updateBookingDTO.getStatusId());

	            return ResponseEntity.ok(response);
	        } else {
	            response.put("success", false);
	            response.put("message", "Booking not found or already accepted by another worker");

	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	        }
	    } catch (Exception e) {
	        System.out.println("Error updating booking: " + e.getMessage());
	        response.put("success", false);
	        response.put("message", "Internal server error");

	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	    }
	}
	
	@RequestMapping(
		    method = RequestMethod.GET,
		    path = "/getCompletedBookings/{workerId}"
		)
		public ArrayList<CompletedBookingDTO> getCompletedBookings(@PathVariable int workerId) {
		    ArrayList<CompletedBookingDTO> bookingList = new ArrayList<>();
		    try {
		        ShareServiceDAO db = new ShareServiceDAO();
		        bookingList = db.getCompletedBookings(workerId); // Call DAO method to fetch completed bookings
		    } catch (Exception e) {
		        System.out.println("Error fetching completed bookings: " + e.getMessage());
		    }
		    return bookingList;
		}

	
	
	     
}
