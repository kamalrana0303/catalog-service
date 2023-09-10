package com.whatbook.catalogservice.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.MappedSuperclass;

@Getter
@Setter
@MappedSuperclass
public class BaseId {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private  Long id;
}
