package cleanify_ws.cleanify_ws.controller;

import java.util.ArrayList;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;

import cleanify_ws.cleanify_ws.dbaccess_booking.BookingDTO;
import cleanify_ws.cleanify_ws.dbaccess_booking.BookingDAO;


@RestController
public class BookingController {
	
	//getting all bookings
	@RequestMapping(
			method=RequestMethod.GET,
			path="/getAllBookings")
	public ArrayList<BookingDTO> getAllBookings() {
		
		ArrayList<BookingDTO> bookingList = new ArrayList<>();
		try {
			BookingDAO db = new BookingDAO();
			bookingList = db.getAllBookings();
		}catch(Exception e) {
			System.out.println(e);
		}
		
		
		return bookingList;
		
	}
	
	
	
	
	
	//get booking by worker
	@RequestMapping(
			method=RequestMethod.GET,
			path="/getBookingForWorker/{workerId}")
	public ArrayList<BookingDTO> getBookingByWorkerId(@PathVariable int workerId) {
		
		ArrayList<BookingDTO> bookingList = new ArrayList<>();
		try {
			BookingDAO db = new BookingDAO();
			bookingList = db.getBookingsForWorker(workerId);
		}catch(Exception e) {
			System.out.println(e);
		}
		
		
		return bookingList;
		
	}
	
	
	
	// get booking details
	@RequestMapping(
			method=RequestMethod.GET,
			path="/getAcceptedBookingDetails/{bookingId}/{workerId}")
	public BookingDTO getAcceptedBookingDetail(@PathVariable int bookingId, 
		    @PathVariable int workerId) {
		
		BookingDTO booking = null;
		
		try {
			BookingDAO db = new BookingDAO();
			booking = db.getBookingById(bookingId, workerId);
		}catch(Exception e) {
			System.out.println(e);
		}
		
		
		
		return booking;
		
	}
	
	
	// accepting booking
	@RequestMapping(
		    method = RequestMethod.PUT, 
		    path = "/acceptBooking/{bookingId}/{workerId}")
		public int acceptBooking(@PathVariable int bookingId, @PathVariable int workerId) {
		    int rec = 0;
		    try {
		        BookingDAO db = new BookingDAO();
		        System.out.println("Attempting to accept booking...");
		        rec = db.acceptBooking(bookingId, workerId);
		        System.out.println("Booking accepted successfully.");
		    } catch (Exception e) {
		        System.out.println("Error in accepting booking: " + e);
		    }
		    return rec;
		}

	
	
	
	// get all accepted booking 
	@RequestMapping(
			method=RequestMethod.GET,
			path="/getAllAcceptedBookings/{workerId}")
	public ArrayList<BookingDTO> getAllAcceptedBookings(@PathVariable int workerId) {
		
		ArrayList<BookingDTO> bookingList = new ArrayList<>();
		
		try {
			BookingDAO db = new BookingDAO();
			bookingList = db.getAcceptedBookingsForWorker(workerId);
		}catch(Exception e) {
			System.out.println(e);
		}
		
		
		
		return bookingList;
		
	}
	     
}
