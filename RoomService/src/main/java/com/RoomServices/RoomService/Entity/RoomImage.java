package com.RoomServices.RoomService.Entity;

import lombok.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table
public class RoomImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imagePath;

    @ManyToOne
    @JoinColumn(name = "room_id")
    @JsonIgnore
    @JsonBackReference
    @ToString.Exclude
    private Room room;

    @JsonProperty("imageUrl")
    public String getImageUrl() {
        String fileName = imagePath.replace("uploads/", "");
        return "http://localhost:8080/api/rooms/image/" + fileName;
    }

}
