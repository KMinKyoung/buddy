package me.minkyoung.buddy_back.config;

import com.oracle.bmc.Region;
import com.oracle.bmc.auth.InstancePrincipalsAuthenticationDetailsProvider;
import com.oracle.bmc.objectstorage.ObjectStorageClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OciObjectStorageConfig {

    @Value("${oci.region}")
    private  String regionId;

    @Bean
    public ObjectStorageClient objectStorageClient() throws Exception{
        var provider = InstancePrincipalsAuthenticationDetailsProvider.builder().build();
        var client = ObjectStorageClient.builder().build(provider);
        client.setRegion(Region.fromRegionId(regionId));
        return client;
    }
}
