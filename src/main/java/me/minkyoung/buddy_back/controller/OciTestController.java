package me.minkyoung.buddy_back.controller;

import com.oracle.bmc.objectstorage.ObjectStorageClient;
import com.oracle.bmc.objectstorage.requests.GetNamespaceRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/oci")
//@Profile("prod")
public class OciTestController {

    private final ObjectStorageClient client;

    @GetMapping("/namespace")
    public String namespace() {
        return client.getNamespace(GetNamespaceRequest.builder().build()).getValue();
    }
}