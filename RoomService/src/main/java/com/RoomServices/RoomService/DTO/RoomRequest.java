package com.RoomServices.RoomService.DTO;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import lombok.Data;

@Data
public class RoomRequest {

    private String name;
    private Double price;

    private List<MultipartFile> images;
}
