package me.snov.newrelic.elasticsearch.services;

import me.snov.newrelic.elasticsearch.responses.ClusterHealth;

import java.util.HashSet;
import java.util.List;

public class ClusterHealthService {

	public boolean isYellow(ClusterHealth clusterHealth) {
		return "yellow".equals(clusterHealth.status);
	}

	public boolean isRed(ClusterHealth clusterHealth) {
		return "red".equals(clusterHealth.status);
	}
}
