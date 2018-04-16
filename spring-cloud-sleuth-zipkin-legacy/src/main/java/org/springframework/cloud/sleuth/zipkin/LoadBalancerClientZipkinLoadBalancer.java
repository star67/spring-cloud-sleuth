package org.springframework.cloud.sleuth.zipkin;

import java.net.URI;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;

class LoadBalancerClientZipkinLoadBalancer implements ZipkinLoadBalancer {

	private final LoadBalancerClient loadBalancerClient;
	private final ZipkinProperties zipkinProperties;

	LoadBalancerClientZipkinLoadBalancer(LoadBalancerClient loadBalancerClient,
			ZipkinProperties zipkinProperties) {
		this.loadBalancerClient = loadBalancerClient;
		this.zipkinProperties = zipkinProperties;
	}

	@Override
	public URI instance() {
		if (this.loadBalancerClient != null) {
			URI uri = URI.create(this.zipkinProperties.getBaseUrl());
			String host = uri.getHost();
			ServiceInstance instance = this.loadBalancerClient.choose(host);
			if (instance != null) {
				return instance.getUri();
			}
		}
		return URI.create(this.zipkinProperties.getBaseUrl());
	}
}