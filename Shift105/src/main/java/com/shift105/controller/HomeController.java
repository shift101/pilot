package com.shift105.controller;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shift105.model.Shift;
import com.shift105.repository.ShiftRepository;

@Controller
public class HomeController {
	
	@Autowired
	private ShiftRepository shifts;
	@Autowired
	private ShiftHelper helper;

	@Value("${welcome.message}")
    private String message;
	
	@GetMapping("/")
    public String main(Model model) {
        model.addAttribute("message", message);

        return "welcome"; //view
    }
	
	@GetMapping("/planned")
    public String planned(Model model) {

        return "planned"; //view
    }
	
	@GetMapping("/actual")
    public String actual(Model model) {

        return "actual"; //view
    }
	
	@GetMapping("/variance")
    public String variance(Model model) {

        return "variance"; //view
    }
	
	@GetMapping("/attendance")
    public String att_variance(Model model) {

        return "att_variance"; //view
    }
	
		
	@GetMapping("/propose")
    public String propose(Model model) {
		Shift shift = new Shift();
		shift.setMonth_name("May");
		shift.setYear(2020);
		model.addAttribute("shift", shift);
        return "propose"; //view
    }
	
	@RequestMapping("/upload")
    public String importAttendance(final RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute("hidden",
				"hidden");
        return "attendance"; //view
    }
	
	@PostMapping("/import")
    public String singleFileUpload(@RequestParam("file") MultipartFile file,final RedirectAttributes redirectAttributes) {
		try {
			XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
		    ;
		    
		    shifts.updateAttendance(helper.mapAttendanceSheet(workbook.getSheetAt(0)));
		} catch (Exception e) {
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("message",
					"Error during uploading " + file.getOriginalFilename() + "!");
			return "redirect:/upload";
		}
		redirectAttributes.addFlashAttribute("message",
				"Successfully uploaded " + file.getOriginalFilename() + "!");
		redirectAttributes.addFlashAttribute("hidden",
				"visible");
		return "redirect:/upload";
	}
}
