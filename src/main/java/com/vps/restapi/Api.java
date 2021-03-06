package com.vps.restapi;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.apache.commons.mail.EmailException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringVersion;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.vps.restapi.model.User;
import com.vps.restapi.model.UserRepository;
import com.vps.restapi.utils.CommonUtils;
import com.vps.restapi.utils.EmailSender;
import com.vps.restapi.utils.UserUtils;

import freemarker.core.ParseException;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;

//mapowanie rest
@RestController
@RequestMapping("/user")
public class Api {
	private static final Logger LOG = LogManager.getLogger(Api.class);

	@Autowired
	private UserRepository userRepository;

	@PostConstruct
	public void init() {
		LOG.info("Startuje api" + SpringVersion.getVersion());
	}

	@RequestMapping(method = { RequestMethod.POST })
	public ResponseEntity<User> registerUser(@RequestBody User userData) throws EmailException, TemplateNotFoundException,
			MalformedTemplateNameException, ParseException, IOException, TemplateException {
		// ==================================================================
		LOG.info("user received");
		// ==================================================================

		if (userRepository.findByEmail(userData.getEmail()).isPresent()) {
			LOG.error("User already exist");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}

		String email = userData.getEmail();
		userData.setToken(CommonUtils.generateUuid());
		userData.setPermissions("user");
		CommonUtils.makeTheFolder(userData);
//		CommonUtils.saveProfilePic(profilePic, userData);
		
		
			if (email == null || email.isEmpty()) {
			// ==================================================================
			if (LOG.isDebugEnabled()) {
				LOG.debug("empty email");
			}
			// ==================================================================
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		String uid = userData.getUid();
		if (uid == null || uid.isEmpty()) {
			userData.setActive(true);
			EmailSender.newAccountEmail(userData);
			LOG.info("User inserted");
			return ResponseEntity.ok(userRepository.insert(userData));

		} else {
			Optional<User> userFromDatabase = userRepository.findById(userData.getUid());

			if (userFromDatabase.isPresent()) {
				// ==================================================================
				if (LOG.isDebugEnabled()) {
					LOG.debug("user already exist: " + email);
				}
				// ==================================================================
				return ResponseEntity.status(HttpStatus.CONFLICT).build();
			}
		}
		return ResponseEntity.status(HttpStatus.OK).build();

	}

	@RequestMapping(path = "/{status}", method = RequestMethod.GET)
	public List<User> getAll(@PathVariable("status") String status) {
		if (status == null || status.isEmpty()) {
			return userRepository.findAll();
		}
		if (status.equals("active")) {
			return userRepository.findByActive(true);
		}
		return userRepository.findByActive(false);
	}

	@RequestMapping(path = "/update", method = RequestMethod.PUT)
	public ResponseEntity<User> update(@RequestBody User userData) throws Exception {
		Optional<User> userFromDatabase = userRepository.findById(userData.getUid());
		if (!userFromDatabase.isPresent()) {
			// ==================================================================
			if (LOG.isDebugEnabled()) {
				LOG.debug("User not found by: " + userData.getEmail());
			}
			// ==================================================================
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		User user = userFromDatabase.get();
		if (UserUtils.checkIfEqual(userData, user)) {
			LOG.info("No change in user");
			return ResponseEntity.status(HttpStatus.CONFLICT).build();

		}
		userData.setUid(user.getUid());
		LOG.info("User edited");
		EmailSender.editAccountEmail(userFromDatabase, userData);
		return ResponseEntity.ok(userRepository.save(userData));

	}

	@RequestMapping(path = "/delete/{userId}", method = RequestMethod.DELETE)
	public ResponseEntity<User> delete(@PathVariable("userId") String userId) throws EmailException {
		Optional<User> userFromDatabase = userRepository.findById(userId);
		if (!userFromDatabase.isPresent()) {
			// ==================================================================
			if (LOG.isDebugEnabled()) {
				LOG.debug("User not found by: " + userId);
			}
			// ==================================================================
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		User userDeleted = userFromDatabase.get();
		userDeleted.setActive(false);
		EmailSender.deleteAccountEmail(userDeleted);

		LOG.info("User Deleted :" + userDeleted.getEmail());

		return ResponseEntity.ok(userRepository.save(userDeleted));

	}

	@RequestMapping(path = "confirm/{token}")
	public ResponseEntity<User> checkConfirmationEmail(@PathVariable("token") String token) throws EmailException {

		Optional<User> userFromDatabase = userRepository.findByToken(token);
		if (!userFromDatabase.isPresent()) {
			// ==================================================================
			if (LOG.isDebugEnabled()) {
				LOG.debug("User not found by: " + token);
			}
			// ==================================================================
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		User userConfirmed = userFromDatabase.get();
		Boolean confirmed = userConfirmed.getConfirmed();
		if (confirmed == null) {
			userConfirmed.setConfirmed(true);
			EmailSender.confirmationAccountEmail(userConfirmed);
			LOG.info("User Confirmed :" + userConfirmed.getEmail());
			ResponseEntity.ok(userRepository.save(userConfirmed));
		} else {
			// ==================================================================
			if (LOG.isDebugEnabled()) {
				LOG.debug("User already confirmed by: " + token);
			}
			ResponseEntity.status(HttpStatus.CREATED).build();
		}
		return new ResponseEntity<User>(CommonUtils.redirectUrl(), HttpStatus.SEE_OTHER);

	}

	@RequestMapping(path = "/login", method = RequestMethod.PUT)
	public ResponseEntity<User> loginUser(@RequestBody User userLog) throws EmailException {
		Optional<User> userFromDatabase = userRepository.findByEmail(userLog.getEmail());
		LOG.info("user to login received");
		if (!userFromDatabase.isPresent()) {
			// ==================================================================
			LOG.debug("User not found by: " + userLog.getEmail());

			// ==================================================================
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		LOG.info("User found");
		User userToCheck = userFromDatabase.get();
		if (!userLog.getPassword().equals(userToCheck.getPassword())) {

			LOG.error("Invalid Password");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		return new ResponseEntity<User>(CommonUtils.loginUrl(), HttpStatus.ACCEPTED);

	}

	@RequestMapping(path = "/login/changepass", method = RequestMethod.PUT)
	public @ResponseBody ResponseEntity<User> userChangePassword(@RequestBody User userLog) throws EmailException,
			TemplateNotFoundException, MalformedTemplateNameException, ParseException, IOException, TemplateException {
		Optional<User> userFromDatabase = userRepository.findByEmail(userLog.getEmail());
		LOG.info("email received");
		if (!userFromDatabase.isPresent()) {
			// ==================================================================
			LOG.debug("User not found by: " + userLog.getEmail());

			// ==================================================================
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		LOG.info("User found");
		User userPassword = userFromDatabase.get();
		EmailSender.changePasswordEmail(userPassword);

		return new ResponseEntity<User>(HttpStatus.ACCEPTED);

	}

	@RequestMapping(path = "/changepass", method = RequestMethod.PUT)
	public ResponseEntity<User> passwordChange(@RequestBody User userLog) throws EmailException {
		Optional<User> userFromDatabase = userRepository.findByEmail(userLog.getEmail());
		LOG.info("user to passchange received");
		if (!userFromDatabase.isPresent()) {
			// ==================================================================
			LOG.debug("User not found by: " + userLog.getEmail());

			// ==================================================================
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		LOG.info("User found");
		User userToCheck = userFromDatabase.get();
		if (userLog.getPassword().equals(userToCheck.getPassword())) {

			LOG.error("No change in password");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		} else {
			userRepository.save(userToCheck);
			LOG.info("Password changed");
		}
		return new ResponseEntity<User>(HttpStatus.ACCEPTED);

	}
	
	@RequestMapping(path = "/profile", method = RequestMethod.GET)
	public ResponseEntity<User> getUserProfileData(@PathVariable("email") String email) throws EmailException {
		Optional<User> userFromDatabase = userRepository.findByEmail(email);
		LOG.info("user to passchange received");
		if (!userFromDatabase.isPresent()) {
			// ==================================================================
			LOG.debug("User not found by: " + email);

			// ==================================================================
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		LOG.info("User found");
		return new ResponseEntity<User>(userFromDatabase.get(), HttpStatus.ACCEPTED);

	}

}