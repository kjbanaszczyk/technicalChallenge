package com.gft.technicalchallenge.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Created by klbk on 12/12/2016.
 */

@RequestMapping("/")
@RestController
public class FilesController {

    Logger logger = Logger.getLogger(FilesController.class.getName());

    @CrossOrigin
    @RequestMapping(path = "/addFile", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<String> addFile(@RequestBody String path) throws IOException {
        File file = new File(path);

        if(file.createNewFile())
            return new ResponseEntity<>("\"" + file.getName() + " created\"", HttpStatus.OK);
        else
            return new ResponseEntity<>("\"" + file.getName() + " already exist\"", HttpStatus.OK);

    }

    @CrossOrigin
    @RequestMapping(path = "/removeFile", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<String> removeFile(@RequestBody String path) throws IOException {
        File file = new File(path);
        if(file.delete())
            return new ResponseEntity<>("\"" + file.getName() + " deleted\"", HttpStatus.OK);
        else
            return new ResponseEntity<>("\"" + file.getName() + " can't be deleted\"", HttpStatus.OK);
    }

}
