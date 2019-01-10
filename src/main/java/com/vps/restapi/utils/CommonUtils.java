package com.vps.restapi.utils;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.apache.catalina.startup.UserDatabase;
import org.springframework.http.HttpHeaders;

import com.vps.restapi.model.User;

public class CommonUtils {
	public static final String generateUuid() {
		return UUID.randomUUID().toString();

	}

	public static HttpHeaders redirectUrl() {
		HttpHeaders headers = new HttpHeaders();
		String redirectUrl = "http://localhost:4200/";
		headers.add("Location", redirectUrl);
		return headers;
	}

	public static HttpHeaders loginUrl() {
		HttpHeaders headers = new HttpHeaders();
		String redirectUrl = "http://localhost:4200/home";
		headers.add("Location", redirectUrl);
		return headers;
	}

	public static HttpHeaders PasswordUrl() {
		HttpHeaders headers = new HttpHeaders();
		String redirectUrl = "http://localhost:4200/passwordChange";
		headers.add("Location", redirectUrl);
		return headers;
	}
	
	public static User makeTheFolder(User userData) {
		File profilePicDir = new File("//src//main/resources//"+userData.getToken());

		// if the directory does not exist, create it
		if (!profilePicDir.exists()) {
		    System.out.println("creating directory: " + profilePicDir.getName());
		    boolean result = false;

		    try{
		        profilePicDir.mkdir();
		        userData.setProfileurl(profilePicDir.getName());
		        result = true;
		    } 
		    catch(SecurityException se){
		        //handle it
		    }        
		    if(result) {    
		        System.out.println("DIR created");  
		    }
		}
		return userData;
	}
	public static void saveProfilePic(BufferedImage profilePic, User userData) {
		try{
		
           File profileSaved= new File(userData.getToken()+".jpg");
            ImageIO.write(profilePic,"jpg", profileSaved);
        }catch(IOException e){
            e.printStackTrace();
        }
    }
		
	}

