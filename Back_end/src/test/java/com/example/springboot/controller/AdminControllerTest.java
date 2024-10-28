package com.example.springboot.controller;

import com.example.springboot.model.Admin;
import com.example.springboot.model.User;
import com.example.springboot.service.AdminService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.is;

@WebMvcTest(AdminController.class)
@AutoConfigureMockMvc(addFilters = false)
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminService adminService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateAdmin() throws Exception {
        Admin admin = new Admin();
        admin.setAdminId(1L);
        User user = new User();
        user.setUsername("adminUser");
        admin.setUser(user);

        when(adminService.createAdmin("adminUser", "password")).thenReturn(admin);

        mockMvc.perform(post("/admins/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"adminUser\", \"password\": \"password\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message", is("admin created successfully")))
                .andExpect(jsonPath("$.admin_id", is("1")));

        verify(adminService, times(1)).createAdmin("adminUser", "password");
    }

    @Test
    @WithMockUser
    void testGetAdminById_whenAdminExists() throws Exception {
        Admin admin = new Admin();
        admin.setAdminId(1L);
        User user = new User();
        user.setUsername("adminUser");
        admin.setUser(user);

        when(adminService.getAdminById(1L)).thenReturn(Optional.of(admin));

        mockMvc.perform(get("/admins/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.admin_id", is("1")))
                .andExpect(jsonPath("$.username", is("adminUser")));

        verify(adminService, times(1)).getAdminById(1L);
    }

    @Test
    @WithMockUser
    void testGetAdminById_whenAdminNotFound() throws Exception {
        when(adminService.getAdminById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/admins/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", is("Admin not found")));

        verify(adminService, times(1)).getAdminById(1L);
    }

    @Test
    @WithMockUser
    void testUpdateAdmin_whenAdminExists() throws Exception {
        doNothing().when(adminService).updateAdmin(1L, "newAdmin", "newPassword");

        mockMvc.perform(put("/admins/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"newAdmin\", \"password\": \"newPassword\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Admin updated successfully")));

        verify(adminService, times(1)).updateAdmin(1L, "newAdmin", "newPassword");
    }

    @Test
    @WithMockUser
    void testUpdateAdmin_whenAdminNotFound() throws Exception {
        doThrow(new RuntimeException("Admin not found")).when(adminService).updateAdmin(1L, "newAdmin", "newPassword");

        mockMvc.perform(put("/admins/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"newAdmin\", \"password\": \"newPassword\"}"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", is("Admin not found")));

        verify(adminService, times(1)).updateAdmin(1L, "newAdmin", "newPassword");
    }

    @Test
    void testDeleteAdmin_whenAdminExists() throws Exception {
        doNothing().when(adminService).deleteAdmin(1L);

        mockMvc.perform(delete("/admins/delete/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Admin deleted successfully")));

        verify(adminService, times(1)).deleteAdmin(1L);
    }

    @Test
    void testDeleteAdmin_whenAdminNotFound() throws Exception {
        doThrow(new RuntimeException("Admin not found")).when(adminService).deleteAdmin(1L);

        mockMvc.perform(delete("/admins/delete/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", is("Admin not found")));

        verify(adminService, times(1)).deleteAdmin(1L);
    }
}