package cleanify_ws.cleanify_ws.controller;
import java.sql.SQLException;
import java.util.ArrayList;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;

import cleanify_ws.cleanify_ws.dbaccess_dao.ShareServiceDAO;
import cleanify_ws.cleanify_ws.dbaccess_dao.WorkerDAO;
import cleanify_ws.cleanify_ws.dto.CategoryDTO;
import cleanify_ws.cleanify_ws.dto.WorkerDTO;


@RestController
public class WorkerController {
	
	@RequestMapping(
			method=RequestMethod.POST,
			path="/registerWorker",
			consumes = "application/json")
	public ResponseEntity<String> registerWorker(@RequestBody WorkerDTO workerDTO) throws SQLException {
		System.out.println("Sending JSON: " + workerDTO.toString());
		
		System.out.println("Sending JSON: " + workerDTO.toString());
		WorkerDAO WorkerDAO = new WorkerDAO();
        boolean isRegistered = WorkerDAO.registerWorker(workerDTO);

        if (isRegistered) {
            return ResponseEntity.ok("Worker registered successfully");
        } else {
            return ResponseEntity.badRequest().body("Worker already exists or invalid data");
        }
    }
}
