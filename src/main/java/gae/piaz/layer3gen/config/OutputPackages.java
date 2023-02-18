package gae.piaz.layer3gen.config;

import lombok.Data;

@Data
public class OutputPackages {
    private String repositories;
    private String services;
    private String controllers;
    private String dtos;
    private String mappers;
}
