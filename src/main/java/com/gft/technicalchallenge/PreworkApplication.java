package com.gft.technicalchallenge;

import com.gft.technicalchallenge.reactivex.TreeObserver;
import com.gft.technicalchallenge.reactivex.TreeReactiveStream;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import rx.Observable;

import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;

@SpringBootApplication
public class PreworkApplication {

	public static void main(String[] args) throws IOException {
		SpringApplication.run(PreworkApplication.class, args);
	}

}
