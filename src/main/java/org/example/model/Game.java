package org.example.model;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class Game {
    private int id;
    private String name;
    private Date releaseDate;
    private float rating;
    private float cost;
    private String description;
}
