package com.jeanramal.autosendmsgwhatsapp.payload;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import feign.Response;

@FeignClient(name = "sending-notification", url = "https://chromedriver.storage.googleapis.com")
public interface ChromeDriverClient {

  @GetMapping(name = "getLatestRelease", value = "/LATEST_RELEASE")
  String getLatestRelease();

  @GetMapping(name = "getLatestReleaseFile", value = "/{lastestRelease}/chromedriver_win32.zip")
  Response getLatestReleaseFile(@PathVariable String lastestRelease);

}
