package me.snov.newrelic.elasticsearch.services;

import me.snov.newrelic.elasticsearch.responses.ClusterHealth;

import java.util.HashSet;
import java.util.List;

public class ClusterHealthService {

	public boolean isYellow(ClusterHealth ClusterHealth) {
		return "yellow".equals(ClusterHealth.status);
	}

	public boolean isRed(ClusterHealth ClusterHealth) {
		return "red".equals(ClusterHealth.status);
	}
}
