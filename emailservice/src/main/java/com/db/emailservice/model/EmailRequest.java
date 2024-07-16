package com.db.emailservice.model;

import java.io.Serializable;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailRequest implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@NotBlank(message = "Email address cannot be null or empty")
	@Email(message = "Email should be valid")
	private String emailTo;
	
	@NotBlank(message = "Email subject cannot be null or empty")
	private String subject;
	
	@NotBlank(message = "Email message cannot be null or empty")
	private String message;
}
