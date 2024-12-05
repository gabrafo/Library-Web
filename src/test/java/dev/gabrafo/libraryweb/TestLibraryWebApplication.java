package dev.gabrafo.libraryweb;

import org.springframework.boot.SpringApplication;

public class TestLibraryWebApplication {

	public static void main(String[] args) {
		SpringApplication.from(LibraryWebApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
