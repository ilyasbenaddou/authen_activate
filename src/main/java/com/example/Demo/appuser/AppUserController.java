package com.example.Demo.appuser;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/v1/user")


public class AppUserController {

	private AppUserService appUserService;

	@GetMapping("/activate/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<AppUser> activateUser(@PathVariable Long id) {
		return ResponseEntity.ok().body(appUserService.activateUser(id));
	}
//	@GetMapping("/desactivate/{id}")
//	@PreAuthorize("hasRole('ADMIN')")
//	public ResponseEntity<AppUser> desactivateUser(@PathVariable Long id) {
//		return ResponseEntity.ok().body(appUserService.desactivateUser(id));
//	}

}
