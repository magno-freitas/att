package main.java.vet.service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import main.java.vet.model.Pet;
import main.java.vet.util.DatabaseConnection;
import main.java.vet.exception.ValidationException;

public class PetService {
    
    public void addPet(Pet pet) throws SQLException {
        String query = "INSERT INTO pets (client_id, name, species, breed, birth_date) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, pet.getClientId());
            stmt.setString(2, pet.getName());
            stmt.setString(3, pet.getSpecies());
            stmt.setString(4, pet.getBreed());
            stmt.setDate(5, pet.getBirthDate());
            
            stmt.executeUpdate();
            
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    pet.setId(rs.getInt(1));
                }
            }
        }
    }

    public Pet getPetById(int petId) throws SQLException {
        String query = "SELECT * FROM pets WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, petId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Pet pet = new Pet();
                pet.setId(rs.getInt("id"));
                pet.setClientId(rs.getInt("client_id"));
                pet.setName(rs.getString("name"));
                pet.setSpecies(rs.getString("species"));
                pet.setBreed(rs.getString("breed"));
                pet.setBirthDate(rs.getDate("birth_date"));
                return pet;
            }
            return null;
        }
    }

    public boolean checkVaccinesUpToDate(int petId) throws SQLException {
        String query = "SELECT COUNT(*) FROM vaccine_records WHERE pet_id = ? AND next_dose_date < CURRENT_DATE";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, petId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) == 0; // Returns true if no vaccines are due
            }
            return true;
        }
    }

    public List<Pet> getPetsByClient(int clientId) throws SQLException {
        String query = "SELECT * FROM pets WHERE client_id = ?";
        List<Pet> pets = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, clientId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Pet pet = new Pet();
                pet.setId(rs.getInt("id"));
                pet.setClientId(rs.getInt("client_id"));
                pet.setName(rs.getString("name"));
                pet.setSpecies(rs.getString("species"));
                pet.setBreed(rs.getString("breed"));
                pet.setBirthDate(rs.getDate("birth_date"));
                pets.add(pet);
            }
        }
        return pets;
    }
}