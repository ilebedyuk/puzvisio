package com.puzlvisio.web;

import com.puzlvisio.domain.entities.Picture;
import com.puzlvisio.service.GalleryService;
import com.puzlvisio.service.PictureService;
import com.puzlvisio.utils.ImageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by Chudov A.V. on 11/17/2016.
 */
@Controller
public class ImageController {

	@Autowired
	private ImageUtil imageUtil;

	@Autowired
	private PictureService pictureService;

	@Autowired
	private GalleryService galleryService;

	private static final String JPG = "jpg";
	private static final String CONTENT_TYPE = "image/jpeg";

	@RequestMapping(value = "/picture/{pictureId}/image", method = RequestMethod.GET)
	public void getPictureImage(@PathVariable String pictureId, HttpServletRequest request,
						HttpServletResponse response) throws IOException {

		BufferedImage imgP = ImageIO.read(imageUtil.getImage(pictureService.getById(pictureId)));
		getImage(response, imgP);
	}

	@RequestMapping(value = "/gallery/{galleryId}/image", method = RequestMethod.GET)
	public void getGalleryImage(@PathVariable String galleryId, HttpServletRequest request,
						HttpServletResponse response) throws IOException {

		BufferedImage imgP = ImageIO.read(imageUtil.getGalleryImage(galleryService.getById(galleryId)));
		getImage(response, imgP);
	}

	private void getImage(HttpServletResponse response, BufferedImage imgP) throws IOException {
		response.setContentType(CONTENT_TYPE);
		OutputStream osP = response.getOutputStream();
		ImageIO.write(imgP, JPG, osP);
		osP.close();
	}

	@RequestMapping(value = "/picture/{pictureId}/image", method = RequestMethod.POST)
	public void savePictureImage(@PathVariable String pictureId,
								 @RequestParam("file") MultipartFile file,
								 HttpServletRequest request,
								HttpServletResponse response) throws IOException {

		Picture picture = pictureService.getById(pictureId);

		if(picture != null) {
			imageUtil.saveImage(file, picture);
		} else {
			response.sendError(400, "Cannot find picture with id: " + pictureId);
		}
	}

	@RequestMapping(value = "/picture/test", method = RequestMethod.POST)
	public void	testRest(
						@RequestPart("json") String json,
						@RequestPart("file") MultipartFile file,
						HttpServletRequest request,
						HttpServletResponse response) {

		System.out.println("Test REST. File: " + file.getOriginalFilename());
		System.out.println("Test REST. JSON: " + json);

	}
}
