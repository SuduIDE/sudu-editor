package org.sudu.experiments.webdemo;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
class WebDemoController {

  final SessionManager sm = new SessionManager();

  @GetMapping("/hello")
  public ResponseEntity<String> hello() {
    String result = "hello response";
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @PostMapping("/newSession")
  public ResponseEntity<String> newSession(
      @RequestBody() String body
  ) {
    String newID = sm.newSession(s -> s.setSource(body));
    return new ResponseEntity<>(newID, HttpStatus.OK);
  }

  @PostMapping("/setSrc")
  public ResponseEntity<String> setSrc(
      @RequestParam("id") String id,
      @RequestBody() String body
  ) {
    WebSession session = sm.getSession(id);
    if (session != null) {
      session.setSource(body);
      return new ResponseEntity<>(HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
  }

  @PostMapping("/setSeed")
  public ResponseEntity<String> setSeed(
      @RequestParam("id") String id,
      @RequestBody() String body
  ) {
    WebSession session = sm.getSession(id);
    if (session != null) {
      try {
        session.setSeed(Integer.parseInt(body));
        return new ResponseEntity<>(HttpStatus.OK);
      } catch (NumberFormatException e) {
        System.err.println("bad setSeed: " + body);
      }
    }
    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
  }

  @GetMapping("/src")
  public ResponseEntity<String> src(
      @RequestParam("id") String id
  ) {
    WebSession session = sm.getSession(id);
    if (session != null) {
      String source = session.source();
      return new ResponseEntity<>(source, HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
  }

  @GetMapping("/dag1")
  public ResponseEntity<String> dag1(
      @RequestParam("id") String id
  ) {
    WebSession session = sm.getSession(id);
    if (session != null) {
      String result = session.dag1();
      return new ResponseEntity<>(result, HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
  }

  @GetMapping("/dag2")
  public ResponseEntity<String> dag2(
      @RequestParam("id") String id
  ) {
    WebSession session = sm.getSession(id);
    if (session != null) {
      String result = session.dag2();
      return new ResponseEntity<>(result, HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
  }


  @GetMapping("/calculatePaths")
  public ResponseEntity<String> calculatePaths(
      @RequestParam("id") String id
  ) {
    WebSession session = sm.getSession(id);
    if (session != null) {
      String result = session.calculatePaths();
      return new ResponseEntity<>(result, HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
  }

  @GetMapping("/highlight")
  public ResponseEntity<String> highlight(
      @RequestParam("id") String id,
      @RequestParam("pathGroupIdx") String pathGroupIdx,
      @RequestParam("idx") String idx
  ) {
    WebSession session = sm.getSession(id);
    if (session != null) {
      try {
        int groupIndex = Integer.parseInt(pathGroupIdx);
        int index = Integer.parseInt(idx);
        String result = session.highlight(groupIndex, index);
        return new ResponseEntity<>(result, HttpStatus.OK);
      } catch (NumberFormatException | ArrayIndexOutOfBoundsException | NullPointerException ignored) {}
    }
    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
  }
}
