package com.commerciale.service;

import java.awt.Image;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.commerciale.model.Product;

@Converter
public class ImageConverter implements AttributeConverter<Image, byte[]> {
	private Image image;
	private Product product;
	@Override
	public byte[] convertToDatabaseColumn(Image arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Image convertToEntityAttribute(byte[] arg0) {
		 try {
			image = ImageIO.read(new ByteArrayInputStream(arg0));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return image;
	}
	
}
