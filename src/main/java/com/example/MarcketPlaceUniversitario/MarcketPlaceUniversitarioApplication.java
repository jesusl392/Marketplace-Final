package com.example.MarcketPlaceUniversitario;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MarcketPlaceUniversitarioApplication {

	public static void main(String[] args) {
		loadEnv();
		SpringApplication.run(MarcketPlaceUniversitarioApplication.class, args);
		System.out.println("MarcketPlaceUniversitarioApplication started");
	}
	private static void loadEnv(){
		// En Render el Secret File está en /etc/secrets/.env
		// En local está en la raíz del proyecto
		Dotenv dotenv = Dotenv.configure()
				.directory("/etc/secrets")
				.ignoreIfMissing()
				.load();

		// Si no encontró variables (estamos en local), cargar desde raíz
		if (dotenv.get("DB_URL") == null) {
			dotenv = Dotenv.configure().ignoreIfMissing().load();
		}

		setIfPresent(dotenv, "DB_URL");
		setIfPresent(dotenv, "DB_USERNAME");
		setIfPresent(dotenv, "DB_PASSWORD");
		setIfPresent(dotenv, "CLOUDINARY_CLOUD_NAME");
		setIfPresent(dotenv, "CLOUDINARY_API_KEY");
		setIfPresent(dotenv, "CLOUDINARY_API_SECRET");
		setIfPresent(dotenv, "BREVO_API_KEY");
	}

	private static void setIfPresent(Dotenv dotenv, String key) {
		String value = dotenv.get(key);
		if (value != null) System.setProperty(key, value);
	}


}
