package com.ponyz.stellar.web.api;

import com.ponyz.stellar.domain.admin.Repository.AdminRepository;
import com.ponyz.stellar.domain.admin.entity.Admin;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class UserController {

    private final AdminRepository adminRepository;

    @GetMapping("/hello")
    public String index() {
        String res = "Hello, World!";
        return res;
    }

    @GetMapping("/auth")
    public ResponseEntity<Boolean> getAuth(@RequestParam("id") String id, @RequestParam("password") String password) {
        Optional<Admin> isAuth = adminRepository.findByIdAndPassword(id, password);
        return ResponseEntity.ok().body(isAuth.isPresent());
    }
}
