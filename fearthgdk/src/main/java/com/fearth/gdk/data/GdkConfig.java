package com.fearth.gdk.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GdkConfig {
    public int id;
    public String name;
}
