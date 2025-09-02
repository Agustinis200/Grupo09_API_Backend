package com.uade.tpo.marketplace.service.image;

import java.io.IOException;
import java.sql.SQLException;

import javax.sql.rowset.serial.SerialException;

import org.springframework.stereotype.Service;

import com.uade.tpo.marketplace.controllers.image.AddFileRequest;
import com.uade.tpo.marketplace.entity.Image;

@Service
public interface ImageService {
    public Image create(AddFileRequest image) throws IOException, SerialException, SQLException;

    public Image viewById(long id);
}
