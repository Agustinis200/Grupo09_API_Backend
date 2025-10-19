package com.uade.tpo.marketplace.service.image;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;

import javax.sql.rowset.serial.SerialException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.tpo.marketplace.controllers.image.AddFileRequest;
import com.uade.tpo.marketplace.entity.Image;
import com.uade.tpo.marketplace.entity.Product;
import com.uade.tpo.marketplace.repository.ImageRepository;
import com.uade.tpo.marketplace.repository.ProductRepository;

@Service
public class ImageServiceImpl implements ImageService {
    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Image create(AddFileRequest request) throws IOException, SerialException, SQLException {
        byte[] bytes = request.getFile().getBytes();
        Blob blob = new javax.sql.rowset.serial.SerialBlob(bytes);
        Product product = productRepository.findById(request.getProductId()).get();
        return imageRepository.save(Image.builder().image(blob).product(product).build());
    }

    @Override
    public Image viewById(long id) {
        return imageRepository.findById(id).get();
    }

    public void delete(long id) {
        imageRepository.deleteById(id);
    }

}
